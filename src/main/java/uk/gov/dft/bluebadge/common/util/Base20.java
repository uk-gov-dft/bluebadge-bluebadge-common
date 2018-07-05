package uk.gov.dft.bluebadge.common.util;

/**
 * Provides utlitties to convert between integer and Base20 strings. Note. Misses out the 'I' so the
 * range of characters is 0-9, A,B,C,D,E,F,G,H,J,K
 */
public class Base20 {

  private static final double BASE = 20;
  private static final char[] symbols =
      new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'J', 'K'
      };

  // Prevent class construction
  private Base20() {}

  /**
   * Encode a 0 or positive integer value to a Base20 String.
   *
   * @param number
   * @return a Base20 encoded String.
   * @throws IllegalArgumentException when the integer supplied is not valid.
   */
  public static String encode(int number) {
    if (number < 0) {
      throw new IllegalArgumentException("number must be 0 or a positive integer");
    }

    return convert(number, 0, "");
  }

  private static String convert(int number, int position, String result) {
    if (number < Math.pow(BASE, position + 1d)) {
      return symbols[(number / (int) Math.pow(BASE, position))] + result;
    } else {
      int remainder = (number % (int) Math.pow(BASE, position + 1d));
      return convert(
          number - remainder,
          position + 1,
          symbols[remainder / (int) (Math.pow(BASE, position))] + result);
    }
  }

  /**
   * Decode a Base20 encoded String to its decimal counterpart.
   *
   * @param base20String
   * @return an integer value.
   * @throws IllegalArgumentException thrown when the encoded String supplied is either null or zero
   *     length or contains an invalid character.
   */
  public static int decode(String base20String) {

    if (base20String == null || base20String.trim().length() == 0) {
      throw new IllegalArgumentException("Must provide a base20 encoded string");
    }

    int maxLength = base20String.length();
    int accumulatedValue = 0;

    for (int position = 0; position < maxLength; position++) {
      if (position == maxLength - 1) {
        accumulatedValue += charValue(base20String.toCharArray()[maxLength - 1]);
      } else {
        accumulatedValue +=
            Math.pow(BASE, maxLength - position - 1d) * charValue(base20String.charAt(position));
      }
    }

    return accumulatedValue;
  }

  // Returns the value of the character
  private static int charValue(char encodedChar) {

    for (int i = 0; i < symbols.length; i++) {
      if (symbols[i] == encodedChar) {
        return i;
      }
    }

    throw new IllegalArgumentException(encodedChar + " is not a valid BASE20 character!");
  }
}
