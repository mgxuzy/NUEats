package nu.eats.domain;

import nu.eats.core.result.Failure;
import nu.eats.core.result.Result;
import nu.eats.core.result.Success;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public final class Password {
    private static final Pattern UPPERCASE_LETTER_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    private String value;

    private Password(String value) {
        this.value = value;
    }

    public static Result<Password, List<String>> create(String value) {
        var violations = findViolations(value);

        return violations == null ? new Success<>(new Password(value)) : new Failure<>(violations);
    }

    private static List<String> findViolations(String value) {
        if (value == null || value.isBlank()) {
            return List.of("Password is required");
        }

        List<String> violations = null;

        if (value.length() < 8) {
            violations = new ArrayList<>();

            violations.add("Must be 8+ characters");
        }

        if (!UPPERCASE_LETTER_PATTERN.matcher(value).find()) {
            violations = violations == null ? new ArrayList<>() : violations;

            violations.add("Must contain an uppercase letter");
        }

        if (!DIGIT_PATTERN.matcher(value).find()) {
            violations = violations == null ? new ArrayList<>() : violations;

            violations.add("Must contain a special character (!@#$%^&*(),.?\":{}|<>)");
        }

        if (!SPECIAL_CHARACTER_PATTERN.matcher(value).find()) {
            violations = violations == null ? new ArrayList<>() : violations;

            violations.add("Must contain special char");
        }

        return Collections.unmodifiableList(violations);
    }

    public String value() {
        return value;
    }

    public Result<Void, List<String>> setValue(String value) {
        var violations = findViolations(value);

        if (violations == null) {
            this.value = value;

            return new Success<>(null);
        } else {
            return new Failure<>(Collections.unmodifiableList(violations));
        }
    }
}
