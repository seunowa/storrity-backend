/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.entity;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Seun Owa
 */
@Data
public class AppUserQueryParams {
    private List<String> usernames;
    private List<String> roles;
    //@Todo add query params for pwdUpdatedAt and pwdChange Required asio implement in CustomRepositoryImpl
    @Size(min = 2, max = 2, message = "createdAtRange must contain exactly two dates")
    private List<LocalDateTime> createdAtRange;
    @Size(min = 2, max = 2, message = "updatedAtRange must contain exactly two dates")
    private List<LocalDateTime> updatedAtRange;
    private Integer offset;
    private Integer limit;
    private String searchPhrase;
    private String sort;
}
