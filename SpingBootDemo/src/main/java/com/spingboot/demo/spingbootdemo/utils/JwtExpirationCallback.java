package com.spingboot.demo.spingbootdemo.utils;

public interface JwtExpirationCallback {
    void onJwtTokenExpired(String expiredToken);
}
