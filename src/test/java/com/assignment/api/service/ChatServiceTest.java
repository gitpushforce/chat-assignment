package com.assignment.api.service;

import com.assignment.api.dto.request.CreateChatRequest;
import com.assignment.api.dto.response.CreateChatDto;
import com.assignment.api.entity.ChatParticipantsTbl;
import com.assignment.api.entity.ChatTbl;
import com.assignment.api.entity.UserMst;
import com.assignment.api.exception.ChatCreationException;
import com.assignment.api.exception.WrongParticipantsNumberException;
import com.assignment.api.repository.ChatParticipantsRepository;
import com.assignment.api.repository.ChatRepository;
import com.assignment.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ChatServiceTest {
    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatParticipantsRepository chatParticipantsRepository;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("createChat: success、 参加者人数が3人でグループチャット作成")
    void testCreateChatSuccessGroup() {
        // given:
        CreateChatRequest request = new CreateChatRequest();
        request.setIsGroup(true);
        request.setParticipantsIds(List.of(1L, 2L, 3L));

        ChatTbl chat = new ChatTbl();
        chat.setId(1L);
        chat.setIsGroup(true);

        UserMst user = new UserMst();
        user.setId(1L);

        when(chatRepository.save(any(ChatTbl.class))).thenReturn(chat);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // when
        CreateChatDto result = chatService.createChat(request);

        // then:
        assertTrue(result.getSuccess());
        verify(chatRepository, times(1)).save(any(ChatTbl.class));
        verify(chatParticipantsRepository, times(3)).save(any(ChatParticipantsTbl.class));
    }

   // @Test
    //@DisplayName("createChat: fail(WrongParticipantsNumberException), 参加者人数が2人でグループチャット作成")
   @Test
   @DisplayName("createChat: success、 参加者人数が2人で1対1チャット作成")
   void testCreateChatSuccessOneToOne() {
        // given:
       CreateChatRequest request = new CreateChatRequest();
       request.setIsGroup(false);
       request.setParticipantsIds(List.of(1L, 2L));

       ChatTbl chat = new ChatTbl();
       chat.setId(1L);
       chat.setIsGroup(false);

       UserMst user = new UserMst();
       user.setId(1L);

       when(chatRepository.save(any(ChatTbl.class))).thenReturn(chat);
       when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

       // when:
       CreateChatDto result = chatService.createChat(request);

       // then:
       assertTrue(result.getSuccess());
       verify(chatRepository, times(1)).save(any(ChatTbl.class));
       verify(chatParticipantsRepository, times(2)).save(any(ChatParticipantsTbl.class));
   }

    @Test
    @DisplayName("createChat: failure、参加者人数が2人でグループチャット作成、WrongParticipantsNumberExceptionが返却される")
    void testCreateChatFailureGroup() {
        CreateChatRequest request = new CreateChatRequest();
        request.setIsGroup(true);
        request.setParticipantsIds(List.of(1L, 2L));

        assertThrows(WrongParticipantsNumberException.class, () -> chatService.createChat(request));
    }

    @Test
    @DisplayName("createChat: failure、参加者人数が1人で1対1チャット作成の時、WrongParticipantsNumberExceptionが返却される")
    void testCreateChatFailureOneToOne() {
        CreateChatRequest request = new CreateChatRequest();
        request.setIsGroup(false);
        request.setParticipantsIds(List.of(1L));

        assertThrows(WrongParticipantsNumberException.class, () -> chatService.createChat(request));
    }

    @Test
    @DisplayName("createChat: failure、isGroupがnullの場合、ChatCreationExceptionが返却される")
    void testCreateChatFailureInvalidIsGroup() {
        CreateChatRequest request = new CreateChatRequest();
        request.setIsGroup(null);
        request.setParticipantsIds(List.of(1L, 2L));

        assertThrows(ChatCreationException.class, () -> chatService.createChat(request));
    }
}
