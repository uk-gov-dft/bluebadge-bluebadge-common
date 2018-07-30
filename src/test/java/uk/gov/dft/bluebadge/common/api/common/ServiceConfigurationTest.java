package uk.gov.dft.bluebadge.common.api.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

import org.junit.Test;

public class ServiceConfigurationTest {
  @Test
  public void getUrlPrefix_full() {
    ServiceConfiguration serviceConfiguration =
        ServiceConfiguration.builder()
            .scheme("http")
            .host("blahblah")
            .port(9876)
            .contextpath("hereandthere")
            .build();
    assertThat(serviceConfiguration.getUrlPrefix()).isEqualTo("http://blahblah:9876/hereandthere");
  }

  @Test
  public void attributes_notNull() {
    ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
    try {
      serviceConfiguration.setScheme(null);
      fail("null allowed");
    } catch (NullPointerException ne) {}
    try {
      serviceConfiguration.setHost(null);
      fail("null allowed");
    } catch (NullPointerException ne) {}
    try {
      serviceConfiguration.setPort(null);
      fail("null allowed");
    } catch (NullPointerException ne) {}
    try {
      serviceConfiguration.setContextpath(null);
      fail("null allowed");
    } catch (NullPointerException ne) {}
  }

  @Test
  public void builderAttributes_notNull() {
    try {
      ServiceConfiguration.builder().build();
      fail("null allowed");
    } catch (NullPointerException ne) {}
    try {
      ServiceConfiguration.builder().scheme("non").build();
      fail("null allowed");
    } catch (NullPointerException ne) {}
    try {
      ServiceConfiguration.builder().scheme("bob").host("").build();
      fail("null allowed");
    } catch (NullPointerException ne) {}
    try {
      ServiceConfiguration.builder().scheme("bob").host("").port(1).build();
      fail("null allowed");
    } catch (NullPointerException ne) {}
  }
}
