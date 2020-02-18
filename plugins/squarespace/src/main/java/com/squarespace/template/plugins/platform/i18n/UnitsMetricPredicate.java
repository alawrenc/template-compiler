/**
 * Copyright (c) 2017 SQUARESPACE, Inc.
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

package com.squarespace.template.plugins.platform.i18n;

import com.squarespace.template.Arguments;
import com.squarespace.template.ArgumentsException;
import com.squarespace.template.BasePredicate;
import com.squarespace.template.CodeExecuteException;
import com.squarespace.template.Context;


/**
 * Predicate that returns true if the given category uses the metric system
 * for the current locale. Defaults to true when locale is not determined.
 */
public class UnitsMetricPredicate extends BasePredicate {

  // DEPRECATED
  public UnitsMetricPredicate() {
    super("units-metric?", false);
  }

  @Override
  public void validateArgs(Arguments args) throws ArgumentsException {
  }

  @Override
  public boolean apply(Context ctx, Arguments arguments) throws CodeExecuteException {
    return true;
  }

}
