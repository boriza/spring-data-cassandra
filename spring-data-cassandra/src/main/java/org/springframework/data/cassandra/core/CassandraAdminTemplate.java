/*
 * Copyright 2013-2016 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.cassandra.core;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cassandra.core.SessionCallback;
import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.cassandra.core.cql.generator.CreateTableCqlGenerator;
import org.springframework.cassandra.core.cql.generator.DropTableCqlGenerator;
import org.springframework.cassandra.core.keyspace.CreateTableSpecification;
import org.springframework.cassandra.core.keyspace.DropTableSpecification;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.mapping.CassandraPersistentEntity;
import org.springframework.util.Assert;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;

/**
 * Default implementation of {@link CassandraAdminOperations}.
 *
 * @author Mark Paluch
 */
public class CassandraAdminTemplate extends CassandraTemplate implements CassandraAdminOperations {

	private static final Logger log = LoggerFactory.getLogger(CassandraAdminTemplate.class);

	/**
	 * Constructor used for a basic template configuration
	 *
	 * @param session must not be {@literal null}.
	 * @param converter must not be {@literal null}.
	 */
	public CassandraAdminTemplate(Session session, CassandraConverter converter) {
		super(session, converter);
	}

	@Override
	public void createTable(final boolean ifNotExists, final CqlIdentifier tableName, Class<?> entityClass,
			Map<String, Object> optionsByName) {

		CassandraPersistentEntity<?> entity = getPersistentEntity(entityClass);
		CreateTableSpecification createTableSpecification = getConverter().getMappingContext()
				.getCreateTableSpecificationFor(entity).ifNotExists(ifNotExists);

		getCqlOperations().execute(CreateTableCqlGenerator.toCql(createTableSpecification));
	}

	public void dropTable(Class<?> entityClass) {
		dropTable(getTableName(entityClass));
	}

	@Override
	public void dropTable(CqlIdentifier tableName) {
		getCqlOperations().execute(DropTableCqlGenerator.toCql(DropTableSpecification.dropTable(tableName)));
	}

	@Override
	public TableMetadata getTableMetadata(String keyspace, CqlIdentifier tableName) {

		Assert.notNull(tableName, "Table name must not be null");

		return getCqlOperations().execute((SessionCallback<TableMetadata>) session -> session.getCluster().getMetadata()
				.getKeyspace(keyspace).getTable(tableName.toCql()));
	}
}
