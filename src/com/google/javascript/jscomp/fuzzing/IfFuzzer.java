/*
 * Copyright 2013 The Closure Compiler Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.javascript.jscomp.fuzzing;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

/**
 * UNDER DEVELOPMENT. DO NOT USE!
 */
class IfFuzzer extends AbstractFuzzer {

  public IfFuzzer(Random random, ScopeManager scopeManager, JSONObject config,
      StringNumberGenerator snGenerator) {
    super(random, scopeManager, config, snGenerator);
  }

  /* (non-Javadoc)
   * @see com.google.javascript.jscomp.fuzzing.AbstractFuzzer#isEnough(int)
   */
  @Override
  protected boolean isEnough(int budget) {
    return budget >= 3;
  }

  /* (non-Javadoc)
   * @see com.google.javascript.jscomp.fuzzing.AbstractFuzzer#generate(int)
   */
  @Override
  protected Node generate(int budget) {
    int numComponents;
    if (budget <= 3) {
      numComponents = 2;
    } else {
      numComponents =
          random.nextDouble() < getOwnConfig().optDouble("hasElse") ? 3 : 2;
    }
    AbstractFuzzer[] fuzzers = new AbstractFuzzer[numComponents];
    BlockFuzzer blockFuzzer =
        new BlockFuzzer(random, scopeManager, config, snGenerator);
    Arrays.fill(fuzzers, blockFuzzer);
    fuzzers[0] =
        new ExpressionFuzzer(random, scopeManager, config, snGenerator);
    Node[] components = distribute(budget - 1, fuzzers);
    return new Node(Token.IF, components);
  }

  /* (non-Javadoc)
   * @see com.google.javascript.jscomp.fuzzing.AbstractFuzzer#getConfigName()
   */
  @Override
  protected String getConfigName() {
    return "if";
  }

}