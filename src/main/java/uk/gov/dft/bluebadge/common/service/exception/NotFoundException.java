package uk.gov.dft.bluebadge.common.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFoundException extends ServiceException {

  @Override
  public ResponseEntity<CommonResponse> getResponse() {
    return ResponseEntity.status(NOT_FOUND).body(commonResponse);
  }

  public enum Operation {
    DELETE("delete"),
    UPDATE("update"),
    RETRIEVE("retrieve");

    private final String description;

    Operation(String description) {
      this.description = description;
    }
  }

  public NotFoundException(String objectName, Operation operation) {
    super(HttpStatus.NOT_FOUND);
    commonResponse.getError().setMessage("NotFound." + objectName);
    commonResponse
        .getError()
        .setReason("Could not " + operation.description + " " + objectName + ".");
  }
}
