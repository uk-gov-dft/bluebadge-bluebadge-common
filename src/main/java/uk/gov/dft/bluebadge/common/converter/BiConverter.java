package uk.gov.dft.bluebadge.common.converter;


import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;


/**
 * Converts to and from API model and DB Entity Model.
 *
 * @param <E> DB Entity bean
 * @param <M> API Model bean
 */
@SuppressWarnings("squid:RedundantThrowsDeclarationCheck")
interface BiConverter<E, M> {
  E convertToEntity(M model) throws BadRequestException;

  M convertToModel(E entity);

  default List<M> convertToModelList(List<E> entityList) {
    List<M> modelList = new ArrayList<>();
    for (E entityItem : entityList) {
      modelList.add(convertToModel(entityItem));
    }
    return modelList;
  }
}
