package com.sample.dental.smile.dentail.work.flow.authToken;

public class JwtDetailsHolder {

    private static final ThreadLocal<JwtDetails> jwtDetailsThreadLocal = new ThreadLocal<>();

    public static void setJwtDetails(JwtDetails jwtDetails) {
        jwtDetailsThreadLocal.set(jwtDetails);
    }

    public static JwtDetails getJwtDetails() {
        return jwtDetailsThreadLocal.get();
    }

    public static void clear() {
        jwtDetailsThreadLocal.remove();
    }
}

