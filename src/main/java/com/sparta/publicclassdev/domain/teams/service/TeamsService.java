package com.sparta.publicclassdev.domain.teams.service;

import com.sparta.publicclassdev.domain.chatrooms.entity.ChatRoomUsers;
import com.sparta.publicclassdev.domain.chatrooms.entity.ChatRooms;
import com.sparta.publicclassdev.domain.chatrooms.repository.ChatRoomUsersRepository;
import com.sparta.publicclassdev.domain.chatrooms.repository.ChatRoomsRepository;
import com.sparta.publicclassdev.domain.teams.dto.TeamResponseDto;
import com.sparta.publicclassdev.domain.teams.entity.TeamUsers;
import com.sparta.publicclassdev.domain.teams.entity.Teams;
import com.sparta.publicclassdev.domain.teams.repository.TeamUsersRepository;
import com.sparta.publicclassdev.domain.teams.repository.TeamsRepository;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.domain.users.repository.UsersRepository;
import com.sparta.publicclassdev.global.exception.CustomException;
import com.sparta.publicclassdev.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamsService {

    private final TeamsRepository teamsRepository;
    private final TeamUsersRepository teamUsersRepository;
    private final UsersRepository usersRepository;
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChatRoomUsersRepository chatRoomUsersRepository;

    private static final List<String> Modifier = List.of(
        "Agile", "Brave", "Calm", "Daring", "Eager", "Fierce", "Gentle", "Heroic", "Jolly", "Keen"
    );

    private static final List<String> Label = List.of(
        "Warriors", "Knights", "Mavericks", "Pioneers", "Rangers", "Samurais", "Titans", "Vikings",
        "Wizards", "Yankees"
    );

    private final Random RANDOM = new Random();
    private final ConcurrentLinkedDeque<Users> waitQueue = new ConcurrentLinkedDeque<>();

    private String randomTeamName() {
        String teamName;
        do {
            String modifier = Modifier.get(RANDOM.nextInt(Modifier.size()));
            String label = Label.get(RANDOM.nextInt(Label.size()));
            teamName = modifier + " " + label;
        } while (teamsRepository.existsByName(teamName));
        return teamName;
    }

    @Transactional
    public void teamMatch(Users users) {
        waitQueue.add(users);
    }

    @Transactional
    public TeamResponseDto createTeam() {
        List<Users> waitUser = new ArrayList<>();
        for (int i = 0; i < 3 && !waitQueue.isEmpty(); i++) {
            waitUser.add(waitQueue.poll());
        }
        Collections.shuffle(waitUser);

        Teams teams = Teams.builder()
            .name(randomTeamName())
            .build();
        teamsRepository.save(teams);

        ChatRooms chatRooms = ChatRooms.builder()
            .teams(teams)
            .build();
        chatRoomsRepository.save(chatRooms);

        List<Users> teamMembers = new ArrayList<>();
        for (Users users : waitUser) {
            teamMembers.add(users);

            TeamUsers teamUsers = TeamUsers.builder()
                .teams(teams)
                .users(users)
                .build();
            teamUsersRepository.save(teamUsers);

            ChatRoomUsers chatRoomUsers = ChatRoomUsers.builder()
                .chatRooms(chatRooms)
                .users(users)
                .build();
            chatRoomUsersRepository.save(chatRoomUsers);
        }
        return new TeamResponseDto(teams, teamMembers);
    }

    public void deleteAllTeams() {
        teamsRepository.deleteAll();
        teamUsersRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public TeamResponseDto getTeamById(Long teamsId, Long usersId) {
        Teams teams = validateTeam(teamsId);
        Users users = validateUser(usersId);

        boolean isUserInTeam = teamUsersRepository.existsByTeamsAndUsers(teams, users);
        if (!isUserInTeam) {
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }

        List<Users> teamMembers = teams.getTeamUsers().stream()
            .map(TeamUsers::getUsers)
            .collect(Collectors.toList());

        return new TeamResponseDto(teams, teamMembers);
    }


    private Teams validateTeam(Long teamsId) {
        return teamsRepository.findById(teamsId)
            .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
    }

    private Users validateUser(Long userId) {
        return usersRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
