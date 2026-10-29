/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.cart.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Schema(description = "Change payment for individual payment method/channe")
public class ChangeDto {   
    @NotNull
    private UUID paymentChannelId;
    @NotNull
    private Money amount;
//    private PaymentStatus paymentStatus; //At present all checkout must be paid but I left this should the approach be chaned in the future
    private String paymentReference;//optional
    
}
