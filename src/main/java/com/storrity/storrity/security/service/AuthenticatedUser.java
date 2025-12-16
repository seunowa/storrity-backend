/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

/**
 *
 * @author Seun Owa
 */
public class AuthenticatedUser {

    private final String username;
    private final String clientId;

    public AuthenticatedUser(String username, String clientId) {
        this.username = username;
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public String getClientId() {
        return clientId;
    }
}
