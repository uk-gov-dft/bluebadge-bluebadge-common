package uk.gov.dft.bluebadge.common.service.exception;

import com.google.common.collect.Lists;
import org.springframework.http.HttpStatus;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;

import java.util.List;

public class BadRequestException extends ServiceException {

  public BadRequestException(List<ErrorErrors> fieldErrors) {
    super(HttpStatus.BAD_REQUEST);
    commonResponse.getError().setErrors(fieldErrors);
  }

  public BadRequestException(ErrorErrors fieldError) {
    this(Lists.newArrayList(fieldError));
  }
}
