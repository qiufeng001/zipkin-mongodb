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

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import java.util.List;
import zipkin2.CheckResult;
import zipkin2.storage.SpanConsumer;
import zipkin2.storage.SpanStore;
import zipkin2.storage.StorageComponent;

public final class MongoDBStorage extends StorageComponent {

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder extends StorageComponent.Builder {
    ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/zipkin");

    @Override public Builder strictTraceId(boolean strictTraceId) {
      if (!strictTraceId) throw new IllegalArgumentException("unstrict trace ID not supported");
      return this;
    }

    @Override public Builder searchEnabled(boolean searchEnabled) {
      if (searchEnabled) throw new IllegalArgumentException("search not supported");
      return this;
    }

    @Override public Builder autocompleteKeys(List<String> keys) {
      if (keys == null) throw new NullPointerException("keys == null");
      if (!keys.isEmpty()) throw new IllegalArgumentException("autocomplete not supported");
      return this;
    }

    public Builder connectionString(String connectionString) {
      if (connectionString == null) throw new NullPointerException("connectionString == null");
      ConnectionString parsed = new ConnectionString(connectionString);
      if (parsed.getDatabase() == null) throw new IllegalArgumentException("database == null");
      this.connectionString = parsed;
      return this;
    }

    @Override public MongoDBStorage build() {
      return new MongoDBStorage(this);
    }

    Builder() {
    }
  }

  final ConnectionString connectionString;

  MongoDBStorage(Builder builder) {
    connectionString = builder.connectionString;
  }

  /** client and close are typically called from different threads */
  private volatile MongoClient client; // using sync for now as it is simpler
  private volatile boolean closeCalled;

  @Override public SpanStore spanStore() {
    return new MongoDBSpanStore(this);
  }

  @Override public SpanConsumer spanConsumer() {
    return new MongoDBSpanConsumer(this);
  }

  @Override public CheckResult check() {
    try (MongoCursor<String> cursor = client().listDatabaseNames().iterator()) {
      return CheckResult.OK;
    } catch (Exception e) {
      return CheckResult.failed(e);
    }
  }

  MongoClient client() {
    if (client == null) {
      synchronized (this) {
        if (client == null) {
          client = MongoClients.create(connectionString);
        }
      }
    }
    return client;
  }

  @Override public void close() {
    if (closeCalled) return;
    // blocking to prevent access while initializing
    synchronized (this) {
      if (!closeCalled) {
        if (closeCalled) return;
        MongoClient client = this.client;
        if (client != null) client.close();
        closeCalled = true;
      }
    }
  }

  @Override public final String toString() {
    return "MongoDBStorage{"
        + "hosts=" + connectionString.getHosts()
        + ", database=" + connectionString.getDatabase()
        + "}";
  }
}
