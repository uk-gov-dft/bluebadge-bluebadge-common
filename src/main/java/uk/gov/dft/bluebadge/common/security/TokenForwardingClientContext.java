package uk.gov.dft.bluebadge.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public class TokenForwardingClientContext extends DefaultOAuth2ClientContext {
  @Override
  public OAuth2AccessToken getAccessToken() {
    Authentication principal = SecurityContextHolder.getContext().getAuthentication();
    if (principal instanceof OAuth2Authentication) {
      OAuth2Authentication authentication = (OAuth2Authentication) principal;
      Object details = authentication.getDetails();
      if (details instanceof OAuth2AuthenticationDetails) {
        OAuth2AuthenticationDetails oauthsDetails = (OAuth2AuthenticationDetails) details;
        return new DefaultOAuth2AccessToken(oauthsDetails.getTokenValue());
      }
    }
    return super.getAccessToken();
  }
}
