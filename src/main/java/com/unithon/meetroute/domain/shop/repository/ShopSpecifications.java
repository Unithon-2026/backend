package com.unithon.meetroute.domain.shop.repository;

import com.unithon.meetroute.domain.priority.entity.PriorityGrade;
import com.unithon.meetroute.domain.shop.entity.Shop;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ShopSpecifications {

    private ShopSpecifications() {
    }

    public static Specification<Shop> hasGu(String gu) {
        if (!StringUtils.hasText(gu)) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("gu"), gu);
    }

    public static Specification<Shop> hasBusinessType(String businessType) {
        if (!StringUtils.hasText(businessType)) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("businessType"), businessType);
    }

    public static Specification<Shop> hasPriorityGrade(PriorityGrade priorityGrade) {
        if (priorityGrade == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("priorityGrade"), priorityGrade);
    }
}
