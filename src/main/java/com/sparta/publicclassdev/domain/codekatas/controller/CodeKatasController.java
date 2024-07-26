package com.sparta.publicclassdev.domain.codekatas.controller;

import com.sparta.publicclassdev.domain.codekatas.dto.CodeKatasDto;
import com.sparta.publicclassdev.domain.codekatas.service.CodeKatasService;
import com.sparta.publicclassdev.global.dto.DataResponse;
import com.sparta.publicclassdev.global.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/codekatas")
public class CodeKatasController {

    private final CodeKatasService codeKatasService;

    @PostMapping
    public ResponseEntity<DataResponse<CodeKatasDto>> createCodeKata(@RequestBody CodeKatasDto codeKatasDto) {
        CodeKatasDto codeKatas = codeKatasService.createCodeKata(codeKatasDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new DataResponse<>(201, "코드카타 생성 성공", codeKatas));
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<CodeKatasDto>>> getAllCodeKata() {
        List<CodeKatasDto> codeKatas = codeKatasService.getAllCodeKatas();
        return ResponseEntity.status(HttpStatus.OK)
            .body(new DataResponse<>(200, "전체 코드카타 조회 성공", codeKatas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<CodeKatasDto>> getCodeKata(@PathVariable Long id) {
        CodeKatasDto codeKatas = codeKatasService.getCodeKata(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new DataResponse<>(200, "코드카타 조회 성공", codeKatas));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<CodeKatasDto>> updateCodeKata(@PathVariable Long id,
        @RequestBody CodeKatasDto codeKatasDto) {
        CodeKatasDto codeKatas = codeKatasService.updateCodeKata(id, codeKatasDto);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new DataResponse<>(200, "코드카타 수정 성공", codeKatas));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteCodeKata(@PathVariable Long id) {
        codeKatasService.deleteCodeKata(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new MessageResponse(200, "코드카타 삭제 성공"));
    }

    @PostMapping("/create")
    public ResponseEntity<DataResponse<CodeKatasDto>> createRandomCodeKata() {
        CodeKatasDto codeKatas = codeKatasService.createRandomCodeKata();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new DataResponse<>(201, "랜덤 코드카타 생성 성공", codeKatas));
    }

    @GetMapping("/today")
    public ResponseEntity<DataResponse<CodeKatasDto>> getTodayCodeKata() {
        CodeKatasDto codeKatas = codeKatasService.getTodayCodeKata();
        return ResponseEntity.status(HttpStatus.OK)
            .body(new DataResponse<>(200, "오늘의 코드카타 조회 성공", codeKatas));
    }
}
