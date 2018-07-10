package uk.gov.dft.bluebadge.common.converter;

import java.util.ArrayList;
import java.util.List;

public interface ToModelConverter<E, M> {

  M convertToModel(E entity);

  default List<M> convertToModelList(List<E> entityList) {
    List<M> modelList = new ArrayList<>();
    for (E entityItem : entityList) {
      modelList.add(convertToModel(entityItem));
    }
    return modelList;
  }
}
