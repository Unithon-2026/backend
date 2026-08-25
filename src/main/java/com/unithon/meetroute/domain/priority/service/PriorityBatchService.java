package com.unithon.meetroute.domain.priority.service;

import com.unithon.meetroute.domain.priority.dto.PriorityBatchResponse;
import com.unithon.meetroute.domain.priority.entity.Priority;
import com.unithon.meetroute.domain.priority.repository.PriorityRepository;
import com.unithon.meetroute.domain.shop.entity.Shop;
import com.unithon.meetroute.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 전체 Shop에 대해 우선순위를 일괄 계산해 Priority 테이블/Shop.score,priorityGrade를 채우는 배치.
 * 페이지(PAGE_SIZE) 단위로 트랜잭션을 짧게 끊고, saveAll + hibernate.jdbc.batch_size로
 * shop당 개별 API 호출 없이 왕복 횟수를 줄인다.
 */
@Service
@RequiredArgsConstructor
public class PriorityBatchService {

    private static final int PAGE_SIZE = 1000;

    private final ShopRepository shopRepository;
    private final PriorityRepository priorityRepository;
    private final PriorityScoreCalculator priorityScoreCalculator;
    private final PlatformTransactionManager transactionManager;

    public PriorityBatchResponse calculateAllAndUpsert() {
        long startedAt = System.currentTimeMillis();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        long totalShopCount = shopRepository.count();
        long processedCount = 0;
        Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by("id"));

        while (true) {
            Page<Shop> page = shopRepository.findAll(pageable);
            if (!page.hasContent()) {
                break;
            }

            transactionTemplate.executeWithoutResult(status -> processPage(page.getContent()));
            processedCount += page.getNumberOfElements();

            if (!page.hasNext()) {
                break;
            }
            pageable = page.nextPageable();
        }

        long elapsedMillis = System.currentTimeMillis() - startedAt;
        return new PriorityBatchResponse(totalShopCount, processedCount, elapsedMillis);
    }

    private void processPage(List<Shop> shops) {
        List<Long> shopIds = shops.stream().map(Shop::getId).toList();
        Map<Long, Priority> existingByShopId = priorityRepository.findByShop_IdIn(shopIds).stream()
                .collect(Collectors.toMap(priority -> priority.getShop().getId(), Function.identity()));

        LocalDateTime now = LocalDateTime.now();
        List<Priority> toInsert = new ArrayList<>();

        for (Shop shop : shops) {
            PriorityScoreCalculator.Result result = priorityScoreCalculator.calculate(shop);
            Priority existing = existingByShopId.get(shop.getId());

            if (existing != null) {
                existing.update(result.score(), result.grade(), now);
            } else {
                toInsert.add(Priority.builder()
                        .shop(shop)
                        .score(result.score())
                        .priorityGrade(result.grade())
                        .calculatedAt(now)
                        .build());
            }

            shop.applyPriority(result.score(), result.grade());
        }

        if (!toInsert.isEmpty()) {
            priorityRepository.saveAll(toInsert);
        }
    }
}
