package com.restaurant.util;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Scanner;

public class Util {

    private static final Scanner SC = new Scanner(System.in, StandardCharsets.UTF_8);

    // ── 密码 ─────────────────────────────────────────
    public static String sha256(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ── 输入工具 ──────────────────────────────────────
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return SC.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int v = Integer.parseInt(SC.nextLine().trim());
                return v;
            } catch (NumberFormatException e) {
                System.out.println("  ✗ 请输入整数");
            }
        }
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            int v = readInt(prompt);
            if (v >= min && v <= max) return v;
            System.out.printf("  ✗ 请输入 %d ~ %d 之间的整数%n", min, max);
        }
    }

    public static BigDecimal readDecimal(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return new BigDecimal(SC.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  ✗ 请输入有效的数字");
            }
        }
    }

    // ── 显示工具 ──────────────────────────────────────
    public static void printLine() {
        System.out.println("  ─────────────────────────────────────────");
    }

    public static void printTitle(String title) {
        System.out.println();
        System.out.println("  ╔═══════════════════════════════════════╗");
        System.out.printf("  ║  %-37s║%n", title);
        System.out.println("  ╚═══════════════════════════════════════╝");
    }

    public static void success(String msg) { System.out.println("  ✔ " + msg); }
    public static void error(String msg)   { System.out.println("  ✗ " + msg); }
    public static void info(String msg)    { System.out.println("  → " + msg); }
}
