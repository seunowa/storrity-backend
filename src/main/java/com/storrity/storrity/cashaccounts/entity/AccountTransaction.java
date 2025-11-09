/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.entity;

import com.storrity.storrity.cashaccounts.dto.AccountTransactionInstruction;
import com.storrity.storrity.util.entity.MetadataConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

/**
 *
 * @author Seun Owa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "account_transaction")
@Schema(description = "Account transaction response object")
public class AccountTransaction {
    public final static String META_STEPS = "steps";     
    
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)    
    private UUID id;
    private String transactionRef;
    private String description;
    private String performedBy;
    private String tag;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Convert(converter = AccountTransactionStepConverter.class)
    private List<AccountTransactionStep> steps;
    @Convert(converter = MetadataConverter.class)
    private Map<String, Object> metadata;
       
    @PrePersist
    public void prePersist(){        
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }
    
    @PreUpdate
    public void preUpdate(){
        LocalDateTime now = LocalDateTime.now();
        updatedAt = now;
    }
    
    public static AccountTransaction from(AccountTransactionInstruction instruction){
        AccountTransaction at = AccountTransaction.builder()
                .transactionRef(instruction.getTransactionRef())
                .description(instruction.getDescription())
                .performedBy(instruction.getPerformedBy())
                .tag(instruction.getTag())
                .steps(instruction.getSteps())
                .build();
        return at;
    }
}
