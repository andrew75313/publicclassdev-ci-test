package com.sparta.publicclassdev.domain.coderuns.service;

import com.sparta.publicclassdev.domain.codekatas.entity.CodeKatas;
import com.sparta.publicclassdev.domain.codekatas.repository.CodeKatasRepository;
import com.sparta.publicclassdev.domain.coderuns.dto.CodeRunsRequestDto;
import com.sparta.publicclassdev.domain.coderuns.dto.CodeRunsResponseDto;
import com.sparta.publicclassdev.domain.coderuns.entity.CodeRuns;
import com.sparta.publicclassdev.domain.coderuns.repository.CodeRunsRepository;
import com.sparta.publicclassdev.domain.teams.entity.Teams;
import com.sparta.publicclassdev.domain.teams.repository.TeamsRepository;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.domain.users.repository.UsersRepository;
import com.sparta.publicclassdev.global.exception.CustomException;
import com.sparta.publicclassdev.global.exception.ErrorCode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CodeRunsService {

    private final CodeRunsRepository codeRunsRepository;
    private final TeamsRepository teamsRepository;
    private final CodeKatasRepository codeKatasRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public CodeRunsResponseDto runCode(Long teamsId, Long codeKatasId, CodeRunsRequestDto requestDto) {
        Users users = getCurrentUser();
        Teams teams = findTeamById(teamsId);
        checkUserTeam(users, teams);

        CodeKatas codeKatas = findCodeKatasById(codeKatasId);

        long startTime = System.currentTimeMillis();
        String result = executeCodeByLanguage(requestDto.getLanguage(), requestDto.getCode());
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        CodeRuns codeRuns = getCreateCodeRun(teamsId, codeKatasId, requestDto.getCode(),
            requestDto.getLanguage(), result, responseTime, teams, codeKatas, users);

        codeRunsRepository.save(codeRuns);

        return new CodeRunsResponseDto(codeKatasId, teamsId, users.getId(), responseTime, result);
    }

    public List<CodeRuns> getCodeRunsByTeam(Long teamsId) {
        Users currentUser = getCurrentUser();
        Teams teams = findTeamById(teamsId);
        checkUserTeam(currentUser, teams);

        return codeRunsRepository.findAllByTeamsId(teamsId);
    }

    private CodeRuns getCreateCodeRun(Long teamsId, Long codeKatasId, String code, String language,
        String result, long responseTime, Teams team, CodeKatas codeKatas, Users currentUser) {
        Optional<CodeRuns> existingRun = codeRunsRepository.findByTeamsIdAndCodeKatasId(teamsId,
            codeKatasId);
        CodeRuns codeRuns;
        if (existingRun.isPresent()) {
            codeRuns = existingRun.get();
            if (responseTime < codeRuns.getResponseTime()) {
                codeRuns.updateResponseTime(responseTime, result);
                codeRunsRepository.save(codeRuns);
            }
        } else {
            codeRuns = CodeRuns.builder()
                .code(code)
                .responseTime(responseTime)
                .result(result)
                .language(language)
                .teams(team)
                .codeKatas(codeKatas)
                .users(currentUser)
                .build();
            codeRunsRepository.save(codeRuns);
        }
        return codeRuns;
    }

    private String executeCodeByLanguage(String language, String code) {
        switch (language.toLowerCase()) {
            case "python":
                return executePythonCode(code);
            case "javascript":
                return executeJavaScriptCode(code);
            case "java":
                return executeJavaCode(code);
            case "ruby":
                return executeRubyCode(code);
            default:
                return "지원하지 않는 언어 : " + language;
        }
    }

    private String executePythonCode(String code) {
        return executeCode("python", ".py", code);
    }

    private String executeJavaScriptCode(String code) {
        return executeCode("node", ".js", code);
    }

    private String executeRubyCode(String code) {
        return executeCode("ruby", ".rb", code);
    }

    private String executeJavaCode(String code) {
        try {
            File file = File.createTempFile("script", ".java");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(code);
            fileWriter.close();

            ProcessBuilder processBuilder = new ProcessBuilder("javac", file.getAbsolutePath());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();

            String classFilePath = file.getAbsolutePath().replace(".java", ".class");
            ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-cp", file.getParent(),
                file.getName().replace(".java", ""));
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();

            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(runProcess.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();

            file.delete();
            new File(classFilePath).delete();

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private String executeCode(String command, String fileExtension, String code) {
        try {
            File scriptFile = File.createTempFile("script", fileExtension);
            FileWriter fileWriter = new FileWriter(scriptFile);
            fileWriter.write(code);
            fileWriter.close();

            ProcessBuilder processBuilder = new ProcessBuilder(command,
                scriptFile.getAbsolutePath());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();

            scriptFile.delete();

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return usersRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Teams findTeamById(Long teamsId) {
        return teamsRepository.findById(teamsId)
            .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
    }

    private CodeKatas findCodeKatasById(Long codeKatasId) {
        return codeKatasRepository.findById(codeKatasId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CODEKATA));
    }

    private void checkUserTeam(Users user, Teams team) {
        boolean isInTeam = user.getTeamUsers().stream()
            .anyMatch(teamUser -> teamUser.getTeams().equals(team));
        if (!isInTeam) {
            throw new CustomException(ErrorCode.USER_NOT_TEAM);
        }
    }
}
