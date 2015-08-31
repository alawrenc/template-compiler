/**
 * Copyright (c) 2014 SQUARESPACE, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.squarespace.template;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * Default base class for Formatters.
 */
public abstract class BaseFormatter extends Plugin implements Formatter {

  /**
   * Constructs a formatter with the given identifier and indicates whether
   * it requires arguments.
   */
  public BaseFormatter(String identifier, boolean requiresArgs) {
    super(identifier, requiresArgs);
  }

  /**
   * Applies the Formatter to the context, using the given arguments which have
   * been validated and optionally converted by validateArgs(). Formatters append
   * output to the Context.
   */
  public JsonNode apply(Context ctx, Arguments args, JsonNode node) throws CodeExecuteException {
    return node;
  }

}
