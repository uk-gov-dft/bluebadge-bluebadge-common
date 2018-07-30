package uk.gov.dft.bluebadge.common.api.common;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfiguration {

  @NonNull @NotNull private String scheme;
  @NonNull @NotNull private String host;
  @NonNull @NotNull private Integer port;
  private Integer connectiontimeout;
  private Integer requesttimeout;
  @NonNull @NotNull private String contextpath;

  public String getUrlPrefix() {
    return UriComponentsBuilder.newInstance()
        .scheme(scheme)
        .host(host)
        .port(port)
        .path(contextpath)
        .build()
        .toUriString();
  }
}
