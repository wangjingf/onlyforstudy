package io.study.gateway.stat;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

public class StatService {
    MetricRegistry metricRegistry = new MetricRegistry();

   public Meter newMeter(String clientName){
       Meter meter1 = new Meter();
       metricRegistry.register("meter1", meter1);

      // Meter meter2 = metricRegistry.meter("meter2");
       return meter1;
   }
}
