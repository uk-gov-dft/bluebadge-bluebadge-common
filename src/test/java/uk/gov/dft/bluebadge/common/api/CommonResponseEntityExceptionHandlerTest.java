package uk.gov.dft.bluebadge.common.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;

@RunWith(JUnit4.class)
public class CommonResponseEntityExceptionHandlerTest {

  private static final String ERROR_OBJECT = "errorObject";
  private static final String ERROR_FIELD = "errorField";
  private static final String ERROR_DEFAULT_MESSAGE = "defaultMessage";
  private static final String REJECTED_VALUE = "rejectedValue";
  private static final boolean IS_BINDING_FAILURE = true;
  private static final String[] ERROR_CODES = {"Error1", "Error2"};
  private static final Object[] ERROR_ARGUMENTS = {"arg1", "arg2"};

  private CommonResponseEntityExceptionHandler handler;

  @Mock private MethodArgumentNotValidException mockException;

  @Mock private HttpHeaders mockHeaders;

  @Mock private WebRequest mockRequest;

  @Mock private BindingResult mockBindingResult;

  @Before
  public void setup() {
    initMocks(this);
    when(mockException.getBindingResult()).thenReturn(mockBindingResult);
    handler = new CommonResponseEntityExceptionHandler();
  }

  @Test
  public void shoudReportFieldErrors() {
    reportFieldErrors(ERROR_CODES);
    reportFieldErrors(new String[] {});
  }

  @Test
  public void shouldReportClassLevelErrors() {
    reportClassLevelErrors(ERROR_CODES);
    reportClassLevelErrors(new String[] {});
  }

  private void reportFieldErrors(String[] errorCodes) {

    // Given
    when(mockBindingResult.getFieldErrors()).thenReturn(createFieldErrors(errorCodes));

    // When
    ResponseEntity<Object> result =
        handler.handleMethodArgumentNotValid(
            mockException, mockHeaders, HttpStatus.BAD_REQUEST, mockRequest);

    // Then
    assertErrorReturned(result);

    CommonResponse response = (CommonResponse) result.getBody();
    assertCommonResponseError(response);

    ErrorErrors errorErrors = response.getError().getErrors().get(0);
    assertThat(errorErrors.getField()).isEqualTo(ERROR_FIELD);
    assertThat(errorErrors.getReason()).isEqualTo(ERROR_DEFAULT_MESSAGE);
    if (errorCodes.length == 0) {
      assertThat(errorErrors.getMessage()).isEqualTo("Unknown");
    } else {
      assertThat(errorErrors.getMessage()).isEqualTo(errorCodes[0]);
    }
  }

  private void assertCommonResponseError(CommonResponse response) {
    assertThat(response.getError()).isNotNull();
    assertThat(response.getError().getErrors()).isNotNull();
    assertThat(response.getError().getErrors().size()).isEqualTo(1);
  }

  private void reportClassLevelErrors(String[] errorCodes) {

    // Given
    when(mockBindingResult.getGlobalErrors()).thenReturn(createObjectErrors(errorCodes));

    // When
    ResponseEntity<Object> result =
        handler.handleMethodArgumentNotValid(
            mockException, mockHeaders, HttpStatus.BAD_REQUEST, mockRequest);

    // Then
    assertErrorReturned(result);
    CommonResponse response = (CommonResponse) result.getBody();
    assertCommonResponseError(response);

    ErrorErrors errorErrors = response.getError().getErrors().get(0);
    assertThat(errorErrors.getField()).isNull();
    assertThat(errorErrors.getReason()).isEqualTo(ERROR_DEFAULT_MESSAGE);
    if (errorCodes.length == 0) {
      assertThat(errorErrors.getMessage()).isEqualTo("Unknown");
    } else {
      assertThat(errorErrors.getMessage()).isEqualTo(errorCodes[0]);
    }
  }

  private void assertErrorReturned(ResponseEntity<Object> result) {
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody()).isInstanceOf(CommonResponse.class);
  }

  private List<ObjectError> createObjectErrors(String[] errorCodes) {
    List<ObjectError> errors = new ArrayList<>();
    errors.add(new ObjectError(ERROR_OBJECT, errorCodes, ERROR_ARGUMENTS, ERROR_DEFAULT_MESSAGE));
    return errors;
  }

  private List<FieldError> createFieldErrors(String[] errorCodes) {
    List<FieldError> errors = new ArrayList<>();
    errors.add(
        new FieldError(
            ERROR_OBJECT,
            ERROR_FIELD,
            REJECTED_VALUE,
            IS_BINDING_FAILURE,
            errorCodes,
            ERROR_ARGUMENTS,
            ERROR_DEFAULT_MESSAGE));
    return errors;
  }
}
