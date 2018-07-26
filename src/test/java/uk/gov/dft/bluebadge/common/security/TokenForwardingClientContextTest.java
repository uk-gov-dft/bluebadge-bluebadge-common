package uk.gov.dft.bluebadge.common.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public class TokenForwardingClientContextTest {
  @Mock private SecurityContext mockSecurityContext;
  @Mock private OAuth2Authentication mockOAuth2Authentication;
  @Mock private OAuth2AuthenticationDetails mockOAuth2AuthenticationDetails;

  private TokenForwardingClientContext tokenForwardingClientContext;

  @Before
  public void setUp() {
    initMocks(this);
    tokenForwardingClientContext = new TokenForwardingClientContext();
    SecurityContextHolder.setContext(mockSecurityContext);
    tokenForwardingClientContext.setAccessToken(new DefaultOAuth2AccessToken("jane"));
  }

  @Test
  public void getAccessToken_whenOauthContext_thenOAuthTokenReturned() {
    when(mockSecurityContext.getAuthentication()).thenReturn(mockOAuth2Authentication);
    when(mockOAuth2Authentication.getDetails()).thenReturn(mockOAuth2AuthenticationDetails);
    when(mockOAuth2AuthenticationDetails.getTokenValue()).thenReturn("bob");

    OAuth2AccessToken accessToken = tokenForwardingClientContext.getAccessToken();
    assertThat(accessToken).isNotNull();
    assertThat(accessToken.getValue()).isEqualTo("bob");
  }

  @Test
  public void getAccessToken_whenNotOauthContext_thenDefaultBehaviour() {
    when(mockSecurityContext.getAuthentication())
        .thenReturn(new UsernamePasswordAuthenticationToken(null, null));

    OAuth2AccessToken accessToken = tokenForwardingClientContext.getAccessToken();
    assertThat(accessToken).isNotNull();
    assertThat(accessToken.getValue()).isEqualTo("jane");
  }

  @Test
  public void getAccessToken_whenNotOauthDetails_thenDefaultBehaviour() {
    when(mockSecurityContext.getAuthentication()).thenReturn(mockOAuth2Authentication);
    when(mockOAuth2Authentication.getDetails()).thenReturn(new Object());

    OAuth2AccessToken accessToken = tokenForwardingClientContext.getAccessToken();
    assertThat(accessToken).isNotNull();
    assertThat(accessToken.getValue()).isEqualTo("jane");
  }
}
