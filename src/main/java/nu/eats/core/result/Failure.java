package nu.eats.core.result;

public record Failure<T, E>(E error) implements Result<T, E> {
    public boolean isSuccess() {
        return false;
    }

    public boolean isFailure() {
        return true;
    }
}
