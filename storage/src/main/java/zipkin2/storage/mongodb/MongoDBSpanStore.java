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

import com.mongodb.client.MongoClient;
import java.util.List;
import zipkin2.Call;
import zipkin2.DependencyLink;
import zipkin2.Span;
import zipkin2.storage.QueryRequest;
import zipkin2.storage.SpanStore;

final class MongoDBSpanStore implements SpanStore {

  final MongoClient client;
  final boolean searchEnabled = true;

  MongoDBSpanStore(MongoDBStorage storage) {
    client = storage.client();
  }

  @Override public Call<List<List<Span>>> getTraces(QueryRequest request) {
    if (!searchEnabled) return Call.emptyList();

    throw new UnsupportedOperationException();
  }

  @Override public Call<List<Span>> getTrace(String hexTraceId) {
    // make sure we have a 16 or 32 character trace ID
    String normalized = Span.normalizeTraceId(hexTraceId);

    throw new UnsupportedOperationException();
  }

  @Override public Call<List<String>> getServiceNames() {
    if (!searchEnabled) return Call.emptyList();

    throw new UnsupportedOperationException();
  }

  @Override public Call<List<String>> getSpanNames(String serviceName) {
    if (!searchEnabled) return Call.emptyList();

    throw new UnsupportedOperationException();
  }

  @Override public Call<List<DependencyLink>> getDependencies(long endTs, long lookback) {
    if (!searchEnabled) return Call.emptyList();

    throw new UnsupportedOperationException();
  }
}
