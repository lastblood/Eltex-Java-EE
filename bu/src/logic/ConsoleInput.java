package logic;

import java.util.Arrays;
import java.util.Scanner;

public class ConsoleInput implements DataInput {
    private static Scanner scan = new Scanner(System.in);
    private String[] hints;

    public ConsoleInput(String[] hints) {
        this.hints = hints;
    }

    @Override
    public String[] input() {
        String[] result = new String[hints.length];

        for (int i = 0; i < hints.length; i++)
            result[i] = readWithHint(hints[i]);

        return result;
    }

    private synchronized String readWithHint(String hint) {
        System.out.print("Enter " + hint + ": ");
        return scan.nextLine(); //TODO: check scan.next() method
    }
}