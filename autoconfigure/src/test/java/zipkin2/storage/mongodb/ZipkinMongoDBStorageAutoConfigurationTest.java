/*
 * Copyright 2019 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use that file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package zipkin2.storage.mongodb;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import zipkin2.autoconfigure.storage.mongodb.Access;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipkinMongoDBStorageAutoConfigurationTest {

  @Rule public ExpectedException thrown = ExpectedException.none();

  AnnotationConfigApplicationContext context;

  @After public void close() {
    if (context != null) {
      context.close();
    }
  }

  @Test public void doesntProvidesStorageComponent_whenStorageTypeNotMongoDB() {
    context = new AnnotationConfigApplicationContext();
    TestPropertyValues.of("zipkin.storage.type:elasticsearch").applyTo(context);
    Access.registerMongoDB(context);
    context.refresh();

    thrown.expect(NoSuchBeanDefinitionException.class);
    context.getBean(MongoDBStorage.class);
  }

  @Test public void providesStorageComponent_whenStorageTypeMongoDB() {
    context = new AnnotationConfigApplicationContext();
    TestPropertyValues.of("zipkin.storage.type:mongodb").applyTo(context);
    Access.registerMongoDB(context);
    context.refresh();

    assertThat(context.getBean(MongoDBStorage.class)).isNotNull();
  }

  @Test public void canOverridesProperty_connectionString() {
    context = new AnnotationConfigApplicationContext();
    TestPropertyValues.of(
        "zipkin.storage.type:mongodb",
        "zipkin.storage.mongodb.connection-string:mongodb://host1:27017/zipkin"
    ).applyTo(context);
    Access.registerMongoDB(context);
    context.refresh();

    assertThat(context.getBean(MongoDBStorage.class).connectionString.getConnectionString())
        .isEqualTo("mongodb://host1:27017/zipkin");
  }
}
