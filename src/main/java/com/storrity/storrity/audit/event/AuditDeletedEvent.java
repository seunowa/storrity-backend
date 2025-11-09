/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.audit.event;

import com.storrity.storrity.audit.entity.Audit;
import com.storrity.storrity.util.event.AppEvent;

/**
 *
 * @author Seun Owa
 */
public class AuditDeletedEvent extends AppEvent<Audit>{
    public AuditDeletedEvent(Audit data) {
        super(data);
    }
}