package uk.gov.dft.bluebadge.common.service.exception;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;

public class BadRequestExceptionTest {

  @Test
  public void singleFieldError() {
    ErrorErrors fieldError = getFieldError("Field1.error");

    BadRequestException exception = new BadRequestException(fieldError);

    ResponseEntity<CommonResponse> response = exception.getResponse();

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertTrue(response.getBody().getError().getErrors().contains(fieldError));
  }

  @Test
  public void multipleFieldErrors() {
    List<ErrorErrors> errors = new ArrayList<>();
    errors.add(getFieldError("First"));
    errors.add(getFieldError("Second"));

    BadRequestException exception = new BadRequestException(errors);

    ResponseEntity<CommonResponse> response = exception.getResponse();

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals(2, response.getBody().getError().getErrors().size());
  }

  @Test
  public void systemError() {
    Error error = new Error();
    error.setMessage("system.error");

    BadRequestException exception = new BadRequestException(error);
    ResponseEntity<CommonResponse> response = exception.getResponse();

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("system.error", response.getBody().getError().getMessage());
    Assert.assertNull(response.getBody().getError().getErrors());
  }

  private ErrorErrors getFieldError(String message) {
    ErrorErrors fieldError = new ErrorErrors();
    fieldError.setMessage(message);
    fieldError.setField("field");
    return fieldError;
  }
}
