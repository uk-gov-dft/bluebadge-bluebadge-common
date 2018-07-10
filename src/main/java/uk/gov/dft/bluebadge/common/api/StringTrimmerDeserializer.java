package uk.gov.dft.bluebadge.common.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class StringTrimmerDeserializer extends JsonDeserializer<String> {

  @Override
  public String deserialize(final JsonParser jsonParser, final DeserializationContext context)
      throws IOException {

    String result = StringDeserializer.instance.deserialize(jsonParser, context);
    return StringUtils.trimToNull(result);
  }
}
