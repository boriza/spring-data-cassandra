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

/**
 * An interface used by {@link AsyncCassandraTemplate} for processing entities of a converted
 * {@link com.datastax.driver.core.ResultSet} on a per-entity basis. Implementations of this interface perform the
 * actual work of processing each entity.
 *
 * @author Mark Paluch
 * @param <T>
 * @since 2.0 TODO: ObjectCallback or Consumer<T>?
 */
public interface ObjectCallback<T> {

	/**
	 * Implementations must implement this method to process each entity in the
	 * {@link com.datastax.driver.core.ResultSet}.
	 * <p>
	 * Exactly what the implementation chooses to do is up to it: A trivial implementation might simply count entities,
	 * while another implementation might build an XML document.
	 * 
	 * @param result the result to process.
	 */
	void onObject(T result);
}
