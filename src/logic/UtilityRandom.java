package logic;

import java.util.Random;

public class UtilityRandom extends Random {
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
}