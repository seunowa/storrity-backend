/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.pos.repository;

import com.storrity.storrity.pos.entity.Pos;
import com.storrity.storrity.pos.entity.PosQueryParam;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface PosRepositoryCustom {
    public List<Pos> list(PosQueryParam params);
    public Long countRecords (PosQueryParam params);
    
}
