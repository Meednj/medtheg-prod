package com.medthegprod.backend.identity.application.port;

import com.medthegprod.backend.identity.domain.model.User;

public interface TokenGenerator {

    String generate(User user);
}