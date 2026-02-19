package nu.eats.core.result;

public sealed interface Result<T, E> permits Success, Failure {
    boolean isSuccess();

    boolean isFailure();

    default T value() {
        return switch (this) {
            case Success<T, E> success -> success.value();
            case Failure<T, E> failure -> null;
        };
    }

    default E error() {
        return switch (this) {
            case Success<T, E> success -> null;
            case Failure<T, E> failure -> failure.error();
        };
    }

    default T valueOrNull() {
        return switch (this) {
            case Success<T, E> success -> success.value();
            case Failure<T, E> failure -> null;
        };
    }

    default E errorOr(E value) {
        return switch (this) {
            case Success<T, E> success -> value;
            case Failure<T, E> failure -> failure.error();
        };
    }
}