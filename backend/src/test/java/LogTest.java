import cc.mrbird.febs.ResumeBootApplication;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.UserService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ResumeBootApplication.class})
@Slf4j
public class LogTest {

  @Autowired
  private UserService userService;

  @Test
  public void queryAll() {
    String encrypt = MD5Util.encrypt("chenchaoyun", "123456");
    log.info("encrypt {}", encrypt);
    User user = userService.findByName("chency");
    log.info("user {}", JSON.toJSONString(user));
  }

}
