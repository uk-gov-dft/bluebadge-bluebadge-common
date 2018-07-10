package uk.gov.dft.bluebadge.common.service.exception;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

public class NotFoundExceptionTest {

  @Test
  public void testResponse() {
    NotFoundException exception =
        new NotFoundException("badge", NotFoundException.Operation.RETRIEVE);
    ResponseEntity<CommonResponse> response = exception.getResponse();

    Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assert.assertEquals("NotFound.badge", response.getBody().getError().getMessage());
  }
}
