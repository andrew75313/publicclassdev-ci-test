package com.sparta.publicclassdev.domain.codekatas.service;

import com.sparta.publicclassdev.domain.codekatas.dto.CodeKatasDto;
import com.sparta.publicclassdev.domain.codekatas.entity.CodeKatas;
import com.sparta.publicclassdev.domain.codekatas.repository.CodeKatasRepository;
import com.sparta.publicclassdev.global.exception.CustomException;
import com.sparta.publicclassdev.global.exception.ErrorCode;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CodeKatasService {

    private final CodeKatasRepository codeKatasRepository;

    public CodeKatasDto createCodeKata(CodeKatasDto codeKatasDto) {
        CodeKatas codeKatas = CodeKatas.builder()
            .contents(codeKatasDto.getContents())
            .markDate(null)
            .build();

        codeKatasRepository.save(codeKatas);

        return new CodeKatasDto(codeKatas.getId(), codeKatasDto.getContents(), codeKatas.getMarkDate());
    }

    @Transactional(readOnly = true)
    public CodeKatasDto todayCodeKata() {
        List<CodeKatas> markKata = codeKatasRepository.findByMarkDateIsNull();
        if (markKata.isEmpty()) {
            return createRandomCodeKata();
        }

        return markKata.stream()
            .findFirst()
            .map(codeKatas -> new CodeKatasDto(codeKatas.getId(), codeKatas.getContents(), codeKatas.getMarkDate()))
            .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_CODEKATA));
    }

    public CodeKatasDto createRandomCodeKata() {
        List<CodeKatas> unmarkKata = codeKatasRepository.findByMarkDateIsNull();
        if(unmarkKata.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CODEKATA);
        }

        CodeKatas codeKatas = unmarkKata.get(new Random().nextInt(unmarkKata.size()));
        codeKatas.markCodeKatas(LocalDate.now());
        codeKatasRepository.save(codeKatas);
        return new CodeKatasDto(codeKatas.getId(), codeKatas.getContents(), codeKatas.getMarkDate());
    }

    public CodeKatasDto updateCodeKata(Long id, CodeKatasDto codeKatasDto) {
        CodeKatas codeKatas = codeKatasRepository.findById(id)
            .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_CODEKATA));

        codeKatas.updateContents(codeKatasDto.getContents());
        codeKatas.markCodeKatas(null);
        codeKatasRepository.save(codeKatas);

        return new CodeKatasDto(codeKatasDto.getId(), codeKatasDto.getContents(), codeKatas.getMarkDate());
    }
}
