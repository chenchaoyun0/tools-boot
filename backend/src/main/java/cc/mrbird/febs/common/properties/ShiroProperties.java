package cc.mrbird.febs.common.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ShiroProperties {


  @Value("${febs.shiro.anonUrl}")
  private String anonUrl;

  /**
   * token默认有效时间 1天
   */
  @Value("${febs.shiro.jwtTimeOut}")
  private Long jwtTimeOut = 86400L;

}
