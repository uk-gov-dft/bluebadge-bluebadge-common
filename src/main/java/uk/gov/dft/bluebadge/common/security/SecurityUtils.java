package uk.gov.dft.bluebadge.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.common.security.model.LocalAuthority;
import uk.gov.dft.bluebadge.common.security.model.User;

@Component
public class SecurityUtils {
  private static final int MOCK_LOCAL_AUTHORITY = 2;
  private static final Integer MOCK_ROLE_ID = 1;
  private static final String MOCK_LOCAL_AUTHORITY_SHORT_CODE = "BIRM";

  /**
   * Returns the currently logged in user.
   *
   * @return
   */
  public User getCurrentUserDetails() {

    Authentication authentication = getCurrentAuthenticationContext();

    return User.builder()
        .localAuthority(createMockLocalAuthority())
        .emailAddress(authentication.getName())
        .name("TODO SecurityUtils")
        .roleId(MOCK_ROLE_ID)
        .build();
  }

  /**
   * Returns the LocalAuthority as provided in the security credentials.
   *
   * @return
   */
  public LocalAuthority getCurrentLocalAuthority() {
    return createMockLocalAuthority();
  }

  // TODO: This should be replaced with a conctrete implemention of something real.
  private LocalAuthority createMockLocalAuthority() {
    return LocalAuthority.builder()
        .id(MOCK_LOCAL_AUTHORITY)
        .shortCode(MOCK_LOCAL_AUTHORITY_SHORT_CODE)
        .build();
  }

  /**
   * Get the current authentication context.
   *
   * @return The authentication context
   * @throws NullPointerException when the authentication context cannot be found.
   */
  private Authentication getCurrentAuthenticationContext() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Would be a coding error if this called for a non-authenticated area.
    if (null == authentication) {
      throw new NullPointerException("No user currently authenticated.");
    }
    return authentication;
  }
}
