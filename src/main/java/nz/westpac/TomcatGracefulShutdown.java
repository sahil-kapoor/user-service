package nz.westpac;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class TomcatGracefulShutdown implements TomcatConnectorCustomizer {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private Long shutdownTimeout;
    private TimeUnit unit;
    private volatile Connector connector = null;

    public TomcatGracefulShutdown(Long shutdownTimeout, TimeUnit unit) {
        this.shutdownTimeout = shutdownTimeout;
        this.unit = unit;
    }

    @Override
    public void customize(Connector connector) {
        LOG.info("TomcatConnectorCustomizer is active on {}", connector.toString());
        this.connector = connector;
    }

    @EventListener
    public void handleOnContextStartedEvent(ContextStartedEvent contextStartedEvent) {
        LOG.info("ContextStartedEvent received, @EventListener registration is working.");
    }

    @EventListener
    public void handleOnContextClosedEvent(ContextClosedEvent contextClosedEvent) {
        LOG.warn("ContextClosedEvent received, preparing for shutdown.");
        if (this.connector == null) {
            return;
        }
        awaitTermination(this.connector);
    }

    private void awaitTermination(Connector connector) {
        connector.pause(); // pause accepting new requests
        Executor executor = connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpx = (ThreadPoolExecutor) executor;
            LOG.warn("Context Closed Event. Awaiting termination for {} {}.", shutdownTimeout, unit);
            LOG.warn("JVM shutdown: Await Tomcat to be finished with pending requests.");
            try {
                tpx.shutdown();
                if (!tpx.awaitTermination(shutdownTimeout, unit)) {
                    LOG.warn("Tomcat ThreadPool did not shut down gracefully. Container Forcefully Proceeding with Shutdown.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            LOG.error("Unrecognized executor: " + executor.getClass());
        }
    }
}
