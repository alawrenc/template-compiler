/**
 * Copyright (c) 2020 SQUARESPACE, Inc.
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

import static com.squarespace.cldrengine.utils.StringUtils.isEmpty;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.squarespace.cldrengine.api.ContextType;
import com.squarespace.cldrengine.api.CurrencyFormatOptions;
import com.squarespace.cldrengine.api.CurrencyFormatStyleType;
import com.squarespace.cldrengine.api.CurrencySymbolWidthType;
import com.squarespace.cldrengine.api.DateFormatOptions;
import com.squarespace.cldrengine.api.DateIntervalFormatOptions;
import com.squarespace.cldrengine.api.DecimalFormatOptions;
import com.squarespace.cldrengine.api.DecimalFormatStyleType;
import com.squarespace.cldrengine.api.FormatWidthType;
import com.squarespace.cldrengine.api.NumberFormatOptions;
import com.squarespace.cldrengine.api.RoundingModeType;

public class OptionParsers {

  // Arbitrary value just to have an upper limit.
  private static final int CLAMP_MAX = 50;

  public static DecimalFormatOptions decimal(Arguments args) {
    return decimal(args.getArgs());
  }

  public static DecimalFormatOptions decimal(List<String> args) {
    DecimalFormatOptions options = new DecimalFormatOptions();
    if (args == null) {
      return options;
    }
    int count = args.size();
    for (int i = 0; i < count; i++) {
      String arg = args.get(i);
      String value = "";
      int index = arg.indexOf(':');
      if (index != -1) {
        value = arg.substring(index + 1);
        arg = arg.substring(0, index);
      }

      if (arg.equals("style")) {
        options.style(DecimalFormatStyleType.fromString(value));
        if (!options.style.ok() && "standard".equals(value)) {
          options.style(DecimalFormatStyleType.DECIMAL);
        }
      } else {
        numberOption(arg, value, options);
      }
    }
    return options;
  }

  public static CurrencyFormatOptions currency(Arguments args) {
    return currency(args.getArgs());
  }

  public static CurrencyFormatOptions currency(List<String> args) {
    CurrencyFormatOptions options = new CurrencyFormatOptions();
    if (args == null) {
      return options;
    }
    int count = args.size();
    for (int i = 0; i < count; i++) {
      String arg = args.get(i);
      currencyOption(arg, options);
    }
    return options;
  }

  public static void currencyOption(String arg, CurrencyFormatOptions opts) {
    String value = "";
    int index = arg.indexOf(':');
    if (index != -1) {
      value = arg.substring(index + 1);
      arg = arg.substring(0, index);
    }

    if (arg.equals("style")) {
      opts.style(CurrencyFormatStyleType.fromString(value));
      if (!opts.style.ok() && "standard".equals(value)) {
        opts.style(CurrencyFormatStyleType.SYMBOL);
      }

    } else if (arg.equals("symbol")) {
      opts.symbolWidth(CurrencySymbolWidthType.fromString(value));

    } else {
      numberOption(arg, value, opts);
    }

  }

  /**
   * Interprets common options for DecimalFormatter and CurrencyFormatter.
   */
  public static void numberOption(String arg, String value, NumberFormatOptions opts) {
    switch (arg) {
      case "group":
      case "grouping":
        opts.group(isEmpty(value) || value.equals("true"));
        break;

      case "no-group":
      case "no-grouping":
        opts.group(false);
        break;

      case "round":
      case "rounding":
        RoundingModeType mode = null;
        switch (value) {
          case "ceil":
            mode = RoundingModeType.CEILING;
            break;
          case "truncate":
            mode = RoundingModeType.DOWN;
            break;
          default:
            mode = RoundingModeType.fromString(value);
            break;
        }
        opts.round(mode);
        break;

      case "minint":
      case "minInt":
      case "minimumIntegerDigits":
        opts.minimumIntegerDigits(clamp(toInt(value), 0, CLAMP_MAX));
        break;

      case "maxfrac":
      case "maxFrac":
      case "maximumFractionDigits":
        opts.maximumFractionDigits(clamp(toInt(value), 0, CLAMP_MAX));
        break;

      case "minfrac":
      case "minFrac":
      case "minimumFractionDigits":
        opts.minimumFractionDigits(clamp(toInt(value), 0, CLAMP_MAX));
        break;

      case "maxsig":
      case "maxSig":
      case "maximumSignificantDigits":
        opts.maximumSignificantDigits(clamp(toInt(value), 0, CLAMP_MAX));
        break;

      case "minsig":
      case "minSig":
      case "minimumSignificantDigits":
        opts.minimumSignificantDigits(clamp(toInt(value), 0, CLAMP_MAX));
        break;

      default:
        break;
    }
  }

  public static DateFormatOptions datetime(Arguments args) {
    return datetime(args.getArgs());
  }

  public static DateFormatOptions datetime(List<String> args) {
    DateFormatOptions options = DateFormatOptions.build();
    if (args == null) {
      return options;
    }
    for (String arg : args) {
      int i = arg.indexOf(':');
      if (i == -1) {
        switch (arg) {
          case "date":
            options.date(FormatWidthType.SHORT);
            break;

          case "time":
            options.time(FormatWidthType.SHORT);
            break;

          default:
            FormatWidthType format = FormatWidthType.fromString(arg);
            if (format != null) {
              options.datetime(format);
            } else {
              // add skeleton fields
              String skel = options.skeleton.get();
              options.skeleton(skel == null ? arg : skel + arg);
            }
            break;
        }
        continue;
      }

      String key = arg.substring(0, i);
      String val = arg.substring(i + 1);

      switch (key) {
        case "context":
          options.context(ContextType.fromString(val));
          break;

        case "date":
        case "time":
        case "datetime": {
          FormatWidthType format = FormatWidthType.fromString(val);
          if (format != null) {
            if (key.equals("date")) {
              options.date(format);
            } else if (key.equals("time")) {
              options.time(format);
            } else {
              options.datetime(format);
            }
          } else {
            // add skeleton fields
            String skel = options.skeleton.get();
            options.skeleton(skel == null ? val : skel + val);
          }
          break;
        }

        case "skeleton":
          options.skeleton(val);
          break;

        case "wrap":
        case "wrapper":
        case "wrapped": {
          options.wrap(FormatWidthType.fromString(val));
          break;
        }

        default:
          break;
      }
    }
    return options;
  }

  public static DateIntervalFormatOptions datetimeInterval(Arguments args) {
    return interval(args.getArgs());
  }

  public static DateIntervalFormatOptions interval(List<String> args) {
    DateIntervalFormatOptions options = DateIntervalFormatOptions.build();
    if (args == null) {
      return options;
    }
    for (String arg : args) {
      int i = arg.indexOf(':');
      if (i == -1) {
        switch (arg) {
          case "context":
          case "skeleton":
          case "date":
          case "time":
            break;
          default:
            if (!StringUtils.isEmpty(arg)) {
              options.skeleton(arg);
            }
            break;
        }
        continue;
      }
      String key = arg.substring(0, i);
      String val = arg.substring(i + 1);
      switch (key) {
        case "context":
          options.context(ContextType.fromString(val));
          break;
        case "skeleton":
          options.skeleton(val);
          break;
        case "date":
          options.date(val);
          break;
        case "time":
          options.time(val);
          break;
        default:
          break;
      }
    }
    return options;
  }

  private static int toInt(String v) {
    return (int) GeneralUtils.toLong(v, 0, v.length());
  }

  private static int clamp(int value, int min, int max) {
    return value < min ? min : (value > max ? max : value);
  }

}
