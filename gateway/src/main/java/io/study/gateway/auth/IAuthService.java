package io.study.gateway.auth;

import io.netty.handler.codec.http.FullHttpRequest;
import io.study.gateway.config.ApiConfig;

public interface IAuthService {
    public boolean isValidRequest(FullHttpRequest request, ApiConfig apiConfig);
}
