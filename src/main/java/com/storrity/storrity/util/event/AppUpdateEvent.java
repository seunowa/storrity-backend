/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.util.event;

/**
 *
 * @author Seun Owa
 */
public class AppUpdateEvent <T> extends AppEvent<T>{
    
    private T prev;
    
    public AppUpdateEvent(T data, T prev) {
        super(data);
        this.prev = prev;
    }

    public T getPrev() {
        return prev;
    }
    
}
