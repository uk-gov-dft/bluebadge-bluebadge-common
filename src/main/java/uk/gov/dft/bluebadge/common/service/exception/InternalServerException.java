package uk.gov.dft.bluebadge.common.service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import uk.gov.dft.bluebadge.common.api.model.Error;

@Slf4j
@SuppressWarnings("WeakerAccess")
public class InternalServerException extends ServiceException {

  public InternalServerException(Error error) {
    super(HttpStatus.INTERNAL_SERVER_ERROR);
    commonResponse.setError(error);
  }

  public InternalServerException(Exception e) {
    super(HttpStatus.INTERNAL_SERVER_ERROR);
    log.error("Internal error, returning 500.", e);
    Error error = new Error();
    error.setReason(e.getMessage());
    commonResponse.setError(error);
  }
}
