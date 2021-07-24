package io.study.gateway.auth;

import io.netty.handler.codec.http.FullHttpRequest;
import io.study.gateway.config.ApiConfig;

public class SuccessAuthService implements IAuthService{
    @Override
    public boolean isValidRequest(FullHttpRequest request, ApiConfig apiConfig) {
        return true;
    }
}
