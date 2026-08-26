package com.unithon.meetroute.domain.bookmark.repository;

import com.unithon.meetroute.domain.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByShop_IdAndUser_Id(Long shopId, Long userId);

    Optional<Bookmark> findByShop_IdAndUser_Id(Long shopId, Long userId);

    Page<Bookmark> findByUser_Id(Long userId, Pageable pageable);

    @Query("select b.shop.id from Bookmark b where b.user.id = :userId and b.shop.id in :shopIds")
    List<Long> findShopIdsByUser_IdAndShop_IdIn(@Param("userId") Long userId, @Param("shopIds") List<Long> shopIds);
}
