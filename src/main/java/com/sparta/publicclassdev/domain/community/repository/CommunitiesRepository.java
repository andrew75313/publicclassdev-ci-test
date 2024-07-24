package com.sparta.publicclassdev.domain.community.repository;

import com.sparta.publicclassdev.domain.community.entity.Communities;
import com.sparta.publicclassdev.domain.users.entity.Users;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunitiesRepository extends JpaRepository<Communities, Long> {
    @Query("select c from Communities c where c.user = :user order by c.modifiedAt desc limit 5")
    List<Communities> findPostByUserLimit5(Users user);

    Page<Communities> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
