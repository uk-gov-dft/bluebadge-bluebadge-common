package uk.gov.dft.bluebadge.common.security;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@Builder
@ToString
public class BBPrincipal {
  private final String emailAddress;
  private final String localAuthorityShortCode;
  @NonNull private final String roleName;

  @NonNull private final String clientId;
}
