package uk.gov.dft.bluebadge.common.security;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import uk.gov.dft.bluebadge.common.security.model.BBPrincipal;
import uk.gov.dft.bluebadge.common.security.model.LocalAuthorityControlled;
import uk.gov.dft.bluebadge.common.security.model.User;

@Slf4j
public class SecurityUtils {
  public static final String LOCAL_AUTHORITY_KEY = "local-authority";
  public static final String USER_NAME_KEY = "user_name";
  public static final String CLIENT_ID_KEY = "client_id";

  private final AccessDecisionManager accessDecisionManager;

  @Autowired
  public SecurityUtils(AccessDecisionManager accessDecisionManager) {
    this.accessDecisionManager = accessDecisionManager;
  }

  /**
   * @return The user extracted from security context and JWT claims
   * @deprecated use getCurrentAuth instead
   */
  @Deprecated
  public User getCurrentUserDetails() {
    BBPrincipal currentAuth = getCurrentAuth();

    return User.builder()
        .emailAddress(currentAuth.getEmailAddress())
        .roleName(currentAuth.getRoleName())
        .localAuthorityShortCode(currentAuth.getLocalAuthorityShortCode())
        .build();
  }

  public BBPrincipal getCurrentAuth() {
    OAuth2Authentication authentication = getCurrentAuthenticationContext();

    Map<String, String> additionalInfo;
    if (authentication.getDetails() == null) {
      throw new NullPointerException("Authentication details is null.");
    } else if ((authentication.getDetails() instanceof OAuth2AuthenticationDetails)) {
      OAuth2AuthenticationDetails oauthDetails =
          (OAuth2AuthenticationDetails) authentication.getDetails();
      additionalInfo = (Map<String, String>) oauthDetails.getDecodedDetails();
    } else if ((authentication.getDetails() instanceof Map)) {
      additionalInfo = ImmutableMap.copyOf((Map) authentication.getDetails());
    } else {
      // Backward compatible to allow older services to use the latest security utils.
      log.warn("Old security authentication being used. Using hard coded local authority!");
      additionalInfo = ImmutableMap.of(LOCAL_AUTHORITY_KEY, "ABERD", CLIENT_ID_KEY, "unknown");
    }

    String roleName =
        authentication
            .getAuthorities()
            .stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElseThrow(() -> new NullPointerException("Authentication has no authorities."));

    return BBPrincipal.builder()
        .clientId(additionalInfo.get(CLIENT_ID_KEY))
        .emailAddress(additionalInfo.get(USER_NAME_KEY))
        .roleName(roleName)
        .localAuthorityShortCode(additionalInfo.get(LOCAL_AUTHORITY_KEY))
        .build();
  }

  /** @return The LocalAuthority as provided in the security credentials. */
  public String getCurrentLocalAuthorityShortCode() {
    return getCurrentAuth().getLocalAuthorityShortCode();
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

  public boolean isAuthorisedLA(LocalAuthorityControlled localAuthorityControlled) {
    return isAuthorisedLA(localAuthorityControlled.getLocalAuthorityShortCode());
  }

  public boolean isAuthorisedLA(String localAuthority) {
    String currentLocalAuthorityShortCode = getCurrentLocalAuthorityShortCode();
    if (null == currentLocalAuthorityShortCode) {
      throw new IllegalStateException("Principal's local authority is null");
    }
    return currentLocalAuthorityShortCode.equals(localAuthority);
  }

  public boolean isPermitted(Permissions permission) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      accessDecisionManager.decide(
          authentication,
          null,
          Collections.singletonList(new SecurityConfig(permission.getPermissionName())));
    } catch (InsufficientAuthenticationException e) {
      return false;
    }
    return true;
  }
}
