package nz.westpac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * There are no shutdown hooks for docker...
 * By default, during a Docker rolling update, Docker will send TERM signal to your container and wait for 10 seconds
 * and then send the KILL signal to finally stop your container.
 *
 * If you need your end-point processes to keep running longer than 10 seconds to complete current requests / housekeeping,
 * one can use this CustomTomcatContainer and configure Dockers stop-grace-period to match.
 * E.g. Here we default to wait 30s, and in this case, one should allow 60 seconds by setting
 * services.app.stop_grace_period in the docker-compose file.
 * @See https://docs.docker.com/compose/compose-file/#stop_grace_period
 * Note also: a Docker container may have been configured  to send SIGINT instead of SIGTERM in its compose-file.
 *
 * The JVM will exit with a code calculated by EXIT-CODE = 128 + SIGNAL-CODE
 * The code for SIGKILL is 9 whereas the code for SIGTERM is 15.
 * So when your microservice has exited with code 137,
 * you know now that it received a kill signal (SIGKILL).
 * With code 143 it received a term signal (SIGTERM).
 */

@Component
public class CustomTomcatContainer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    private static Logger loggger = LoggerFactory.getLogger(CustomTomcatContainer.class);

    @Value("${catalina.threadpool.execution.timeout.seconds}")
    private static Long shutdownTimeoutSeconds = 30L;

    private TomcatGracefulShutdown tomcatGracefulShutdown;

    public CustomTomcatContainer(@Autowired TomcatGracefulShutdown tomcatGracefulShutdown) {
        this.tomcatGracefulShutdown = tomcatGracefulShutdown;
    }

    @PostConstruct
    public void postConstruct() {
        loggger.info("catalina.threadpool.execution.timeout.seconds: {}", shutdownTimeoutSeconds);
    }

    @Bean
    public static TomcatGracefulShutdown gracefulShutdown()  {
        return new TomcatGracefulShutdown(shutdownTimeoutSeconds, TimeUnit.SECONDS);
    }


    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(tomcatGracefulShutdown);
    }


}
