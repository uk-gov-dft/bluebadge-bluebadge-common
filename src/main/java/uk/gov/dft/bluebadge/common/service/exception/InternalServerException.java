package uk.gov.dft.bluebadge.common.service.exception;

import org.springframework.http.HttpStatus;
import uk.gov.dft.bluebadge.common.api.model.Error;

@SuppressWarnings("WeakerAccess")
public class InternalServerException extends ServiceException {

  public InternalServerException(Error error) {
    super(HttpStatus.INTERNAL_SERVER_ERROR);
    commonResponse.setError(error);
  }
}

