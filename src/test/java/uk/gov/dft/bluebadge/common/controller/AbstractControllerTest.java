package uk.gov.dft.bluebadge.common.controller;

import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractControllerTest {

  @Test
  public void parseInvalidFormatReason() {

    assertEquals("ABC", AbstractController.parseInvalidFormatReason("AB`some stuff |,.`C"));
  }
}