package uk.gov.dft.bluebadge.common.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
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
import uk.gov.dft.bluebadge.common.security.model.BBPrincipal;
import uk.gov.dft.bluebadge.common.security.model.LocalAuthorityControlled;
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
          ImmutableSet.of(
              new SimpleGrantedAuthority("ROLE_" + Role.LA_ADMIN),
              new SimpleGrantedAuthority("PERM_FIND_USERS")));

  private SecurityUtils securityUtils;
  private OAuth2Authentication auth2Authentication;
  private ImmutableMap<String, String> claims;

  @Before
  public void setUp() {
    initMocks(this);
    securityUtils = new SecurityUtils();
    SecurityContextHolder.setContext(mockSecurityContext);
    auth2Authentication = new OAuth2Authentication(request, userAuthentication);
    claims =
        ImmutableMap.<String, String>builder()
            .put("local-authority", TEST_LA_SHORT_CODE)
            .put("client_id", "fakeClient")
            .put("user_name", DEFAULT_EMAIL_ADDRESS)
            .build();
  }

  private void setupAuthenticationDetails() {
    HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
    OAuth2AuthenticationDetails details = new OAuth2AuthenticationDetails(mockHttpRequest);
    details.setDecodedDetails(claims);
    auth2Authentication.setDetails(details);
  }

  @Test
  public void shouldReturnAValidPrincipal() {

    // given
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    setupAuthenticationDetails();

    // when
    BBPrincipal currentUserDetails = securityUtils.getCurrentAuth();

    // then
    assertThat(currentUserDetails.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
    assertThat(currentUserDetails.getRoleName()).isEqualTo("ROLE_LA_ADMIN");
    assertThat(currentUserDetails.getLocalAuthorityShortCode()).isEqualTo(TEST_LA_SHORT_CODE);
    assertThat(currentUserDetails.getClientId()).isEqualTo("fakeClient");
  }

  @Test
  public void shouldReturnAValidPrincipal_whenAuthDetailsIsAMap() {

    // given
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    auth2Authentication.setDetails(claims);

    // when
    BBPrincipal currentUserDetails = securityUtils.getCurrentAuth();

    // then
    assertThat(currentUserDetails.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
    assertThat(currentUserDetails.getRoleName()).isEqualTo("ROLE_LA_ADMIN");
    assertThat(currentUserDetails.getLocalAuthorityShortCode()).isEqualTo(TEST_LA_SHORT_CODE);
    assertThat(currentUserDetails.getClientId()).isEqualTo("fakeClient");
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
    assertThat(currentUserDetails.getRoleName()).isEqualTo("ROLE_LA_ADMIN");
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

  @Test
  public void whenAuthenticationDetailsNotAsExpected_thenException_todoLocalAuthIsAberd() {
    // given
    auth2Authentication.setDetails(new ArrayList<>());
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);

    // when
    User currentUserDetails = securityUtils.getCurrentUserDetails();
    assertThat(currentUserDetails.getLocalAuthorityShortCode()).isEqualTo("ABERD");
  }

  @Test
  public void whenSameLA_thenAuthorised() {
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    setupAuthenticationDetails();

    TestLAControlled laControlled =
        TestLAControlled.builder().localAuthorityShortCode(TEST_LA_SHORT_CODE).build();
    boolean result = securityUtils.isAuthorisedLA(laControlled);

    assertThat(result).isTrue();
  }

  @Test
  public void whenDifferentLA_thenNotAuthorised() {
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    setupAuthenticationDetails();

    TestLAControlled laControlled =
        TestLAControlled.builder().localAuthorityShortCode("ABERD").build();
    boolean result = securityUtils.isAuthorisedLA(laControlled);

    assertThat(result).isFalse();
  }

  @Test
  public void whenControlledLAIsNull_thenNotAuthorised() {
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    setupAuthenticationDetails();

    TestLAControlled laControlled = TestLAControlled.builder().build();
    boolean result = securityUtils.isAuthorisedLA(laControlled);

    assertThat(result).isFalse();
  }

  @Test
  public void whenUserHasNoLA_andNotDftUser_thenNotAuthorised() {
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    claims = ImmutableMap.<String, String>builder().put("client_id", "fakeClient").build();
    setupAuthenticationDetails();

    TestLAControlled laControlled =
        TestLAControlled.builder().localAuthorityShortCode("ABERD").build();
    assertThat(securityUtils.isAuthorisedLA(laControlled)).isFalse();
  }

  @Test
  public void whenUserHasNoLA_andDftUser_thenAuthorised() {
    userAuthentication =
        new UsernamePasswordAuthenticationToken(
            DEFAULT_EMAIL_ADDRESS,
            "bar",
            ImmutableSet.of(
                new SimpleGrantedAuthority("ROLE_" + Role.DFT_ADMIN.name()),
                new SimpleGrantedAuthority("PERM_FIND_USERS")));
    auth2Authentication = new OAuth2Authentication(request, userAuthentication);
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    claims = ImmutableMap.<String, String>builder().put("client_id", "fakeClient").build();
    setupAuthenticationDetails();

    TestLAControlled laControlled =
        TestLAControlled.builder().localAuthorityShortCode("ABERD").build();
    assertThat(securityUtils.isAuthorisedLA(laControlled)).isTrue();
  }

  @Test
  public void isPermitted_shouldReturnTrue_WhenPermissionIsInUserAuthorities() {
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    boolean result = securityUtils.isPermitted(Permissions.FIND_USERS);
    assertThat(result).isTrue();
  }

  @Test
  public void isPermitted_shouldReturnFalse_WhenPermissionIsNotInUserAuthorities() {
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    boolean result = securityUtils.isPermitted(Permissions.FIND_BADGES);
    assertThat(result).isFalse();
  }

  @Test
  public void isPermitted_shouldReturnFalse_whenNull() {
    boolean result = securityUtils.isPermitted(null);
    assertThat(result).isFalse();
  }

  @Test
  public void hasRole_shouldReturnTrue_WhenUserHasRole() {
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    boolean result = securityUtils.hasRole(Role.LA_ADMIN);
    assertThat(result).isTrue();
  }

  @Test
  public void hasRole_shouldReturnFalse_WhenUserDoesntHasRole() {
    when(mockSecurityContext.getAuthentication()).thenReturn(auth2Authentication);
    boolean result = securityUtils.hasRole(Role.DFT_ADMIN);
    assertThat(result).isFalse();
  }

  @Test
  public void hasRole_shouldReturnFalse_whenNull() {
    boolean result = securityUtils.hasRole(null);
    assertThat(result).isFalse();
  }

  @Builder
  @Getter
  private static class TestLAControlled implements LocalAuthorityControlled {
    private String localAuthorityShortCode;
  }
}
