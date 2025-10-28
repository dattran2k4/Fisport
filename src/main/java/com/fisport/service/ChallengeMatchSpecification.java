package com.fisport.service;

import com.fisport.common.EChallengeStatus;
import com.fisport.common.ELevel;
import com.fisport.model.ChallengeMatch;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.*;

public class ChallengeMatchSpecification {

    private ChallengeMatchSpecification() {
    }

    public static Specification<ChallengeMatch> filterChallengeMatch(EChallengeStatus status, ELevel level, String matchType,
                                                                     LocalDate date, BigDecimal fee, Long cityId, Long fieldTypeId) {

        return (Root<ChallengeMatch> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Join<?, ?> bookingJoin = root.join("booking");
            Join<?, ?> subFieldJoin = bookingJoin.join("subfield");
            Join<?, ?> fieldJoin = subFieldJoin.join("field");
            Join<?, ?> wardJoin = fieldJoin.join("ward");
            Join<?, ?> cityJoin = wardJoin.join("city");
            Join<?, ?> matchTypeJoin = root.join("challengeMatchType");

            Predicate predicate = cb.conjunction();

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }

            if (level != null) {
                predicate = cb.and(predicate, cb.equal(root.get("suggestedLevel"), level));
            }

            if (matchType != null) {
                predicate = cb.and(predicate, cb.equal(matchTypeJoin.get("name"), matchType));
            }

            if (date != null) {
                predicate = cb.and(predicate, cb.equal(bookingJoin.get("bookingDate"), date));
            }

            if (fee != null) {
                predicate = cb.and(predicate, cb.equal(root.get("participationFee"), fee));
            }

            if (cityId != null) {
                predicate = cb.and(predicate, cb.equal(cityJoin.get("id"), cityId));
            }

            if (fieldTypeId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("sportId"), fieldTypeId));
            }

            return predicate;
        };
    }
}
