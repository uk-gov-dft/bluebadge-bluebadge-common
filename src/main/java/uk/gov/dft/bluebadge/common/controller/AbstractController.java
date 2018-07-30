package uk.gov.dft.bluebadge.common.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.ServiceException;

public abstract class AbstractController {

  @SuppressWarnings("unused")
  @ExceptionHandler({ServiceException.class})
  public ResponseEntity<CommonResponse> handleServiceException(ServiceException e) {
    return e.getResponse();
  }

  @SuppressWarnings("unused")
  @ExceptionHandler({InvalidFormatException.class})
  public ResponseEntity<CommonResponse> handleInvalidFormatException(InvalidFormatException e) {
    Error error = new Error();
    error.setReason(e.getMessage());
    error.setMessage("InvalidFormat." + e.getTargetType().getSimpleName());
    BadRequestException e1 = new BadRequestException(error);
    return e1.getResponse();
  }
}
