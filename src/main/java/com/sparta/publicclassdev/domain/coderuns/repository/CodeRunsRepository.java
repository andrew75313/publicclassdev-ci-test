package com.sparta.publicclassdev.domain.coderuns.repository;

import com.sparta.publicclassdev.domain.coderuns.entity.CodeRuns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRunsRepository extends JpaRepository<CodeRuns, Long> {

}
