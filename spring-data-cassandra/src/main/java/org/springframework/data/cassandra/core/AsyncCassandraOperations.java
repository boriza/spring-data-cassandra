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

import java.util.List;

import org.springframework.cassandra.core.QueryOptions;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.util.concurrent.ListenableFuture;

import com.datastax.driver.core.Statement;

import reactor.core.publisher.Mono;

/**
 * Interface specifying a basic set of Cassandra operations. Implemented by {@link AsyncCassandraTemplate}. Not often
 * used directly, but a useful option to enhance testability, as it can easily be mocked or stubbed.
 *
 * @author Mark Paluch
 * @since 2.0
 * @see AsyncCassandraTemplate
 */
public interface AsyncCassandraOperations {

	// -------------------------------------------------------------------------
	// Methods dealing with static CQL
	// -------------------------------------------------------------------------

	/**
	 * Execute a {@code SELECT} query and convert the resulting items to a {@link List} of entities.
	 *
	 * @param cql must not be {@literal null}.
	 * @param entityType The entity type must not be {@literal null}.
	 * @return the converted results
	 */
	<T> ListenableFuture<List<T>> select(String cql, Class<T> entityType);

	/**
	 * Execute a {@code SELECT} query and convert the resulting items notifying {@link ObjectCallback} for each entity.
	 *
	 * @param cql must not be {@literal null}.
	 * @param objectCallback object that will extract results, one object at a time, must not be {@literal null}.
	 * @param entityType The entity type must not be {@literal null}.
	 * @return the completion handle
	 */
	<T> ListenableFuture<Void> select(String cql, ObjectCallback<T> objectCallback, Class<T> entityType);

	/**
	 * Execute a {@code SELECT} query and convert the resulting item to an entity.
	 *
	 * @param cql must not be {@literal null}.
	 * @param entityType The entity type must not be {@literal null}.
	 * @return the converted object or {@literal null}.
	 */
	<T> ListenableFuture<T> selectOne(String cql, Class<T> entityType);

	// -------------------------------------------------------------------------
	// Methods dealing with com.datastax.driver.core.Statement
	// -------------------------------------------------------------------------

	/**
	 * Execute a {@code SELECT} query and convert the resulting items to a stream of entities.
	 *
	 * @param select must not be {@literal null}.
	 * @param entityType The entity type must not be {@literal null}.
	 * @return the converted results
	 */
	<T> ListenableFuture<List<T>> select(Statement select, Class<T> entityType);

	/**
	 * Execute a {@code SELECT} query and convert the resulting items notifying {@link ObjectCallback} for each entity.
	 *
	 * @param select must not be {@literal null}.
	 * @param objectCallback object that will extract results, one object at a time, must not be {@literal null}.
	 * @param entityType The entity type must not be {@literal null}.
	 * @return the completion handle
	 */
	<T> ListenableFuture<Void> select(Statement select, ObjectCallback<T> objectCallback, Class<T> entityType);

	/**
	 * Execute a {@code SELECT} query and convert the resulting item to an entity.
	 *
	 * @param select must not be {@literal null}.
	 * @param entityType The entity type must not be {@literal null}.
	 * @return the converted object or {@literal null}.
	 */
	<T> ListenableFuture<T> selectOne(Statement select, Class<T> entityType);

	// -------------------------------------------------------------------------
	// Methods dealing with entities
	// -------------------------------------------------------------------------

	/**
	 * Execute the Select by {@code id} for the given {@code entityType}.
	 *
	 * @param id must not be {@literal null}.
	 * @param entityType The entity type must not be {@literal null}.
	 * @return the converted object or {@literal null}.
	 */
	<T> ListenableFuture<T> selectOneById(Object id, Class<T> entityType);

	/**
	 * Determine whether the row {@code entityType} with the given {@code id} exists.
	 *
	 * @param id must not be {@literal null}.
	 * @param entityType must not be {@literal null}.
	 * @return true, if the object exists
	 */
	ListenableFuture<Boolean> exists(Object id, Class<?> entityType);

	/**
	 * Returns the number of rows for the given entity class.
	 *
	 * @param entityClass must not be {@literal null}.
	 * @return
	 */
	ListenableFuture<Long> count(Class<?> entityClass);

	/**
	 * Insert the given entity and emit the entity if the insert was applied.
	 *
	 * @param entity The entity to insert, must not be {@literal null}.
	 * @return The entity given
	 */
	<T> ListenableFuture<T> insert(T entity);

	/**
	 * Insert the given entity applying {@link WriteOptions} and emit the entity if the insert was applied.
	 *
	 * @param entity The entity to insert, must not be {@literal null}.
	 * @param options may be {@literal null}.
	 * @return The entity given
	 */
	<T> ListenableFuture<T> insert(T entity, WriteOptions options);

	/**
	 * Insert the given entities and emit the entity if the insert was applied.
	 *
	 * @param entities The entities to insert, must not be {@literal null}.
	 * @return The entities given.
	 */
	<T> ListenableFuture<T> insert(Iterable<? extends T> entities);

	/**
	 * Insert the given entities applying {@link WriteOptions} and emit the entity if the insert was applied.
	 *
	 * @param entities The entities to insert, must not be {@literal null}.
	 * @param options may be {@literal null}.
	 * @return The entities given.
	 */
	<T> ListenableFuture<T> insert(Iterable<? extends T> entities, WriteOptions options);

	/**
	 * Update the given entity and emit the entity if the update was applied.
	 *
	 * @param entity The entity to update, must not be {@literal null}.
	 * @return The entity given
	 */
	<T> Mono<T> update(T entity);

	/**
	 * Update the given entity applying {@link WriteOptions} and emit the entity if the update was applied.
	 *
	 * @param entity The entity to update, must not be {@literal null}.
	 * @param options may be {@literal null}.
	 * @return The entity given
	 */
	<T> Mono<T> update(T entity, WriteOptions options);

	/**
	 * Update the given entities and emit the entity if the update was applied.
	 *
	 * @param entities The entities to update, must not be {@literal null}.
	 * @return The entities given.
	 */
	<T> ListenableFuture<T> update(Iterable<? extends T> entities);

	/**
	 * Update the given entities applying {@link WriteOptions} and emit the entity if the update was applied.
	 *
	 * @param entities The entities to update, must not be {@literal null}.
	 * @param options may be {@literal null}.
	 * @return The entities given.
	 */
	<T> ListenableFuture<T> update(Iterable<? extends T> entities, WriteOptions options);

	/**
	 * Remove the given object from the table by id.
	 *
	 * @param id must not be {@literal null}.
	 * @param entityType The entity type must not be {@literal null}.
	 */
	Mono<Boolean> deleteById(Object id, Class<?> entityType);

	/**
	 * Delete the given entity and emit the entity if the delete was applied.
	 *
	 * @param entity must not be {@literal null}.
	 */
	<T> Mono<T> delete(T entity);

	/**
	 * Delete the given entity applying {@link QueryOptions} and emit the entity if the delete was applied.
	 *
	 * @param entity must not be {@literal null}.
	 * @param options may be {@literal null}.
	 */
	<T> Mono<T> delete(T entity, QueryOptions options);

	/**
	 * Delete the given entities and emit the entity if the delete was applied.
	 *
	 * @param entities must not be {@literal null}.
	 */
	<T> ListenableFuture<T> delete(Iterable<? extends T> entities);

	/**
	 * Delete the given entities applying {@link QueryOptions} and emit the entity if the delete was applied.
	 *
	 * @param entities must not be {@literal null}.
	 * @param options may be {@literal null}.
	 */
	<T> ListenableFuture<T> delete(Iterable<? extends T> entities, QueryOptions options);

	/**
	 * Execute a {@code TRUNCATE} query to remove all entities of a given class.
	 * 
	 * @param entityType The entity type must not be {@literal null}.
	 */
	ListenableFuture<Void> truncate(Class<?> entityType);

	/**
	 * Returns the underlying {@link CassandraConverter}.
	 *
	 * @return the converter.
	 */
	CassandraConverter getConverter();

}
