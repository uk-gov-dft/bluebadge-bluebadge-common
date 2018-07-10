package uk.gov.dft.bluebadge.common.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;

public class BiConverterTest {

  @Test
  public void convertToModelList() {

    // Given
    BiConverter<String, String> testConverter =
        new BiConverter<String, String>() {
          @Override
          public String convertToEntity(String model) throws BadRequestException {
            return null;
          }

          @Override
          public String convertToModel(String entity) {
            return entity;
          }
        };

    String[] values = {"Fred", "James", "Dotty"};
    List<String> strings = Arrays.asList(values);

    // When
    List<String> result = testConverter.convertToModelList(strings);

    assertThat(result).contains(values);
  }
}
