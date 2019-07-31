package cc.mrbird.febs.common.promethues;

import cc.mrbird.febs.common.utils.IPUtil;
import cc.mrbird.febs.common.utils.SpringContextUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class RegisterInit {
  public static String DEVOPS_MANAGER = System.getenv().get("DEVOPS_MANAGER");
  public static String DEVOPS_GRAFANA = System.getenv().get("DEVOPS_GRAFANA");

  public static RestTemplate restTemplate = new RestTemplate();


  public static String[] grafana = {"deliver-monitor.json", "jvm.json", "springboot.json"};

  public static void register() {
    try {
      registerPrometheus();
      //registerGrafana();
    } catch (Exception e) {
      log.error("create prometheus or grafana error {}", e);
    }
  }


  public static void registerPrometheus() throws Exception {
    Environment environment = SpringContextUtil.getBean(Environment.class);
    String category = environment.getProperty("spring.application.name");
    String selfAddr = IPUtil.selfHostName() + ":" + environment.getProperty("server.port");
    String url = DEVOPS_MANAGER + "/api/v1/prometheus/" + category + "?metricsPath=prometheus";
    HttpHeaders headers = new HttpHeaders();
    MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
    headers.setContentType(type);
    String entityBody = entityBody(category, selfAddr);
    log.info("entityBody {}", entityBody);
    HttpEntity<String> httpEntity = new HttpEntity<>(entityBody, headers);
    log.info("Prometheus url {}", url);
    ResponseEntity responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
    log.info("registerPrometheus responseEntity {}", JSON.toJSONString(responseEntity));
    if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
      throw new Exception("create Prometheus error response " + responseEntity);
    }
  }

  public static void registerGrafana() throws Exception {
    String url = DEVOPS_GRAFANA.replaceAll("http://", "http://admin:admin@") + "api/dashboards/import";
    HttpHeaders headers = new HttpHeaders();
    MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
    headers.setContentType(type);
    for (String name : grafana) {
      ClassPathResource classPathResource = new ClassPathResource(name);
      String json = IOUtils.toString(classPathResource.getInputStream(), "UTF-8");
      HttpEntity<String> httpEntity = new HttpEntity<>(json, headers);
      log.info("Grafana url {}", url);
      ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
      log.info("registerGrafana responseEntity {}", JSON.toJSONString(responseEntity));
      if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
        throw new Exception("create Grafana error response " + responseEntity);
      }
    }


  }

  public static String entityBody(String category, String selfAddr) {
    return "[{\"labels\": {\"category\": \"" + category + "\"},\"targets\": [\"" + selfAddr + "\"]}]";
  }
}
