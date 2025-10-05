package com.Fisport.service;

import com.Fisport.common.EFieldStatus;
import com.Fisport.model.Field;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class FieldSpecification {

    public static Specification<Field> filterFields(Long wardId, Long fieldTypeId, EFieldStatus status, String keyword, String username, Long... featureIds) {
        return (Root<Field> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Join<?, ?>  hasFeatureJoin = root.join("fieldHasFeatures");
            Join<?, ?> featureJoin = hasFeatureJoin.join("feature");

            Predicate predicate = cb.conjunction();

            if (wardId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("ward").get("id"), wardId));
            }

            if (fieldTypeId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("fieldType").get("id"), fieldTypeId));
            }

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("fieldStatus"), status));
            }

            if (StringUtils.hasLength(keyword)) {
                String pattern = String.format("%%%s%%", keyword.trim().toLowerCase());
                predicate = cb.and(predicate, cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern)
                        //sth
                ));
            }

            if (StringUtils.hasLength(username)) {
                predicate = cb.and(predicate, cb.equal(root.get("owner").get("username"), username));
            }

            if (featureIds != null && featureIds.length > 0) {
                    predicate = cb.and(predicate, featureJoin.get("id").in((Object[]) featureIds));

                    query.groupBy(root.get("id"));
                    query.having(cb.equal(cb.countDistinct(featureJoin.get("id")), featureIds.length));
            }

            return predicate;
        };
    }
}
