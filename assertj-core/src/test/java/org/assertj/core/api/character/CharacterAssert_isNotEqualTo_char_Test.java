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
package org.assertj.core.api.character;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.CharacterAssert;
import org.assertj.core.api.CharacterAssertBaseTest;

/**
 * Tests for <code>{@link CharacterAssert#isNotEqualTo(char)}</code>.
 * 
 * @author Alex Ruiz
 */
class CharacterAssert_isNotEqualTo_char_Test extends CharacterAssertBaseTest {

  @Override
  protected CharacterAssert invoke_api_method() {
    return assertions.isNotEqualTo('b');
  }

  @Override
  protected void verify_internal_effects() {
    verify(characters).assertNotEqual(getInfo(assertions), getActual(assertions), 'b');
  }
}
