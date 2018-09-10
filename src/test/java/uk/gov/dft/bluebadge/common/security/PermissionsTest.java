package uk.gov.dft.bluebadge.common.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class PermissionsTest {

  @Test
  public void createUserPermissionName(){
    assertThat(Permissions.CREATE_USER.getPermissionName()).isEqualTo("PERM_CREATE_USER");
  }
  @Test
  public void permissionNameTest(){
    Permissions[] values = Permissions.values();
    List<String> permNames = Stream.of(values).map(p -> p.getPermissionName()).collect(Collectors.toList());
    List<String> expected = Stream.of(values).map(p -> "PERM_" + p.name()).collect(Collectors.toList());

    assertThat(permNames).containsExactlyInAnyOrderElementsOf(expected);
  }
}