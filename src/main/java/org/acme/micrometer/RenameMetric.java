package org.acme.micrometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.quarkus.runtime.StartupEvent;
import java.util.Collections;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
public class RenameMetric {

  @Inject
  PrometheusMeterRegistry registry;

  void onStart(@Observes StartupEvent ev) {
    registry.config().namingConvention(new NamingMetric());
  }

}
