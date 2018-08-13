package uk.gov.dft.bluebadge.common.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import uk.gov.dft.bluebadge.common.security.model.User;

public class SecurityUtilsTest {

  private static final String DEFAULT_EMAIL_ADDRESS = "fred@bloggs.com";
  private static final String TEST_LA_SHORT_CODE = "man";

  @Mock private SecurityContext mockSecurityContext;

  private OAuth2Request request =
      new OAuth2Request(
          null, "id", null, false, Collections.singleton("read"), null, null, null, null);

  private UsernamePasswordAuthenticationToken userAuthentication =
      new UsernamePasswordAuthenticationToken(
          DEFAULT_EMAIL_ADDRESS,
          "bar",
          Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

  private SecurityUtils securityUtils;
  private OAuth2Authentication auth2Authentication;

  @Before
  public void setUp() {
    initMocks(this);
    securityUtils = new SecurityUtils();
    SecurityContextHolder.setContext(mockSecurityContext);
    auth2Authentication = new OAuth2Authentication(request, userAuthentication);
  }

  private void setupAuthenticationDetails() {
    HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
    OAuth2AuthenticationDetails details = new OAuth2AuthenticationDetails(mockHttpRequest);
    details.setDecodedDetails(ImmutableMap.of("local-authority", TEST_LA_SHORT_CODE));
    auth2Authentication.setDetails(details);
  }

  @Test
  public void shouldReturnAValidUser() {

    // given
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    setupAuthenticationDetails();

    // when
    User currentUserDetails = securityUtils.getCurrentUserDetails();

    // then
    assertThat(currentUserDetails.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
    assertThat(currentUserDetails.getRoleName()).isEqualTo("ROLE_USER");
    assertThat(currentUserDetails.getLocalAuthorityShortCode()).isEqualTo(TEST_LA_SHORT_CODE);
  }

  @Test
  public void shouldReturnAValidLocalAuthority() {
    // given
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    setupAuthenticationDetails();

    // when
    String currentLocalAuthority = securityUtils.getCurrentLocalAuthorityShortCode();

    // then
    assertThat(currentLocalAuthority).isEqualTo(TEST_LA_SHORT_CODE);
  }

  @Test(expected = NullPointerException.class)
  public void whenAuthenticationNull_thenException() {
    securityUtils.getCurrentUserDetails();
  }
  @Test(expected = IllegalStateException.class)
  public void whenNotOAuthAuthentication_thenException() {
    // given
    when(mockSecurityContext.getAuthentication()).thenReturn(userAuthentication);

    // when
    securityUtils.getCurrentUserDetails();
  }
  @Test(expected = NullPointerException.class)
  public void whenAuthenticationDetailsNull_thenException() {
    // given
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);

    // when
    securityUtils.getCurrentUserDetails();
  }
  @Test(expected = IllegalStateException.class)
  public void whenAuthenticationDetailsNotAsExpected_thenException() {
    // given
    auth2Authentication.setDetails(new ArrayList<>());
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);

    // when
    securityUtils.getCurrentUserDetails();
  }
}
