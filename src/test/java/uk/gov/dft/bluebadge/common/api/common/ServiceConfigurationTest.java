package uk.gov.dft.bluebadge.common.api.common;

import static org.assertj.core.api.Assertions.assertThat;

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
}
