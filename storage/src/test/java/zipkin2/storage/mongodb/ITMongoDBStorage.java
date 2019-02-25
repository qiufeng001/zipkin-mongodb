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

import java.io.IOException;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import zipkin2.TestObjects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class ITMongoDBStorage {

  /** Written intentionally to allow you to run a single nested method via the CLI. See README */
  static MongoDBStorageRule classRule() {
    return new MongoDBStorageRule("mongo:4.0.6");
  }

  // This test is temporary playground to be removed later once things are implemented
  public static class ITBabySteps {
    @ClassRule public static MongoDBStorageRule mongodb = classRule();

    @Test public void checkWorks() throws IOException {
      mongodb.storage.spanConsumer().accept(TestObjects.TRACE).execute();

      assertThat(mongodb.storage.spanStore().getTrace(TestObjects.TRACE.get(0).traceId()).execute())
          .containsExactlyInAnyOrderElementsOf(TestObjects.TRACE);
    }

    @After public void clear() throws Exception {
      mongodb.clear();
    }
  }
}
