package uk.gov.dft.bluebadge.common.security;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import uk.gov.dft.bluebadge.common.security.model.User;

public class SecurityUtils {
  public static final String LOCAL_AUTHORITY_KEY = "local-authority";

  /**
   * Returns the currently logged in user.
   *
   * @return
   */
  public User getCurrentUserDetails() {

    OAuth2Authentication authentication = getCurrentAuthenticationContext();

    if (authentication.getDetails() == null) {
      throw new NullPointerException("Authentication details is null.");
    } else if (!(authentication.getDetails() instanceof OAuth2AuthenticationDetails)) {
      throw new IllegalStateException(
          "Authentication details of unsupported type: " + authentication.getDetails().getClass());
    }

    OAuth2AuthenticationDetails oauthDetails =
        (OAuth2AuthenticationDetails) authentication.getDetails();
    Map<String, String> additionalInfo = (Map<String, String>) oauthDetails.getDecodedDetails();

    String roleName =
        authentication
            .getAuthorities()
            .stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElseThrow(() -> new NullPointerException("Authentication has no authorities."));

    return User.builder()
        .emailAddress(authentication.getName())
        .roleName(roleName)
        .localAuthorityShortCode(extractLocalAuthorityShortCode(additionalInfo))
        .build();
  }

  /**
   * Returns the LocalAuthority as provided in the security credentials.
   *
   * @return
   */
  public String getCurrentLocalAuthorityShortCode() {
    return getCurrentUserDetails().getLocalAuthorityShortCode();
  }

  private String extractLocalAuthorityShortCode(Map<String, String> additionalInfo) {
    return additionalInfo.get(LOCAL_AUTHORITY_KEY);
  }

  /**
   * Get the current authentication context.
   *
   * @return The authentication context
   * @throws NullPointerException when the authentication context cannot be found.
   */
  private OAuth2Authentication getCurrentAuthenticationContext() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (null == authentication) {
      throw new NullPointerException("No user currently authenticated.");
    } else if (!(authentication instanceof OAuth2Authentication)) {
      throw new IllegalStateException(
          "Authentication of unsupported type: " + authentication.getClass());
    }
    return (OAuth2Authentication) authentication;
  }
}
