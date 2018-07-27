package uk.gov.dft.bluebadge.common.util;

import java.util.Collection;

public class Matchers {

  private Matchers() {}

  public static class CollectionMatchers {
    private Collection<?> collection;

    private CollectionMatchers(Collection<?> collection) {
      this.collection = collection;
    }

    public boolean isNullOrEmpty() {
      return null == collection || collection.isEmpty();
    }

    public boolean isNotEmpty() {
      return !isNullOrEmpty();
    }
  }

  public static class EnumListMatchers {

    private Enum<?>[] list;

    private EnumListMatchers(Enum<?>... list) {
      this.list = list;
    }

    public boolean contains(Enum<?> value) {
      for (Enum<?> item : list) {
        if (item.equals(value)) {
          return true;
        }
      }
      return false;
    }

    public boolean doesNotContain(Enum<?> value) {
      return !contains(value);
    }
  }

  @SuppressWarnings("WeakerAccess")
  public static EnumListMatchers enumValues(Enum<?>... list) {
    return new EnumListMatchers(list);
  }

  @SuppressWarnings("WeakerAccess")
  public static CollectionMatchers collection(Collection<?> collection) {
    return new CollectionMatchers(collection);
  }
}
