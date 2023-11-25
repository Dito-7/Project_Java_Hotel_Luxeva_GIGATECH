package DataModel;

import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.Console;
import java.sql.Date;

class InputScanner {
    private static Scanner input = new Scanner(System.in);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat();

    public static int readInt(String prompt) {
        System.out.print(prompt + " : ");
        return Integer.parseInt(input.nextLine());
    }

    public static String readString(String prompt) {
        System.out.print(prompt + " : ");
        return input.nextLine();
    }

    public static Date readDate(String prompt, String format) {
        System.out.print(prompt + " (" + format + "): ");
        String dateString = input.nextLine();
        try {
            dateFormat.applyPattern(format);
            java.util.Date utilDate = dateFormat.parse(dateString);
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            System.out.println("Format tanggal salah. Mohon masukkan tanggal dengan format yang benar.");
            return readDate(prompt, format);
        }
    }

    public static void clearConsole() {
        final String os = System.getProperty("os.name");

        try {
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLoadingAnimation(int duration) {
        String[] animationFrames = { "|", "/", "-", "\\" };

        long startTime = System.currentTimeMillis();
        long endTime = startTime + duration;

        while (System.currentTimeMillis() < endTime) {
            for (String frame : animationFrames) {
                System.out.print("\rLoading " + frame);
                try {
                    Thread.sleep(100); // Adjust the delay between frames here
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.print("\r"); // Clear the loading animation line
    }

    public static String readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword(prompt + " : ");
            return new String(passwordArray);
        } else {
            System.out.println("Warning: Cannot mask password on this environment. Password will be visible.");
            return readString(prompt);
        }
    }
}
