package uk.gov.dft.bluebadge.common.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AbstractControllerTest {

  @Test
  public void parseInvalidFormatReason() {

    assertEquals("ABC", AbstractController.parseInvalidFormatReason("AB`some stuff |,.`C"));
  }
}
