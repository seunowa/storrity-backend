/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.salesattendant.service;

import com.storrity.storrity.salesattendant.dto.SalesAttendantCreationDto;
import com.storrity.storrity.salesattendant.dto.SalesAttendantUpdateDto;
import com.storrity.storrity.salesattendant.entity.SalesAttendant;
import com.storrity.storrity.salesattendant.entity.SalesAttendantQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface SalesAttendantService {
    public SalesAttendant create(SalesAttendantCreationDto dto);
    public SalesAttendant fetch(UUID id);
    public SalesAttendant fetchByUsername(String username);
    public List<SalesAttendant> list(SalesAttendantQueryParams params);
    public CountDto count(SalesAttendantQueryParams params);
    public SalesAttendant update(UUID id, SalesAttendantUpdateDto dto);
    public SalesAttendant delete(UUID id);
}
