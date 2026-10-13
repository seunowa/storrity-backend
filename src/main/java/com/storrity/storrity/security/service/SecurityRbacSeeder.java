/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.security.service;

import com.storrity.storrity.security.entity.UserPermission;
import com.storrity.storrity.security.entity.UserRole;
import com.storrity.storrity.security.repository.UserPermissionRepository;
import com.storrity.storrity.security.repository.UserRoleRepository;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
/** Idempotently seeds the permission registry and required system roles. */
@Component
public class SecurityRbacSeeder implements ApplicationRunner {
    private final UserPermissionRepository permissionsRepository;
    private final UserRoleRepository rolesRepository;
    public SecurityRbacSeeder(UserPermissionRepository permissionsRepository, UserRoleRepository rolesRepository) { this.permissionsRepository = permissionsRepository; this.rolesRepository = rolesRepository; }

    @Override @Transactional
    public void run(ApplicationArguments args) {
        Map<String, String> catalog = new LinkedHashMap<>();
        catalog.put("BUDGET_VIEW", "View budget information retained from the existing permission catalogue");
        catalog.put("BUDGET_APPROVE", "Approve budgets retained from the existing permission catalogue");
        catalog.put("USER_READ", "List, fetch and count users");
        catalog.put("USER_CREATE", "Create users");
        catalog.put("USER_DELETE", "Delete users");
        catalog.put("USER_ROLE_ASSIGN", "Assign or replace user roles");
        catalog.put("USER_SUPER_ADMIN_ASSIGN", "Assign the Super Admin role to a user");
        catalog.put("USER_PASSWORD_CHANGE", "Change own password");
        catalog.put("USER_PASSWORD_RESET", "Reset another user password");
        catalog.put("USER_PROFILE_READ", "Read user profiles");
        catalog.put("USER_PROFILE_UPDATE", "Update user profiles");
        catalog.put("ROLE_READ", "Read roles");
        catalog.put("ROLE_CREATE", "Create roles");
        catalog.put("ROLE_UPDATE", "Update role details and permissions");
        catalog.put("ROLE_DELETE", "Delete eligible custom roles");
        catalog.put("PERMISSION_READ", "Read the permission catalogue");
        catalog.put("PRODUCT_READ", "Read products and product lookup data");
        catalog.put("PRODUCT_CREATE", "Create products");
        catalog.put("PRODUCT_UPDATE", "Update products");
        catalog.put("PRODUCT_DELETE", "Delete products");
        catalog.put("PRODUCT_IMPORT", "Import products");
        catalog.put("PRODUCT_BATCH_CREATE", "Create products in batches");
        catalog.put("PRODUCT_EXPORT", "Export products");
        catalog.put("STOCK_MOVEMENT_READ", "Read stock movements");
        catalog.put("STOCK_MOVEMENT_CREATE", "Record stock movements");
        catalog.put("INVENTORY_REPORT_READ", "Read inventory reports");
        catalog.put("INVENTORY_RECONCILIATION_READ", "Read inventory reconciliation reports");
        catalog.put("SUPPLY_READ", "Read supplies");
        catalog.put("SUPPLY_DRAFT_CREATE", "Create supply drafts");
        catalog.put("SUPPLY_DRAFT_UPDATE", "Update supply drafts");
        catalog.put("SUPPLY_DRAFT_DELETE", "Delete supply drafts");
        catalog.put("SUPPLY_DRAFT_SUBMIT", "Submit supply drafts");
        catalog.put("SUPPLY_DRAFT_APPROVE", "Approve supply drafts");
        catalog.put("SUPPLY_ORDER", "Place supply orders");
        catalog.put("SUPPLY_DELIVERY_RECORD", "Record supply delivery");
        catalog.put("SUPPLY_DELIVERY_SUBMIT", "Submit delivery for approval");
        catalog.put("SUPPLY_DELIVERY_APPROVE", "Approve supply delivery");
        catalog.put("SUPPLY_RECEIVE", "Receive supplies into inventory");
        catalog.put("SUPPLY_CANCEL", "Cancel supplies");
        catalog.put("SUPPLY_REPORT_READ", "Read supply reports");
        catalog.put("SUPPLY_SETTINGS_READ", "Read supply process settings");
        catalog.put("SUPPLY_SETTINGS_UPDATE", "Update supply process settings");
        catalog.put("STOCK_TRANSFER_READ", "Read stock transfers");
        catalog.put("STOCK_TRANSFER_DRAFT_CREATE", "Create stock transfer drafts");
        catalog.put("STOCK_TRANSFER_DRAFT_UPDATE", "Update stock transfer drafts");
        catalog.put("STOCK_TRANSFER_DRAFT_DELETE", "Delete stock transfer drafts");
        catalog.put("STOCK_TRANSFER_DRAFT_SUBMIT", "Submit stock transfers");
        catalog.put("STOCK_TRANSFER_DRAFT_APPROVE", "Approve stock transfer drafts");
        catalog.put("STOCK_TRANSFER_SEND", "Send stock transfers");
        catalog.put("STOCK_TRANSFER_RECEIPT_SUBMIT", "Submit stock transfer receipt");
        catalog.put("STOCK_TRANSFER_RECEIPT_APPROVE", "Approve stock transfer receipt");
        catalog.put("STOCK_TRANSFER_RECEIVE", "Receive stock transfers");
        catalog.put("STOCK_TRANSFER_CANCEL", "Cancel stock transfers");
        catalog.put("STOCK_TRANSFER_REPORT_READ", "Read stock transfer and movement reports");
        catalog.put("STOCK_TRANSFER_SETTINGS_READ", "Read stock transfer settings");
        catalog.put("STOCK_TRANSFER_SETTINGS_UPDATE", "Update stock transfer settings");
        catalog.put("SALE_READ", "Read sales");
        catalog.put("SALE_CREATE", "Create sales");
        catalog.put("SALE_REPORT_READ", "Read sales reports");
        catalog.put("SALE_RETURN_READ", "Read sales returns");
        catalog.put("SALE_RETURN_CREATE", "Create sales returns");
        catalog.put("POS_READ", "Read POS terminals");
        catalog.put("POS_CREATE", "Create POS terminals");
        catalog.put("POS_UPDATE", "Update POS terminals");
        catalog.put("POS_DELETE", "Delete POS terminals");
        catalog.put("CART_READ", "Read carts");
        catalog.put("CART_CREATE", "Create carts");
        catalog.put("CART_UPDATE", "Update carts and cart items");
        catalog.put("CART_DELETE", "Delete carts and cart items");
        catalog.put("CHECKOUT_READ", "Read checkouts");
        catalog.put("CHECKOUT_CREATE", "Create checkouts");
        catalog.put("SALES_ATTENDANT_READ", "Read sales attendants");
        catalog.put("SALES_ATTENDANT_CREATE", "Create sales attendants");
        catalog.put("SALES_ATTENDANT_UPDATE", "Update sales attendants");
        catalog.put("SALES_ATTENDANT_DELETE", "Delete sales attendants");
        catalog.put("STORE_READ", "Read stores");
        catalog.put("STORE_CREATE", "Create stores");
        catalog.put("STORE_UPDATE", "Update stores");
        catalog.put("STORE_DELETE", "Delete stores");
        catalog.put("CUSTOMER_READ", "Read customers");
        catalog.put("CUSTOMER_CREATE", "Create customers");
        catalog.put("CUSTOMER_UPDATE", "Update customers");
        catalog.put("CUSTOMER_DELETE", "Delete customers");
        catalog.put("CLIENT_SYSTEM_READ", "Read client systems");
        catalog.put("CLIENT_SYSTEM_CREATE", "Create client systems");
        catalog.put("CLIENT_SYSTEM_UPDATE", "Update client systems");
        catalog.put("CLIENT_SYSTEM_DELETE", "Delete client systems");
        catalog.put("CASH_ACCOUNT_READ", "Read cash accounts");
        catalog.put("CASH_ACCOUNT_CREATE", "Create cash accounts");
        catalog.put("CASH_ACCOUNT_UPDATE", "Update cash accounts");
        catalog.put("CASH_ACCOUNT_DELETE", "Delete cash accounts");
        catalog.put("CASH_ACCOUNT_PARENT_UPDATE", "Change cash account parent");
        catalog.put("ACCOUNT_TRANSACTION_READ", "Read account transactions");
        catalog.put("ACCOUNT_TRANSACTION_CREATE", "Create account transactions");
        catalog.put("ACCOUNT_STATEMENT_READ", "Read account statements");
        catalog.put("STOCK_MOVEMENT_REPORT_READ", "Read stock movement reports");
        catalog.put("AUDIT_READ", "Read audit records");
        catalog.put("AUDIT_DELETE", "Delete audit records");
        catalog.put("AI_STATUS_READ", "Read AI provider status");
        catalog.put("AI_PROVIDER_READ", "Read supported AI providers");
        catalog.put("AI_PROVIDER_CONFIGURE", "Configure AI provider details");
        catalog.put("AI_PROVIDER_ACTIVATE", "Activate an AI provider");
        catalog.put("AI_REPORT_GENERATE", "Generate AI reports");
        catalog.put("AI_BUSINESS_CONTEXT_READ", "Read AI business context");
        catalog.put("AI_BUSINESS_CONTEXT_UPDATE", "Update AI business context");
        catalog.put("DATABASE_BACKUP", "Create database backups");
        catalog.put("LICENSE_READ", "Read license information");
//        catalog.put("LICENSE_VALIDATE", "Validate a license file");
//        catalog.put("LICENSE_IMPORT", "Import a license file");
//        catalog.put("LICENSE_CLIENT_REQUEST", "Request an additional licensed client");
//        catalog.put("LICENSE_CLIENT_STATUS_READ", "Read client license status");
//        catalog.put("COUNTRY_READ", "Read country lookup data");
        catalog.put("COUNTRY_CONFIG_UPDATE", "Change selected country");
//        catalog.put("AUTH_ROOT_USER_STATUS_READ", "Check root user initialization status");
//        catalog.put("AUTH_ROOT_USER_INITIALIZE", "Initialize root user");
        catalog.forEach((id, description) -> permissionsRepository.findById(id).orElseGet(() -> permissionsRepository.save(new UserPermission(id, description))));
        Set<String> all = catalog.keySet();
        seedRole("SUPER_ADMIN", all);
        Set<String> admin = new LinkedHashSet<>(all);
//        admin.removeAll(Set.of("AUTH_ROOT_USER_INITIALIZE", "DATABASE_BACKUP", "LICENSE_IMPORT", "LICENSE_CLIENT_REQUEST", "COUNTRY_CONFIG_UPDATE", "USER_SUPER_ADMIN_ASSIGN"));
        admin.removeAll(Set.of("DATABASE_BACKUP", "COUNTRY_CONFIG_UPDATE", "USER_SUPER_ADMIN_ASSIGN"));
        seedRole("ADMIN", admin);
        seedRole("STORE_KEEPER", Set.of("PRODUCT_READ", "PRODUCT_CREATE", "PRODUCT_UPDATE", "PRODUCT_IMPORT", "PRODUCT_EXPORT", "STOCK_MOVEMENT_READ", "STOCK_MOVEMENT_CREATE", "INVENTORY_REPORT_READ", "INVENTORY_RECONCILIATION_READ", "SUPPLY_READ", "SUPPLY_DRAFT_CREATE", "SUPPLY_DRAFT_UPDATE", "SUPPLY_DRAFT_SUBMIT", "SUPPLY_ORDER", "SUPPLY_DELIVERY_RECORD", "SUPPLY_DELIVERY_SUBMIT", "SUPPLY_RECEIVE", "SUPPLY_REPORT_READ", "STOCK_TRANSFER_READ", "STOCK_TRANSFER_DRAFT_CREATE", "STOCK_TRANSFER_DRAFT_UPDATE", "STOCK_TRANSFER_DRAFT_SUBMIT", "STOCK_TRANSFER_SEND", "STOCK_TRANSFER_RECEIPT_SUBMIT", "STOCK_TRANSFER_RECEIVE", "STOCK_TRANSFER_REPORT_READ", "STORE_READ", "CUSTOMER_READ", "SALE_READ", "SALE_REPORT_READ"));
        seedRole("ATTENDANT", Set.of("PRODUCT_READ", "STORE_READ", "CUSTOMER_READ", "CUSTOMER_CREATE", "CUSTOMER_UPDATE", "POS_READ", "CART_READ", "CART_CREATE", "CART_UPDATE", "CART_DELETE", "CHECKOUT_READ", "CHECKOUT_CREATE", "SALE_READ", "SALE_CREATE", "SALES_ATTENDANT_READ", "SALE_RETURN_READ"));
    }

    private void seedRole(String id, Set<String> permissionIds) {
        UserRole existing = rolesRepository.findById(id).orElse(null);
        if (existing != null && !"SUPER_ADMIN".equals(id)) {
            existing.setSystemRole(true);
            rolesRepository.save(existing);
            return; // preserve administrator edits to default role permissions
        }
        UserRole role = existing == null ? new UserRole() : existing;
        role.setId(id);
        role.setSystemRole(true);
        Set<UserPermission> permissions = permissionsRepository.findAllById(permissionIds).stream().collect(Collectors.toCollection(LinkedHashSet::new));
        role.setPermissions(permissions);
        rolesRepository.save(role);
    }
}
