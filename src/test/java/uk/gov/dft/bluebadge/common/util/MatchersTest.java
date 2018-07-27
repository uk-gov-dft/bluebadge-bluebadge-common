package uk.gov.dft.bluebadge.common.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static uk.gov.dft.bluebadge.common.util.Matchers.collection;
import static uk.gov.dft.bluebadge.common.util.Matchers.enumValues;
import static uk.gov.dft.bluebadge.common.util.MatchersTest.TestEnum.VAL1;
import static uk.gov.dft.bluebadge.common.util.MatchersTest.TestEnum.VAL2;
import static uk.gov.dft.bluebadge.common.util.MatchersTest.TestEnum.VAL3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class MatchersTest {

  enum TestEnum {
    VAL1,
    VAL2,
    VAL3
  }

  @Test
  public void testEnums() {

    assertFalse(enumValues(VAL1, VAL2).contains(null));
    assertTrue(enumValues(VAL1, VAL2).doesNotContain(null));

    assertTrue(enumValues(VAL1, VAL2).contains(VAL2));
    assertFalse(enumValues(VAL1, VAL2).contains(VAL3));

    assertFalse(enumValues(VAL1, VAL2).doesNotContain(VAL1));
    assertTrue(enumValues(VAL1, VAL2).doesNotContain(VAL3));
  }

  @Test
  public void testCollections() {

    assertTrue(collection(null).isNullOrEmpty());
    assertFalse(collection(null).isNotEmpty());

    assertTrue(collection(new ArrayList<String>()).isNullOrEmpty());
    assertFalse(collection(new ArrayList<String>()).isNotEmpty());

    Set<String> strings = new HashSet<>();
    strings.add("a");

    assertFalse(collection(strings).isNullOrEmpty());
    assertTrue(collection(strings).isNotEmpty());
  }
}
