/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.testkit;

import java.util.List;

/**
 * @author Alex Ruiz
 */
public final class ObjectArrays {
  private static final Object[] EMPTY = {};

  public static Object[] arrayOf(Object... values) {
    return values;
  }

  public static String[] arrayOf(List<String> list) {
    return list.toArray(new String[0]);
  }

  public static Object[] emptyArray() {
    return EMPTY;
  }

  private ObjectArrays() {}
}
