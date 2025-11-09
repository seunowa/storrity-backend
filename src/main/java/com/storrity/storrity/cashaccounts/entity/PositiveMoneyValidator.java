/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 *
 * @author Seun Owa
 */
public class PositiveMoneyValidator implements ConstraintValidator<PositiveMoney, Money> {

    @Override
    public boolean isValid(Money value, ConstraintValidatorContext context) {
        if (value == null) return true;
        if (value.getValueInMicroNaira() <= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Amount must be greater than zero"
            ).addConstraintViolation();
            return false;
        }
        return true;
        }
}
