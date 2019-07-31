package cc.mrbird.febs.common.aspect;

import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.HttpContextUtil;
import cc.mrbird.febs.common.utils.IPUtil;
import cc.mrbird.febs.system.domain.SysLog;
import cc.mrbird.febs.system.service.LogService;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.Manufacturer;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.RenderingEngine;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * AOP 记录用户操作日志
 *
 * @author MrBird
 * @link https://mrbird.cc/Spring-Boot-AOP%20log.html
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

  @Autowired
  private FebsProperties febsProperties;

  @Autowired
  private LogService logService;

  @Pointcut("@annotation(cc.mrbird.febs.common.annotation.Log)")
  public void pointcut() {
    // do nothing
  }

  @Around("pointcut()")
  public Object around(ProceedingJoinPoint point) throws Throwable {
    Object result = null;
    long beginTime = System.currentTimeMillis();
    // 执行方法
    result = point.proceed();
    // 获取 request
    HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
    // 设置 IP 地址
    String ip = IPUtil.getIpAddr(request);
    // 执行时长(毫秒)
    long time = System.currentTimeMillis() - beginTime;
    if (febsProperties.isOpenAopLog()) {
      // 保存日志
      String token = (String) SecurityUtils.getSubject().getPrincipal();
      String username = "";
      if (StringUtils.isNotBlank(token)) {
        username = JWTUtil.getUsername(token);
      }

      SysLog log = new SysLog();
      log.setUsername(StringUtils.isBlank(username) ? "unknown" : username);
      log.setIp(ip);
      log.setTime(time);

      log.setPath(request.getRequestURI());
      //
      String agentStr = request.getHeader("user-agent");
      UserAgent agent = UserAgent.parseUserAgentString(agentStr);
      // 浏览器
      Browser browser = agent.getBrowser();
      // 浏览器版本
      Version version = agent.getBrowserVersion() == null ? new Version("未知", "未知", "未知") : agent.getBrowserVersion();
      // 系统
      OperatingSystem os = agent.getOperatingSystem();
      /**
       * 保存字段
       */
      // 浏览器类型
      BrowserType browserType = browser.getBrowserType();
      // 浏览器名称和版本
      String browserAndVersion = String.format("%s-%s", browser.getGroup().getName(), version.getVersion());
      // 浏览器厂商
      Manufacturer manufacturer = browser.getManufacturer();
      // 浏览器引擎
      RenderingEngine renderingEngine = browser.getRenderingEngine();
      // 系统名称
      String sysName = os.getName();
      // 产品系列
      OperatingSystem operatingSystem = os.getGroup();
      // 生成厂商
      Manufacturer sysManufacturer = os.getManufacturer();
      // 设备类型
      DeviceType deviceType = os.getDeviceType();

      // 浏览器信息
      log.setUserAgent(agentStr);
      log.setBrowserAndVersion(browserAndVersion);
      log.setBrowserType(browserType.name());
      log.setManufacturer(manufacturer.name());
      log.setRenderingEngine(renderingEngine.name());
      log.setSysName(sysName);
      log.setOperatingSystem(operatingSystem.name());
      log.setSysManufacturer(sysManufacturer.name());
      log.setDeviceType(deviceType.name());

      logService.saveLog(point, log);
    }
    return result;
  }
}
