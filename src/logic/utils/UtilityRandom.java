package logic.utils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UtilityRandom extends Random {
    public static final UtilityRandom instance = new UtilityRandom();

    public String nextString(int minLength, int maxLength) {
        return nextStringFixed(nextInt(maxLength-minLength+1) + minLength);
    }

    public String nextStringFixed(int length) {
        return new String(ints().limit(length)
                .map(x -> Math.abs(x%25) + 0x61).toArray(),
           0, length);
    }

    public int nextInt(int fromInclusive, int toInclusive) {
        return nextInt(toInclusive - fromInclusive + 1) + fromInclusive;
    }

    public <T> List<T> nextElementsFrom(List<T> source, int length) {
        return IntStream.range(0, length).mapToObj(f -> source.get(nextInt(source.size()))).collect(Collectors.toList());
    }

    public Duration nextDuration(int fromSeconds, int toSeconds) {
        return Duration.ofSeconds(nextInt(fromSeconds, toSeconds));
    }
}