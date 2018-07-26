package uk.gov.dft.bluebadge.common.security.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/** User */
@Data
@Builder
@ToString
public class User {
  private Integer id;
  private String name;
  private String emailAddress;
  private LocalAuthority localAuthority;
  private Integer roleId;
  private String roleName;
}
