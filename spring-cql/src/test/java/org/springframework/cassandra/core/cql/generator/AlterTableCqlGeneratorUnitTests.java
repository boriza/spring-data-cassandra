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
package org.springframework.cassandra.core.cql.generator;

import static org.assertj.core.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.cassandra.core.keyspace.AlterTableSpecification;
import org.springframework.cassandra.core.keyspace.TableOption;
import org.springframework.cassandra.core.keyspace.TableOption.CachingOption;
import org.springframework.cassandra.core.keyspace.TableOption.KeyCachingOption;

import com.datastax.driver.core.DataType;

/**
 * Unit tests for {@link AlterTableCqlGenerator}.
 *
 * @author Matthew T. Adams
 * @author David Webb
 * @author Mark Paluch
 */
public class AlterTableCqlGeneratorUnitTests {

	/**
	 * @see DATACASS-192
	 */
	@Test
	public void alterTableAlterColumnType() {

		AlterTableSpecification spec = AlterTableSpecification.alterTable("addamsFamily").alter("lastKnownLocation",
				DataType.uuid());

		assertThat(toCql(spec)).isEqualTo("ALTER TABLE addamsfamily ALTER lastknownlocation TYPE uuid;");
	}

	/**
	 * @see DATACASS-192
	 */
	@Test
	public void alterTableAlterListColumnType() {

		AlterTableSpecification spec = AlterTableSpecification.alterTable("addamsFamily").alter("lastKnownLocation",
				DataType.list(DataType.ascii()));

		assertThat(toCql(spec)).isEqualTo("ALTER TABLE addamsfamily ALTER lastknownlocation TYPE list<ascii>;");
	}

	/**
	 * @see DATACASS-192
	 */
	@Test
	public void alterTableAddColumn() {

		AlterTableSpecification spec = AlterTableSpecification.alterTable("addamsFamily").add("gravesite",
				DataType.varchar());

		assertThat(toCql(spec)).isEqualTo("ALTER TABLE addamsfamily ADD gravesite varchar;");
	}

	/**
	 * @see DATACASS-192
	 */
	@Test
	public void alterTableAddListColumn() {

		AlterTableSpecification spec = AlterTableSpecification.alterTable("users").add("top_places",
				DataType.list(DataType.ascii()));

		assertThat(toCql(spec)).isEqualTo("ALTER TABLE users ADD top_places list<ascii>;");
	}

	/**
	 * @see DATACASS-192
	 */
	@Test
	public void alterTableDropColumn() {

		AlterTableSpecification spec = AlterTableSpecification.alterTable("addamsFamily").drop("gender");

		assertThat(toCql(spec)).isEqualTo("ALTER TABLE addamsfamily DROP gender;");
	}

	/**
	 * @see DATACASS-192
	 */
	@Test
	public void alterTableRenameColumn() {

		AlterTableSpecification spec = AlterTableSpecification.alterTable("addamsFamily").rename("firstname", "lastname");

		assertThat(toCql(spec)).isEqualTo("ALTER TABLE addamsfamily RENAME firstname TO lastname;");
	}

	/**
	 * @see DATACASS-192
	 */
	@Test
	public void alterTableAddCommentAndTableOption() {

		AlterTableSpecification spec = AlterTableSpecification.alterTable("addamsFamily")
				.with(TableOption.READ_REPAIR_CHANCE, 0.2f).with(TableOption.COMMENT, "A most excellent and useful table");

		assertThat(toCql(spec)).isEqualTo(
				"ALTER TABLE addamsfamily WITH read_repair_chance = 0.2 AND comment = 'A most excellent and useful table';");
	}

	/**
	 * @see DATACASS-192
	 */
	@Test
	public void alterTableAddColumnAndComment() {

		AlterTableSpecification spec = AlterTableSpecification.alterTable("addamsFamily")
				.add("top_places", DataType.list(DataType.ascii())).add("other", DataType.list(DataType.ascii()))
				.with(TableOption.COMMENT, "A most excellent and useful table");

		assertThat(toCql(spec)).isEqualTo(
				"ALTER TABLE addamsfamily ADD top_places list<ascii> ADD other list<ascii> WITH comment = 'A most excellent and useful table';");
	}

	/**
	 * @see DATACASS-192
	 */
	@Test
	public void alterTableAddCaching() {

		Map<Object, Object> cachingMap = new LinkedHashMap<Object, Object>();
		cachingMap.put(CachingOption.KEYS, KeyCachingOption.NONE);
		cachingMap.put(CachingOption.ROWS_PER_PARTITION, "15");

		AlterTableSpecification spec = AlterTableSpecification.alterTable("users").with(TableOption.CACHING, cachingMap);

		assertThat(toCql(spec))
				.isEqualTo("ALTER TABLE users WITH caching = { 'keys' : 'none', 'rows_per_partition' : '15' };");
	}

	private String toCql(AlterTableSpecification spec) {
		return new AlterTableCqlGenerator(spec).toCql();
	}
}
