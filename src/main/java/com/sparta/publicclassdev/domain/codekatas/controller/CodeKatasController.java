package com.sparta.publicclassdev.domain.codekatas.controller;

import com.sparta.publicclassdev.domain.codekatas.dto.CodeKatasDto;
import com.sparta.publicclassdev.domain.codekatas.service.CodeKatasService;
import com.sparta.publicclassdev.global.dto.DataResponse;
import com.sparta.publicclassdev.global.dto.MessageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/codekatas")
public class CodeKatasController {

    private final CodeKatasService codeKatasService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResponse<CodeKatasDto>> createCodeKata(@RequestBody CodeKatasDto codeKatasDto) {
        CodeKatasDto codeKatas = codeKatasService.createCodeKata(codeKatasDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new DataResponse<>(201, "코드카타 생성 성공", codeKatas));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResponse<List<CodeKatasDto>>> getAllCodeKata() {
        List<CodeKatasDto> codeKatas = codeKatasService.getAllCodeKatas();
        return ResponseEntity.status(HttpStatus.OK)
            .body(new DataResponse<>(200, "전체 코드카타 조회 성공", codeKatas));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResponse<CodeKatasDto>> getCodeKata(@PathVariable Long id) {
        CodeKatasDto codeKatas = codeKatasService.getCodeKata(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new DataResponse<>(200, "코드카타 조회 성공", codeKatas));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResponse<CodeKatasDto>> updateCodeKata(@PathVariable Long id, @RequestBody CodeKatasDto codeKatasDto) {
        CodeKatasDto codeKatas = codeKatasService.updateCodeKata(id, codeKatasDto);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new DataResponse<>(200, "코드카타 수정 성공", codeKatas));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> deleteCodeKata(@PathVariable Long id) {
        codeKatasService.deleteCodeKata(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new MessageResponse(200, "코드카타 삭제 성공"));
    }

    @GetMapping("/today")
    public ResponseEntity<DataResponse<CodeKatasDto>> getTodayCodeKata() {
        CodeKatasDto codeKatas = codeKatasService.getTodayCodeKata();
        return ResponseEntity.status(HttpStatus.OK)
            .body(new DataResponse<>(200, "오늘의 코드카타 조회 성공", codeKatas));
    }
}
