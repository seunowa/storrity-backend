/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.storrity.storrity.supply.entity;

/**
 *
 * @author Seun Owa
 */
public enum SupplyAction {
//    Draft and deliver can be done multiple times before transitioning to the next action
//    All other actions can be done only once befor trnsitioning to the next
//    When approval fails the supply is trnsitiond to the previous action
    DRAFT,
    SUBMIT_DRAFT,
    APPROVE_DRAFT,
    ORDER,
    DELIVER,
    SUBMIT_DELIVERY,
    APPROVE_DELIVERY,
    RECEIVE,
    CANCEL;
}