package cc.mrbird.febs.common.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class FebsProperties {

  @Autowired
  private ShiroProperties shiro;

  @Value("${febs.openAopLog}")
  private boolean openAopLog = true;

}
