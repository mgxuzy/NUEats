package nu.eats.domain;

import nu.eats.domain.auth.UserType;

public record Customer(String id, String username) implements User {
    @Override
    public String id() {
        return id;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public UserType type() {
        return UserType.CUSTOMER;
    }
}
