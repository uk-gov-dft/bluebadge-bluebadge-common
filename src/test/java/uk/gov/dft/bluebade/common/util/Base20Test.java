package uk.gov.dft.bluebade.common.util;

import org.junit.Assert;
import org.junit.Test;

public class Base20Test {

  private static final String BASE20_ENCODED = "31E";
  public static final int DECODED_INT = 1234;

  @Test(expected = IllegalArgumentException.class)
  public void encodeNegative() {
    Base20.encode(-1);
  }

  @Test
  public void encodeZero() {
    String encode = Base20.encode(0);
    Assert.assertEquals("0", encode);
  }

  @Test
  public void encodePositive() {
    Assert.assertEquals(BASE20_ENCODED, Base20.encode(1234));
  }

  @Test
  public void decodeZero() {
    Assert.assertEquals(0, Base20.decode("0"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void decodeNull() {
    Base20.decode(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void decodeInvalidCharacters() {
    Base20.decode("ABCZ9");
  }

  @Test
  public void decodeValidBase20EncodedString() {
    Assert.assertEquals(DECODED_INT, Base20.decode(BASE20_ENCODED));
  }
}
