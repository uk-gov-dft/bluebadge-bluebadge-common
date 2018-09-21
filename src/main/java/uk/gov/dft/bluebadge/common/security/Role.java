package uk.gov.dft.bluebadge.common.security;

import static uk.gov.dft.bluebadge.common.security.Permissions.CANCEL_BADGE;
import static uk.gov.dft.bluebadge.common.security.Permissions.CREATE_DFT_USER;
import static uk.gov.dft.bluebadge.common.security.Permissions.CREATE_USER;
import static uk.gov.dft.bluebadge.common.security.Permissions.DELETE_APPLICATION;
import static uk.gov.dft.bluebadge.common.security.Permissions.DELETE_BADGE;
import static uk.gov.dft.bluebadge.common.security.Permissions.DELETE_DFT_USER;
import static uk.gov.dft.bluebadge.common.security.Permissions.DELETE_USER;
import static uk.gov.dft.bluebadge.common.security.Permissions.FIND_APPLICATION;
import static uk.gov.dft.bluebadge.common.security.Permissions.FIND_BADGES;
import static uk.gov.dft.bluebadge.common.security.Permissions.FIND_USERS;
import static uk.gov.dft.bluebadge.common.security.Permissions.ORDER_BADGE;
import static uk.gov.dft.bluebadge.common.security.Permissions.RENEW_BADGE;
import static uk.gov.dft.bluebadge.common.security.Permissions.REPLACE_BADGE;
import static uk.gov.dft.bluebadge.common.security.Permissions.RESET_DFT_USER_PASSWORD;
import static uk.gov.dft.bluebadge.common.security.Permissions.RESET_USER_PASSWORD;
import static uk.gov.dft.bluebadge.common.security.Permissions.UPDATE_DFT_USER;
import static uk.gov.dft.bluebadge.common.security.Permissions.UPDATE_USER;
import static uk.gov.dft.bluebadge.common.security.Permissions.VIEW_APPLICATION_DETAILS;
import static uk.gov.dft.bluebadge.common.security.Permissions.VIEW_BADGE_DETAILS;
import static uk.gov.dft.bluebadge.common.security.Permissions.VIEW_USER_DETAILS;

import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum Role {
  DFT_ADMIN(1, "DfT Administrator", PermissionGroups.DFT_ADMIN_PERMS),
  LA_ADMIN(2, "Administrator", PermissionGroups.LA_ADMIN_PERMS),
  LA_EDITOR(3, "Editor", PermissionGroups.EDITOR),
  LA_READ(4, "View Only", PermissionGroups.READ),
  API_CLIENT(6, "API Client", PermissionGroups.API);

  private final Set<Permissions> permissionss;
  private final String prettyName;
  private final int roleId;

  Role(int roleId, String prettyName, Set<Permissions> permissions) {
    this.roleId = roleId;
    this.prettyName = prettyName;
    permissionss = Collections.unmodifiableSet(permissions);
  }

  public static Role findForPrettyName(String name) {
    return Stream.of(Role.values())
        .filter(r -> r.prettyName.equals(name))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No role found for name:" + name));
  }

  public static Role getById(int id) {
	    return Stream.of(Role.values())
	        .filter(r -> r.roleId == id)
	        .findFirst()
	        .orElseThrow(() -> new IllegalArgumentException("No role found for id:" + id));
	  }

  private static class PermissionGroups {
    private static final Set<Permissions> READ = EnumSet.of(FIND_BADGES, VIEW_BADGE_DETAILS);

    private static final Set<Permissions> EDITOR =
        ImmutableSet.<Permissions>builder()
            .addAll(
                EnumSet.of(
                    CANCEL_BADGE,
                    ORDER_BADGE,
                    REPLACE_BADGE,
                    RENEW_BADGE,
                    DELETE_BADGE,
                    FIND_APPLICATION,
                    VIEW_APPLICATION_DETAILS,
                    DELETE_APPLICATION))
            .addAll(READ)
            .build();

    private static final Set<Permissions> ADMIN =
        EnumSet.of(
            FIND_USERS,
            CREATE_USER,
            VIEW_USER_DETAILS,
            UPDATE_USER,
            DELETE_USER,
            RESET_USER_PASSWORD);
    private static final Set<Permissions> LA_ADMIN_PERMS =
        ImmutableSet.<Permissions>builder().addAll(ADMIN).addAll(EDITOR).build();

    private static final Set<Permissions> DFT_ADMIN_PERMS =
        ImmutableSet.<Permissions>builder()
            .addAll(ADMIN)
            .addAll(
                EnumSet.of(
                    CREATE_DFT_USER, UPDATE_DFT_USER, DELETE_DFT_USER, RESET_DFT_USER_PASSWORD))
            .build();

    private static final Set<Permissions> API = EDITOR;
  }
}
