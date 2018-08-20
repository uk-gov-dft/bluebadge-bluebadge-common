package uk.gov.dft.bluebadge.common.util;

import uk.gov.dft.bluebadge.common.security.BBPrincipal;

public class TestBBPrincipal {
  public static BBPrincipal.BBPrincipalBuilder clientCreds() {
    return BBPrincipal.builder()
        .clientId("fake_client")
        .emailAddress(null)
        .localAuthorityShortCode("ABERD")
        .roleName("LA Admin");
  }

  public static BBPrincipal.BBPrincipalBuilder user() {
    return clientCreds().emailAddress("bob@bob.com");
  }
}
