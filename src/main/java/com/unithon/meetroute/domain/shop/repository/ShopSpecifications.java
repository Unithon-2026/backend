package com.unithon.meetroute.domain.shop.repository;

import com.unithon.meetroute.domain.priority.entity.PriorityGrade;
import com.unithon.meetroute.domain.shop.entity.Shop;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class ShopSpecifications {

    private ShopSpecifications() {
    }

    public static Specification<Shop> hasGu(String gu) {
        if (!StringUtils.hasText(gu)) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("gu"), gu);
    }

    public static Specification<Shop> hasBusinessType(String businessType) {
        if (!StringUtils.hasText(businessType)) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("businessType"), businessType);
    }

    public static Specification<Shop> hasPriorityGrade(PriorityGrade priorityGrade) {
        if (priorityGrade == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("priorityGrade"), priorityGrade);
    }

    public static Specification<Shop> inBoundingBox(BigDecimal minLatitude, BigDecimal maxLatitude,
                                                      BigDecimal minLongitude, BigDecimal maxLongitude) {
        if (minLatitude == null || maxLatitude == null || minLongitude == null || maxLongitude == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.and(
                cb.between(root.get("latitude"), minLatitude, maxLatitude),
                cb.between(root.get("longitude"), minLongitude, maxLongitude)
        );
    }
}
