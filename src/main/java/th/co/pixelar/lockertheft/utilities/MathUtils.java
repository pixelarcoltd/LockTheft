package th.co.pixelar.lockertheft.utilities;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
    public static boolean chanceOf(int percent) {
        return Math.random() <= ((float) percent / 100);
    }

    public static int randomBetweenInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
