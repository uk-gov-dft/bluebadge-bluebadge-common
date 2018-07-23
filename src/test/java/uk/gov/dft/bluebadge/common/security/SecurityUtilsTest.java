package uk.gov.dft.bluebadge.common.security;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.gov.dft.bluebadge.common.security.model.LocalAuthority;
import uk.gov.dft.bluebadge.common.security.model.User;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SecurityUtilsTest {

  private static final String DEFAULT_EMAIL_ADDRESS = "fred@bloggs.com";
  private static final int MOCK_LOCAL_AUTHORITY_ID = 22;

  @Mock private SecurityContext mockSecurityContext;

  @Mock private Authentication mockAuthentication;

  private SecurityUtils securityUtils;

  @Before
  public void setUp() {
    initMocks(this);
    securityUtils = new SecurityUtils();
    SecurityContextHolder.setContext(mockSecurityContext);
  }

  @Test
  public void shouldReturnAValidUser() {

    // given
    when(mockAuthentication.getName()).thenReturn(DEFAULT_EMAIL_ADDRESS);
    when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

    // when
    User currentUserDetails = securityUtils.getCurrentUserDetails();

    // then
    assertThat(currentUserDetails.getEmailAddress(), is(DEFAULT_EMAIL_ADDRESS));
  }

  @Test
  public void shouldReturnAValidLocalAuthority() {
    // given

    // when
    LocalAuthority currentLocalAuthority = securityUtils.getCurrentLocalAuthority();

    // then
    assertThat(currentLocalAuthority.getId(), is(MOCK_LOCAL_AUTHORITY_ID));
  }
}
