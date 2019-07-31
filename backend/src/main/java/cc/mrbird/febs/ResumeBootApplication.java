package cc.mrbird.febs;

import cc.mrbird.febs.common.promethues.RegisterInit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@Slf4j
public class ResumeBootApplication extends SpringBootServletInitializer
    implements ApplicationListener<ContextRefreshedEvent> {

  public static void main(String args[]) {
    SpringApplication.run(ResumeBootApplication.class, args);
    log.info(">>>>>>>>>>>>>>>>>>>>>> resume-boot 启动成功! <<<<<<<<<<<<<<<<<<<<<<<<<");
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("--->开机服务执行的操作....");
    try {
      RegisterInit.register();
    } catch (Exception e) {
      log.error("onApplicationEvent error", e);
    }
  }
}
