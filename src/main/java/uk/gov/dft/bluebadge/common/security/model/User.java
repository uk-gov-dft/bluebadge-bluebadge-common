package uk.gov.dft.bluebadge.common.security.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/** User */
@Data
@Builder
@ToString
public class User {
  private final String emailAddress;
  private final String localAuthorityShortCode;
  private final String roleName;
}
