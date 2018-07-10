package uk.gov.dft.bluebadge.common.converter;

public interface ToEntityConverter<E, M> {
  E convertToEntity(M model);
}
