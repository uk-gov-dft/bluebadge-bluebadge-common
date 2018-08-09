package uk.gov.dft.bluebadge.common.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.junit.Test;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class BBAccessTokenConverterTest {

  BBAccessTokenConverter bbAccessTokenConverter = new BBAccessTokenConverter();

  @Test
  public void whenClaimsSupplied_thenSetAsDetails() {
    Map<String, ?> claims = ImmutableMap.of("bob", "jones");
    OAuth2Authentication oAuth2Authentication =
        bbAccessTokenConverter.extractAuthentication(claims);
    assertThat(oAuth2Authentication.getDetails()).isNotNull();
    assertThat(oAuth2Authentication.getDetails()).isSameAs(claims);
  }
}
