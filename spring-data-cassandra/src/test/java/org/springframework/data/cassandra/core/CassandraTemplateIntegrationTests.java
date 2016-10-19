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

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cassandra.core.CqlTemplateNG;
import org.springframework.cassandra.test.integration.AbstractKeyspaceCreatingIntegrationTest;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.domain.Person;
import org.springframework.data.cassandra.test.integration.support.SchemaTestUtils;

/**
 * Integration tests for {@link CassandraTemplateNG}.
 * 
 * @author Mark Paluch
 */
public class CassandraTemplateIntegrationTests extends AbstractKeyspaceCreatingIntegrationTest {

	private CassandraTemplateNG template;

	@Before
	public void setUp() {

		MappingCassandraConverter converter = new MappingCassandraConverter();
		template = new CassandraTemplateNG(new CqlTemplateNG(session), converter);

		// TODO: Cleanup
		SchemaTestUtils.potentiallyCreateTableFor(Person.class, new CassandraTemplate(session));
		SchemaTestUtils.truncate(Person.class, new CassandraTemplate(session));
	}

	/**
	 * @see DATACASS-292
	 */
	@Test
	public void insertShouldInsertEntity() {

		Person person = new Person("heisenberg", "Walter", "White");

		assertThat(template.selectOneById(person.getId(), Person.class)).isNull();

		Person inserted = template.insert(person);

		assertThat(inserted).isNotNull().isEqualTo(person);
		assertThat(template.selectOneById(person.getId(), Person.class)).isEqualTo(person);
	}

	/**
	 * @see DATACASS-292
	 */
	@Test
	public void shouldInsertAndCountEntities() {

		Person person = new Person("heisenberg", "Walter", "White");

		template.insert(person);

		long count = template.count(Person.class);
		assertThat(count).isEqualTo(1L);
	}

	/**
	 * @see DATACASS-292
	 */
	@Test
	public void updateShouldUpdateEntity() {

		Person person = new Person("heisenberg", "Walter", "White");
		template.insert(person);

		person.setFirstname("Walter Hartwell");
		Person updated = template.update(person);
		assertThat(updated).isNotNull();

		assertThat(template.selectOneById(person.getId(), Person.class)).isEqualTo(person);
	}

	/**
	 * @see DATACASS-292
	 */
	@Test
	public void deleteShouldRemoveEntity() {

		Person person = new Person("heisenberg", "Walter", "White");
		template.insert(person);

		Person deleted = template.delete(person);
		assertThat(deleted).isNotNull();

		assertThat(template.selectOneById(person.getId(), Person.class)).isNull();
	}

	/**
	 * @see DATACASS-292
	 */
	@Test
	public void deleteByIdShouldRemoveEntity() {

		Person person = new Person("heisenberg", "Walter", "White");
		template.insert(person);

		Boolean deleted = template.deleteById(person.getId(), Person.class);
		assertThat(deleted).isTrue();

		assertThat(template.selectOneById(person.getId(), Person.class)).isNull();
	}

	/**
	 * @see DATACASS-182
	 */
	@Test
	public void stream() {

		Person person = new Person("heisenberg", "Walter", "White");
		template.insert(person);

		Stream<Person> stream = template.stream("SELECT * FROM person", Person.class);

		assertThat(stream.collect(Collectors.toList())).hasSize(1).contains(person);
	}
}
