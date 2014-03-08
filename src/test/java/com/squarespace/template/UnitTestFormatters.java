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


/**
 * Implementations to test the Formatter interface.
 */
public class UnitTestFormatters extends BaseRegistry<Formatter> {

  public static final Formatter EXECUTE_ERROR = new BaseFormatter("execute-error", false) {
    @Override
    public void apply(Context ctx, Arguments args) throws CodeExecuteException {
      throw new CodeExecuteException(ctx.error(ExecuteErrorType.GENERAL_ERROR).name("ABCXYZ"));
    }
  };

  public static final Formatter INVALID_ARGS = new BaseFormatter("invalid-args", false) {
    @Override
    public void validateArgs(Arguments args) throws ArgumentsException {
      throw new ArgumentsException("Invalid arguments");
    }
  };

  public static final Formatter NPE = new BaseFormatter("npe", false) {

    @Override
    public void apply(Context ctx, Arguments args) throws CodeExecuteException {
      throw new NullPointerException("fake NPE thrown by the test npe formatter.");
    }
  };

  public static final Formatter REQUIRED_ARGS = new BaseFormatter("required-args", true) {
  };

  public static final Formatter UNSTABLE = new BaseFormatter("unstable", false) {
    @Override
    public void apply(Context ctx, Arguments args) throws CodeExecuteException {
      throw new IllegalArgumentException("unexpected error!");
    }
  };

}
