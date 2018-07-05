package uk.gov.dft.bluebadge.common.service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.dft.bluebadge.common.api.domain.CommonResponse;
import uk.gov.dft.bluebadge.common.api.domain.Error;

public abstract class ServiceException extends RuntimeException {
  final transient CommonResponse commonResponse;
  private final HttpStatus httpStatus;

  ServiceException(HttpStatus httpStatus) {
    super();
    this.httpStatus = httpStatus;
    commonResponse = new CommonResponse();
    Error error = new Error();
    commonResponse.setError(error);
  }

  public ResponseEntity<CommonResponse> getResponse() {
    return ResponseEntity.status(httpStatus).body(commonResponse);
  }
}
