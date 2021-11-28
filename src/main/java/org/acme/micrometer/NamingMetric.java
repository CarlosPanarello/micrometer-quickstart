package org.acme.micrometer;

import io.micrometer.core.instrument.Meter.Type;
import io.micrometer.core.instrument.config.NamingConvention;
import java.util.regex.Pattern;

public class NamingMetric implements NamingConvention {
  private static final Pattern nameChars = Pattern.compile("[^a-zA-Z0-9_:]");
  private static final Pattern tagKeyChars = Pattern.compile("[^a-zA-Z0-9_]");
  public static final String SECONDS = "_seconds";
  private final String timerSuffix;

  public NamingMetric() {
    this("");
  }

  public NamingMetric(String timerSuffix) {
    this.timerSuffix = timerSuffix;
  }

  private boolean isNotMetricName(String name) {
    return !name.startsWith("example_prime_number");
  }

  public String name(String name, Type type, String baseUnit) {
    System.out.println("Param--> " + name + " -Type-> " + type +" - Base-> " + baseUnit);

    String conventionName = NamingConvention.snakeCase.name(name, type, baseUnit);
    switch(type) {
      case COUNTER:
      case DISTRIBUTION_SUMMARY:
      case GAUGE:
        if (baseUnit != null && !conventionName.endsWith("_" + baseUnit) && this.isNotMetricName(conventionName)) {
          conventionName = conventionName + "_" + baseUnit;
        }
    }

    switch(type) {
      case COUNTER:
        if (!conventionName.endsWith("_total") && this.isNotMetricName(conventionName)) {
          conventionName = conventionName + "_total";
        }
      case DISTRIBUTION_SUMMARY:
      case GAUGE:
      default:
        break;
      case TIMER:
      case LONG_TASK_TIMER:
        if (conventionName.endsWith(this.timerSuffix) && this.isNotMetricName(conventionName)) {
          conventionName = conventionName + "_seconds";
        } else if (!conventionName.endsWith("_seconds") && this.isNotMetricName(conventionName)) {
          conventionName = conventionName + this.timerSuffix + "_seconds";
        }
    }

    String sanitized = nameChars.matcher(conventionName).replaceAll("_");
    if (!Character.isLetter(sanitized.charAt(0))) {
      sanitized = "m_" + sanitized;
    }
    System.out.println("Sanitized-->" + sanitized);
    return sanitized;
  }

  public String tagKey(String key) {
    return tagConvert(key);
  }

  public static String tagConvert(String key) {
    String conventionKey = NamingConvention.snakeCase.tagKey(key);
    String sanitized = tagKeyChars.matcher(conventionKey).replaceAll("_");
    if (!Character.isLetter(sanitized.charAt(0))) {
      sanitized = "m_" + sanitized;
    }

    return sanitized;
  }
}

