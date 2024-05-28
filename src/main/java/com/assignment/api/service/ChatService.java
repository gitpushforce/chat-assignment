package com.assignment.api.service;

import com.assignment.api.dto.request.CreateChatRequest;
import com.assignment.api.dto.response.CreateChatDto;
import com.assignment.api.entity.ChatParticipantsTbl;
import com.assignment.api.entity.ChatTbl;
import com.assignment.api.entity.UserMst;
import com.assignment.api.exception.ChatCreationException;
import com.assignment.api.exception.WrongParticipantsNumberException;
import com.assignment.api.exception.UserNotFoundException;
import com.assignment.api.repository.ChatParticipantsRepository;
import com.assignment.api.repository.ChatRepository;
import com.assignment.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatParticipantsRepository chatParticipantsRepository;

    public CreateChatDto createChat(CreateChatRequest request) {

        if (Objects.equals(request.getIsGroup(), Boolean.TRUE)) {
            if (request.getParticipantsIds().size() > 2) return createChatProcess(request);
            else throw new WrongParticipantsNumberException("グループチャットを作成するには、3人以上の参加者が必要です。");
        } else if (Objects.equals(request.getIsGroup(), Boolean.FALSE)) {
            if (request.getParticipantsIds().size() == 2) return createChatProcess(request);
            else
                throw new WrongParticipantsNumberException("1対1のチャットを作成するには、参加者を2人に設定してください。");
        } else throw new ChatCreationException("isGroupの値はTrueかFalseを設定してください。");
    }

    @Transactional
    private CreateChatDto createChatProcess(CreateChatRequest request) {
        ChatTbl chat = new ChatTbl();
        chat.setIsGroup(request.getIsGroup());
        final ChatTbl savedChat = chatRepository.save(chat);

        request.getParticipantsIds().forEach(participantId -> {
            UserMst user = userRepository
                    .findById(participantId)
                    .orElseThrow(() -> new UserNotFoundException("ユーザーが見つかりませんでした"));
            ChatParticipantsTbl participant = new ChatParticipantsTbl();
            participant.setChat(savedChat);
            participant.setUser(user);
            chatParticipantsRepository.save(participant);
        });
        return new CreateChatDto(chat.getId(), Boolean.TRUE);
    }
}
