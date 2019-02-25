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
package zipkin2.autoconfigure.storage.mongodb;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import zipkin2.storage.StorageComponent;
import zipkin2.storage.mongodb.MongoDBStorage;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(ZipkinMongoDBStorageProperties.class)
@ConditionalOnProperty(name = "zipkin.storage.type", havingValue = "mongodb")
@ConditionalOnMissingBean(StorageComponent.class)
    // This component is named .*MongoDB.* even though the package already says mongodb because
    // Spring Boot configuration endpoints only printout the simple name of the class
class ZipkinMongoDBStorageAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  StorageComponent storage(ZipkinMongoDBStorageProperties properties) {
    return properties.toBuilder().build();
  }
}
