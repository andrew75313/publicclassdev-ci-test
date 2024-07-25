package com.sparta.publicclassdev.domain.codekatas.repository;

import com.sparta.publicclassdev.domain.codekatas.entity.CodeKatas;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeKatasRepository extends JpaRepository<CodeKatas, Long> {
    List<CodeKatas> findByMarkDateIsNull();
}
