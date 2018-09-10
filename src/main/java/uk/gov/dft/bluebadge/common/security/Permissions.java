package uk.gov.dft.bluebadge.common.security;

public enum Permissions {
  CREATE_USER,
  CREATE_DFT_USER,
  FIND_USERS,
  VIEW_USER_DETAILS,
  UPDATE_USER,
  UPDATE_DFT_USER,
  DELETE_USER,
  DELETE_DFT_USER,
  RESET_USER_PASSWORD,
  RESET_DFT_USER_PASSWORD,

  FIND_BADGES,
  VIEW_BADGE_DETAILS,
  CANCEL_BADGE,
  ORDER_BADGE,
  REPLACE_BADGE,
  RENEW_BADGE,
  DELETE_BADGE,

  FIND_APPLICATION,
  VIEW_APPLICATION_DETAILS,
  DELETE_APPLICATION;

  public static final String PERMISSION_PREFIX = "PERM_";

  public String getPermissionName() {
    return PERMISSION_PREFIX + name();
  }
}
