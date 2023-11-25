/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gigatech_semester2;

import connectMYSQL.config;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.io.Console;

import DataModel.method;
import DataModel.user;

/**
 *
 * @author Dito Aditya Nugroho
 */
public class GiGATECH_semester2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        config db = new config();
        method met = new method();
        menuUtama Menu = new menuUtama();

        InputScanner.clearConsole();
        InputScanner.showLoadingAnimation(1000);

        boolean loginLoop = true;

        while (loginLoop) {
            System.out.println("==============================================");
            System.out.println("------- Selamat Datang di Luxeva Hotel -------");
            System.out.println("---------------- LUXEVA HOTEL ----------------");
            System.out.println("==============================================");
            System.out.println("-- Menu --");
            System.out.println("1. Masuk/Login");
            System.out.println("2. Daftar");
            System.out.println("3. EXIT");

            int menuLogin = InputScanner.readInt("Masukkan Menu");

            if (menuLogin == 1) {
                String name = InputScanner.readString("Masukkan Username");
                String password = InputScanner.readPassword("Masukkan Password");

                boolean LoginGuest = db.checkLoginGuest(name, password);
                boolean LoginOperator = db.checkLoginOperator(name, password);

                if (LoginGuest) {
                    InputScanner.clearConsole();
                    InputScanner.showLoadingAnimation(1000);

                    System.out.println("Login berhasil!\n");
                    System.out.println("Guest\n");

                    Menu.menuGuest(name, db.getUsers_id());

                } else if (LoginOperator) {
                    InputScanner.clearConsole();
                    InputScanner.showLoadingAnimation(1000);

                    System.out.println("Login berhasil!\n");
                    System.out.println("Operator\n");

                    Menu.MenuOperator(name, db.getUsers_id());
                } else {
                    System.out.println(
                            "Login gagal!, Sepertinya akun anda salah atau akun anda sudah tidak aktif. Segera Hubungi ADMIN");
                }

            } else if (menuLogin == 2) {
                met.RegisterGuest();

                System.out.println("Berhasil Daftar, Silahkan Login Terlebih Dahulu");
            } else if (menuLogin == 3) {
                System.exit(0);
            } else {
                loginLoop = true;
                InputScanner.clearConsole();
                System.out.println("Pilih Dengan Benar");
            }
        }
    }
}

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