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

import java.io.Serializable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import zipkin2.storage.mongodb.MongoDBStorage;

import static zipkin2.storage.mongodb.MongoDBStorage.Builder;

@ConfigurationProperties("zipkin.storage.mongodb")
class ZipkinMongoDBStorageProperties implements Serializable { // for Spark jobs
  private static final long serialVersionUID = 0L;

  private String connectionString;
  private String database;

  public String getConnectionString() {
    return connectionString;
  }

  public void setConnectionString(String connectionString) {
    this.connectionString = connectionString;
  }

  public Builder toBuilder() {
    Builder result = MongoDBStorage.newBuilder();
    if (connectionString != null) result.connectionString(connectionString);
    return result;
  }
}
