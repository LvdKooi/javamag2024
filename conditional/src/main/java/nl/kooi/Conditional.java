package nl.kooi;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Conditional<S, T> {

    public static <S> Conditional<S, S> of(S value) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public static <S, U> Pair<Predicate<S>, Function<S, U>> applyIf(Predicate<S> condition, Function<S, U> action) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @SafeVarargs
    public final <U> Conditional<S, U> firstMatching(Pair<Predicate<S>, Function<S, U>>... conditionalActions) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public <U> Conditional<S, U> map(Function<T, U> mapFunction) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public <U> Conditional<T, U> flatMap(Function<T, Conditional<T, U>> flatMapFunction) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public T orElse(T defaultValue) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> throwableSupplier) throws X {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public record Pair<S, T>(S key, T value) {

        public Pair {
            Objects.requireNonNull(key);
            Objects.requireNonNull(value);
        }
    }
}