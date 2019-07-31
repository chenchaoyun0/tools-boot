package cc.mrbird.febs.common.promethues;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.MetricsServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(CollectorRegistry.class)
public class PrometheusConfiguration {


  @Bean
  @ConditionalOnMissingBean
  public CollectorRegistry metricRegister() {
    return CollectorRegistry.defaultRegistry;
  }

  @Bean
  public ServletRegistrationBean registerPrometheusExporterServlet(CollectorRegistry metricRegister) {
    return new ServletRegistrationBean(new MetricsServlet(metricRegister), "/prometheus");
  }


}
