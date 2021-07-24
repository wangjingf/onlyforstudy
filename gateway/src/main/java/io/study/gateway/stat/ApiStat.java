package io.study.gateway.stat;

import com.codahale.metrics.Meter;
import io.study.gateway.config.ApiConfig;

public class ApiStat extends ApiConfig {
    Meter meter;
}
