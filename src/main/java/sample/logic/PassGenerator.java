package sample.logic;

import java.util.SplittableRandom;
import java.util.stream.IntStream;

public final class PassGenerator {

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final SplittableRandom splittableRandom = new SplittableRandom();

    public static String generatePassword() {
        char[] result = new char[16];
        char[] alphabet = LETTERS.toCharArray();
        char[] alphabetUpper = LETTERS.toUpperCase().toCharArray();
        char[] specChars = "!@#$%^&*()~{}[]".toCharArray();
        char[] digits = "1234567890".toCharArray();
        char[][] arr = {alphabet, alphabetUpper, specChars, digits};
        int[] requiredElements = {splittableRandom.nextInt(0, 4)
                , splittableRandom.nextInt(4, 8)
                , splittableRandom.nextInt(8, 12)
                , splittableRandom.nextInt(12, 16)};

        IntStream.range(0, 16)
                .forEach(index -> {
                    if (index == requiredElements[0]) {
                        result[index] = alphabet[splittableRandom.nextInt(alphabet.length)];
                        return;
                    }
                    if (index == requiredElements[1]) {
                        result[index] = alphabetUpper[splittableRandom.nextInt(alphabetUpper.length)];
                        return;
                    }
                    if (index == requiredElements[2]) {
                        result[index] = specChars[splittableRandom.nextInt(specChars.length)];
                        return;
                    }
                    if (index == requiredElements[3]) {
                        result[index] = digits[splittableRandom.nextInt(digits.length)];
                        return;
                    }

                    int whichArr = splittableRandom.nextInt(4);
                    char[] seletedArr = arr[whichArr];
                    result[index] = arr[whichArr][splittableRandom.nextInt(seletedArr.length)];
                });

        return String.valueOf(result);
    }
}
