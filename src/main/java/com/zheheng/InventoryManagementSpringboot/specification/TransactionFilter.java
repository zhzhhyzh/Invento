package com.zheheng.InventoryManagementSpringboot.specification;

import com.zheheng.InventoryManagementSpringboot.models.Transaction;
import com.zheheng.InventoryManagementSpringboot.models.User;
import com.zheheng.InventoryManagementSpringboot.models.Supplier;
import com.zheheng.InventoryManagementSpringboot.models.Product;
import com.zheheng.InventoryManagementSpringboot.models.Category;

import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class TransactionFilter {

    public static Specification<Transaction> byFilter(String searchValue) {
        return (root, query, criteriaBuilder) -> {
            if (searchValue == null || searchValue.trim().isEmpty()) {
                // No filter provided — match all
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + searchValue.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            // --- Transaction fields ---
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("note")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("transactionType").as(String.class)), searchPattern));

            // --- User join (LEFT) ---
            Join<Transaction, User> userJoin = root.join("user", JoinType.LEFT);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("name")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("email")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("phoneNumber")), searchPattern));

            // --- Supplier join (LEFT) ---
            Join<Transaction, Supplier> supplierJoin = root.join("supplier", JoinType.LEFT);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(supplierJoin.get("name")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(supplierJoin.get("contact")), searchPattern));

            // --- Product join (LEFT) ---
            Join<Transaction, Product> productJoin = root.join("product", JoinType.LEFT);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("name")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("sku")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("description")), searchPattern));

            // --- Product category join (LEFT) ---
            Join<Product, Category> categoryJoin = productJoin.join("category", JoinType.LEFT);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("name")), searchPattern));

            // --- Combine all predicates with OR ---
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Transaction> byMonthAndYear(int month, int year) {
        //Use the month and year functions on the createdAt date field
        return ((root, query, criteriaBuilder) -> {
            Expression<Integer> monthExpression = criteriaBuilder.function("month", Integer.class, root.get("createdAt"));
            Expression<Integer> yearExpression = criteriaBuilder.function("year", Integer.class, root.get("createdAt"));

            Predicate monthPredicates = criteriaBuilder.equal(monthExpression, month);
            Predicate yearPredicates = criteriaBuilder.equal(yearExpression, year);

            return criteriaBuilder.and(monthPredicates, yearPredicates);

        });
    }
}
