package uk.gov.dft.bluebadge.common.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;

@ControllerAdvice
public class CommonResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid (
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    CommonResponse commonResponse = new CommonResponse();
    Error systemError = new Error();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      // Don't know yet if codes[0] always exists, but looks like a good
      // message property file key.
      String messageProperty = "Unknown";
      if (error.getCodes().length > 0) {
        messageProperty = error.getCodes()[0];
      }
      systemError.addErrorsItem(
          new ErrorErrors()
              .field(error.getField())
              .message(messageProperty)
              .reason(error.getDefaultMessage()));
    }

    // Don't expect to get any object errors.
    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      // Don't know yet if codes[0] always exists, but looks like a good
      // message property file key.
      String messageProperty = "Unknown";
      if (error.getCodes().length > 0) {
        messageProperty = error.getCodes()[0];
      }
      systemError.addErrorsItem(
          new ErrorErrors().field(null).message(messageProperty).reason(error.getDefaultMessage()));
    }

    commonResponse.setError(systemError);

    return handleExceptionInternal(ex, commonResponse, headers, status, request);
  }
}
