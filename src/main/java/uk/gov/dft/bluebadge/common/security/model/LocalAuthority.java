package uk.gov.dft.bluebadge.common.security.model;

import lombok.Builder;
import lombok.Data;

/** Provides details of the LocalAuthority provided through the security policy. */
@Data
@Builder
public class LocalAuthority {
  private int id;
  private String shortCode;
}
