package uk.gov.dft.bluebadge.common.service.exception;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.common.api.model.Error;

public class InternalServerExceptionTest {

  @Test
  public void testResponse() {
    Error error = new Error();
    error.setMessage("ABC");
    InternalServerException exception = new InternalServerException(error);
    ResponseEntity<CommonResponse> response = exception.getResponse();

    Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    //noinspection ConstantConditions
    Assert.assertEquals("ABC", response.getBody().getError().getMessage());
  }

  @Test
  public void testResponseWhenConstructedWithException() {
    InternalServerException exception = new InternalServerException(new NullPointerException());

    ResponseEntity<CommonResponse> response = exception.getResponse();

    Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}
