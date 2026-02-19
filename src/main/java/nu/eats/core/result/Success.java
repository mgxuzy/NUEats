package nu.eats.core.result;

public record Success<T, E>(T value) implements Result<T, E> {
    public boolean isSuccess() {
        return true;
    }

    public boolean isFailure() {
        return false;
    }
}
