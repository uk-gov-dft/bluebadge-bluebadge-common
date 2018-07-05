package uk.gov.dft.bluebadge.common.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class StringTrimmerDeserializer extends JsonDeserializer<String> {

  @Override
  public String deserialize(final JsonParser p, final DeserializationContext ctxt)
      throws IOException {

    String result = StringDeserializer.instance.deserialize(p, ctxt);
    return StringUtils.trimToNull(result);
  }
}
