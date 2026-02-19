package nu.eats.domain;

import nu.eats.domain.auth.UserType;

public interface User {
    String id();

    String username();

    UserType type();
}
