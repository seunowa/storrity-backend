/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.license.service;

/**
 *
 * @author Seun Owa
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;

public class MachineIdentifier {

    public static String getMachineUUID() {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                return hash(getWindowsMachineGuid());
            } else if (os.contains("mac")) {
                return hash(getMacUUID());
            } else {
                return hash(getLinuxMachineId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get machine UUID", e);
        }
    }

    private static String getWindowsMachineGuid() throws IOException {
        Process p = Runtime.getRuntime().exec(
            "reg query HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Cryptography /v MachineGuid"
        );
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            return br.lines()
                     .filter(line -> line.contains("MachineGuid"))
                     .map(line -> line.split("REG_SZ")[1].trim())
                     .findFirst()
                     .orElseThrow();
        }
    }

    private static String getLinuxMachineId() throws IOException {
        Process p = Runtime.getRuntime().exec("cat /etc/machine-id");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            return br.readLine().trim();
        }
    }

    private static String getMacUUID() throws IOException {
        Process p = Runtime.getRuntime().exec(
            "ioreg -rd1 -c IOPlatformExpertDevice"
        );
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            return br.lines()
                .filter(line -> line.contains("IOPlatformUUID"))
                .map(line -> line.replaceAll(".*= \"|\"", "").trim())
                .findFirst()
                .orElseThrow();
        }
    }

    private static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
