/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.cassandra.core;

import java.util.Iterator;
import java.util.List;

import org.springframework.cassandra.core.CqlOperations;
import org.springframework.cassandra.core.QueryOptions;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.cassandra.convert.CassandraConverter;

import com.datastax.driver.core.Statement;

/**
 * Operations for interacting with Cassandra. These operations are used by the Repository implementation, but can also
 * be used directly when that is desired by the developer.
 *
 * @author Alex Shvid
 * @author David Webb
 * @author Matthew Adams
 * @author Mark Paluch
 * @see CassandraTemplate
 * @see CqlOperations
 * @see Statement
 */
public interface CassandraOperationsNG {

	/**
	 * The table name used for the specified class by this template.
	 *
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the {@link CqlIdentifier}
	 */
	CqlIdentifier getTableName(Class<?> entityClass);

	/**
	 * Executes the given select {@code query} on the entity table of the specified {@code type} backed by a Cassandra
	 * {@link com.datastax.driver.core.ResultSet}.
	 * <p>
	 * Returns a {@link Iterator} that wraps the Cassandra {@link com.datastax.driver.core.ResultSet}.
	 *
	 * @param <T> element return type.
	 * @param query query to execute. Must not be empty or {@literal null}.
	 * @param entityClass Class type of the elements in the {@link Iterator} stream. Must not be {@literal null}.
	 * @return an {@link Iterator} (stream) over the elements in the query result set.
	 * @throws DataAccessException if there is any problem executing the query
	 * @since 1.5
	 */
	<T> Iterator<T> stream(String query, Class<T> entityClass) throws DataAccessException;

	/**
	 * Execute query and convert ResultSet to the list of entities.
	 *
	 * @param cql must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the converted results
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> List<T> select(String cql, Class<T> entityClass) throws DataAccessException;

	/**
	 * Execute the Select Query and convert to the list of entities.
	 *
	 * @param select must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the converted results
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> List<T> select(Statement select, Class<T> entityClass) throws DataAccessException;

	/**
	 * Select objects for the given {@code entityClass} and {@code ids}.
	 *
	 * @param entityClass The entity type must not be {@literal null}.
	 * @param ids must not be {@literal null}.
	 * @return the converted results
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> List<T> selectBySimpleIds(Class<T> entityClass, Iterable<?> ids) throws DataAccessException;

	/**
	 * Execute the Select by {@code id} for the given {@code entityClass}.
	 *
	 * @param entityClass The entity type must not be {@literal null}.
	 * @param id must not be {@literal null}.
	 * @return the converted object or {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> T selectOneById(Class<T> entityClass, Object id) throws DataAccessException;

	/**
	 * Execute CQL and convert ResultSet to the entity
	 *
	 * @param cql must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the converted object or {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> T selectOne(String cql, Class<T> entityClass) throws DataAccessException;

	/**
	 * Execute Select query and convert ResultSet to the entity
	 *
	 * @param select must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the converted object or {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> T selectOne(Statement select, Class<T> entityClass) throws DataAccessException;

	/**
	 * Determine whether the row {@code entityClass} with the given {@code id} exists.
	 *
	 * @param entityClass The entity type must not be {@literal null}.
	 * @param id must not be {@literal null}.
	 * @return true, if the object exists
	 * @throws DataAccessException if there is any problem executing the query
	 */
	boolean exists(Class<?> entityClass, Object id) throws DataAccessException;

	/**
	 * Returns the number of rows for the given {@code entityClass} by querying the table of the given entity class.
	 *
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return number of rows
	 * @throws DataAccessException if there is any problem executing the query
	 */
	long count(Class<?> entityClass) throws DataAccessException;

	/**
	 * Insert the given entity.
	 *
	 * @param entity The entity to insert
	 * @return The entity given
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> T insert(T entity) throws DataAccessException;

	/**
	 * Insert the given entity.
	 *
	 * @param entity The entity to insert
	 * @param options The {@link WriteOptions} to use.
	 * @return The entity given
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> T insert(T entity, WriteOptions options) throws DataAccessException;

	/**
	 * Insert the given list of entities.
	 *
	 * @param entities The entities to insert.
	 * @return The entities given.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> List<T> insert(List<T> entities) throws DataAccessException;

	/**
	 * Insert the given list of entities.
	 *
	 * @param entities The entities to insert.
	 * @param options The {@link WriteOptions} to use.
	 * @return The entities given.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> List<T> insert(List<T> entities, WriteOptions options) throws DataAccessException;

	/**
	 * Update the given entity.
	 *
	 * @param entity The entity to update
	 * @return The entity given
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> T update(T entity) throws DataAccessException;

	/**
	 * Update the given entity.
	 *
	 * @param entity The entity to update
	 * @param options The {@link WriteOptions} to use.
	 * @return The entity given
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> T update(T entity, WriteOptions options) throws DataAccessException;

	/**
	 * Update the given list of entities.
	 *
	 * @param entities The entities to update.
	 * @return The entities given.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> List<T> update(List<T> entities) throws DataAccessException;

	/**
	 * Update the given list of entities.
	 *
	 * @param entities The entities to update.
	 * @param options The {@link WriteOptions} to use.
	 * @return The entities given.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> List<T> update(List<T> entities, WriteOptions options) throws DataAccessException;

	/**
	 * Remove the given object from the table by id.
	 *
	 * @param entityClass The entity type must not be {@literal null}.
	 * @param id must not be {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	void deleteById(Class<?> entityClass, Object id) throws DataAccessException;

	/**
	 * Remove the given object from the table by id.
	 *
	 * @param entity must not be {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> void delete(T entity) throws DataAccessException;

	/**
	 * Remove the given object from the table by id.
	 *
	 * @param entity must not be {@literal null}.
	 * @param options may be {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> void delete(T entity, QueryOptions options) throws DataAccessException;

	/**
	 * Remove the given objects from the table by id.
	 *
	 * @param entities must not be {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> void delete(List<T> entities) throws DataAccessException;

	/**
	 * Remove the given objects from the table by id.
	 *
	 * @param entities must not be {@literal null}.
	 * @param options may be {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> void delete(List<T> entities, QueryOptions options) throws DataAccessException;

	/**
	 * Deletes all entities of a given class.
	 * 
	 * @param entityClass The entity type must not be {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query
	 */
	<T> void deleteAll(Class<T> entityClass) throws DataAccessException;

	/**
	 * Returns a new {@link CassandraBatchOperations}. Each {@link CassandraBatchOperations} instance can be executed only
	 * once so you might want to obtain new {@link CassandraBatchOperations} instances for each batch.
	 *
	 * @return a new {@link CassandraBatchOperations} associated with the given entity class.
	 */
	CassandraBatchOperations batchOps();

	/**
	 * Returns the underlying {@link CassandraConverter}.
	 *
	 * @return the underlying {@link CassandraConverter}.
	 */
	CassandraConverter getConverter();

}
