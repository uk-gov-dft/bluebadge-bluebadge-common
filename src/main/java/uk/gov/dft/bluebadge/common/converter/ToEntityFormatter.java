package uk.gov.dft.bluebadge.common.converter;

import org.apache.commons.lang3.StringUtils;

public class ToEntityFormatter {

  private ToEntityFormatter() {}

  public static String postcode(String modelValue) {
    if (null == modelValue) return null;

    return StringUtils.removeAll(modelValue.toUpperCase(), " ");
  }
}
