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
    private final String clientName;

    public AuthenticatedUser(String username, String clientId, String clientName) {
        this.username = username;
        this.clientId = clientId;
        this.clientName = clientName;
    }

    public String getUsername() {
        return username;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }    
}
