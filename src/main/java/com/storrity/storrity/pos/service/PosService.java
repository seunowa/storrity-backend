/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.pos.service;

import com.storrity.storrity.pos.dto.PosCreationDto;
import com.storrity.storrity.pos.dto.PosUpdateDto;
import com.storrity.storrity.pos.entity.Pos;
import com.storrity.storrity.pos.entity.PosQueryParam;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface PosService {
    public Pos create(PosCreationDto dto);
    public Pos fetch(UUID id);
    public List<Pos> list(PosQueryParam params);
    public CountDto count(PosQueryParam params);
    public Pos update(UUID id, PosUpdateDto dto);
    public Pos delete(UUID id);
}
