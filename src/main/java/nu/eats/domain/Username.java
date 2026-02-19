package nu.eats.domain;

import nu.eats.core.result.Failure;
import nu.eats.core.result.Result;
import nu.eats.core.result.Success;

import java.util.regex.Pattern;

public final class Username {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,20}$");

    private String value;

    private Username() {
    }

    public static Result<Username, String> create(String value) {
        return new Username().setValue(value);
    }

    public String value() {
        return value;
    }

    public Result<Username, String> setValue(String value) {
        if (!USERNAME_PATTERN.matcher(value).find()) {
            return new Failure<>("Invalid username");
        }

        this.value = value;

        return new Success<>(this);
    }
}
