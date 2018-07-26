package uk.gov.dft.bluebadge.common.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ToEntityFormatterTest {
  @Test
  public void postcodeToEntity() {

    assertEquals("WV164AW", ToEntityFormatter.postcode("wv164aw"));
    assertEquals("WV164AW", ToEntityFormatter.postcode("  wv16 4aw  "));
  }
}
