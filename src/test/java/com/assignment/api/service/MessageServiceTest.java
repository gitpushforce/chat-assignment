package com.assignment.api.service;

import com.assignment.api.dto.request.CreateMessageRequest;
import com.assignment.api.dto.response.CreateMessageDto;
import com.assignment.api.dto.response.DeleteMessageDto;
import com.assignment.api.dto.response.MessageDto;
import com.assignment.api.entity.*;
import com.assignment.api.exception.ChatNotFoundException;
import com.assignment.api.exception.MessageNotFoundException;
import com.assignment.api.exception.UserNotFoundException;
import com.assignment.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MessageServiceTest {
    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatParticipantsRepository chatParticipantsRepository;

    @Mock
    private ReadMessageRepository readMessageRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("sendMessageメソッドテスト")
    class sendMessage {
        @Test
        @DisplayName("sendMessage: success メッセージ作成、通常の時")
        void testSendMessageSuccess() {
            // given:
            CreateMessageRequest request = new CreateMessageRequest();
            request.setSenderId(1L);
            request.setContent("Hello!");

            ChatTbl chat = new ChatTbl();
            chat.setId(1L);

            UserMst user = new UserMst();
            user.setId(1L);
            user.setName("User1");

            when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chat));
            when(chatParticipantsRepository.existsByChatIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(true));
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

            MessageTbl savedMessage = new MessageTbl();
            savedMessage.setId(1L);
            savedMessage.setChat(chat);
            savedMessage.setSender(user);
            savedMessage.setContent("Hello!");

            when(messageRepository.save(any(MessageTbl.class))).thenReturn(savedMessage);

            // when:
            CreateMessageDto result = messageService.sendMessage(1L, request);

            // then:
            assertEquals(1L, result.getSenderId());
            assertEquals("User1", result.getSenderName());
            assertEquals("Hello!", result.getContent());
            assertEquals(Boolean.TRUE, result.getSuccess());

            verify(chatRepository, times(1)).findById(anyLong());
            verify(chatParticipantsRepository, times(1)).existsByChatIdAndUserId(anyLong(), anyLong());
            verify(userRepository, times(1)).findById(anyLong());
            verify(messageRepository, times(1)).save(any(MessageTbl.class));
        }

        @Test
        @DisplayName("sendMessage: failure、チャット存在しない時")
        void testSendMessageChatNotFound() {
            // given:
            CreateMessageRequest request = new CreateMessageRequest();
            request.setSenderId(1L);
            request.setContent("Hello!");

            when(chatRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when:
            assertThrows(ChatNotFoundException.class, () -> messageService.sendMessage(1L, request));

            // then:
            verify(chatRepository, times(1)).findById(anyLong());
            verify(chatParticipantsRepository, never()).existsByChatIdAndUserId(anyLong(), anyLong());
            verify(userRepository, never()).findById(anyLong());
            verify(messageRepository, never()).save(any(MessageTbl.class));
        }

        @Test
        @DisplayName("sendMessage: success ユーザーがチャットに存在しない")
        void testSendMessageUserIsNotInChat() {
            // given:
            CreateMessageRequest request = new CreateMessageRequest();
            request.setSenderId(1L);
            request.setContent("Hello!");

            ChatTbl chat = new ChatTbl();
            chat.setId(1L);

            UserMst user = new UserMst();
            user.setId(1L);
            user.setName("User1");

            when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chat));
            when(chatParticipantsRepository.existsByChatIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(false));

            MessageTbl savedMessage = new MessageTbl();
            savedMessage.setId(1L);
            savedMessage.setChat(chat);
            savedMessage.setSender(user);
            savedMessage.setContent("Hello!");

            when(messageRepository.save(any(MessageTbl.class))).thenReturn(savedMessage);

            // when:
            assertThrows(UserNotFoundException.class, () -> messageService.sendMessage(1L, request));

            // then:
            verify(chatRepository, times(1)).findById(anyLong());
            verify(chatParticipantsRepository, times(1)).existsByChatIdAndUserId(anyLong(), anyLong());
        }
    }

    @Nested
    @DisplayName("getMessageメソッドテスト")
    class getMessages {
        @Test
        @DisplayName("getMessages: success、通常の時")
        void testGetMessagesSuccess() {
            // given:
            Long chatId = 1L;
            Long requesterId = 1L;

            ChatTbl chat = new ChatTbl();
            chat.setId(chatId);

            UserMst user = new UserMst();
            user.setId(requesterId);

            MessageTbl message1 = new MessageTbl();
            message1.setId(1L);
            message1.setChat(chat);
            message1.setSender(user);
            message1.setContent("Hello!");

            when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chat));
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(chatParticipantsRepository.existsByChatIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(true));
            when(messageRepository.findAllByChatId(anyLong())).thenReturn(Optional.of(Arrays.asList(message1)));
            when(readMessageRepository.existsByMessageIdAndUserId(anyLong(), anyLong())).thenReturn(false);
            when(readMessageRepository.countByMessageId(anyLong())).thenReturn(0);
            when(readMessageRepository.countParticipantsByChatId(anyLong())).thenReturn(2);

            // when:
            List<MessageDto> result = messageService.getMessages(chatId, requesterId);

            // then:
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).getMessageId());
            assertEquals("Hello!", result.get(0).getContent());

            verify(chatRepository, times(1)).findById(anyLong());
            verify(userRepository, times(1)).findById(anyLong());
            verify(chatParticipantsRepository, times(1)).existsByChatIdAndUserId(anyLong(), anyLong());
            verify(messageRepository, times(1)).findAllByChatId(anyLong());
            verify(readMessageRepository, times(1)).saveAll(anyList());
        }

        @Test
        @DisplayName("getMessages: Failure、チャットID存在しない時")
        void testGetMessagesFailureChatDoesNotExist() {
            // given:
            Long chatId = 1L;
            Long requesterId = 1L;

            when(chatRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when:
            assertThrows(ChatNotFoundException.class, () ->messageService.getMessages(chatId, requesterId));

            // then:
            verify(chatRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("getMessages: Failure、ユーザーID存在しない時")
        void testGetMessagesFailureUserDoesNotExist() {
            // given:
            Long chatId = 1L;
            Long requesterId = 1L;

            ChatTbl chat = new ChatTbl();
            chat.setId(chatId);

            when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chat));
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when:
            assertThrows(UserNotFoundException.class, () ->messageService.getMessages(chatId, requesterId));

            // then:
            verify(chatRepository, times(1)).findById(anyLong());
            verify(userRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("getMessages: Failure、ユーザーIはチャットを参加していない時")
        void testGetMessagesFailureUserNotInChat() {
            // given:
            Long chatId = 1L;
            Long requesterId = 1L;

            ChatTbl chat = new ChatTbl();
            chat.setId(chatId);

            UserMst user = new UserMst();
            user.setId(requesterId);

            when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chat));
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(chatParticipantsRepository.existsByChatIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(false));

            // when:
            assertThrows(UserNotFoundException.class, () -> messageService.getMessages(chatId, requesterId));

            // then:
            verify(chatRepository, times(1)).findById(anyLong());
            verify(userRepository, times(1)).findById(anyLong());
            verify(chatParticipantsRepository, times(1)).existsByChatIdAndUserId(anyLong(), anyLong());
        }
    }

    @Nested
    @DisplayName("deleteMessageメソッドテスト")
    class deleteMessage {
        @Test
        @DisplayName("deleteMessage: success、通常の時")
        void testDeleteMessageSuccess() {
            // given:
            Long messageId = 1L;

            when(messageRepository.existsById(anyLong())).thenReturn(true);

            // when
            DeleteMessageDto result = messageService.deleteMessage(messageId);

            // then
            assertEquals(messageId, result.getDeletedMessageId());
            assertEquals(Boolean.TRUE, result.getSuccess());

            verify(messageRepository, times(1)).existsById(anyLong());
            verify(readMessageRepository, times(1)).deleteById(anyLong());
            verify(messageRepository, times(1)).deleteById(anyLong());
        }

        @Test
        @DisplayName("deleteMessage: failure、メッセージID存在しない時")
        void testDeleteMessageFailureMessageNotFound() {
            // given:
            Long messageId = 1L;

            when(messageRepository.existsById(anyLong())).thenReturn(false);

            // when
            assertThrows(MessageNotFoundException.class, () -> messageService.deleteMessage(messageId));

            // then
            verify(messageRepository, times(1)).existsById(anyLong());
            verify(readMessageRepository, never()).deleteById(anyLong());
            verify(messageRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("deleteMessage: failure、readMessageRepository更新失敗時、DeleteMessageDtoを返却する(success=false)")
        void testDeleteMessageFailureErrorUpdatingReadMessageRepository() {
            // given:
            Long messageId = 1L;

            when(messageRepository.existsById(anyLong())).thenReturn(true);
            doThrow(new RuntimeException()).when(readMessageRepository).deleteById(anyLong());

            // when
            DeleteMessageDto result = messageService.deleteMessage(messageId);

            // then
            assertEquals(messageId, result.getDeletedMessageId());
            assertEquals(Boolean.FALSE, result.getSuccess());

            verify(messageRepository, times(1)).existsById(anyLong());
            verify(readMessageRepository, times(1)).deleteById(anyLong());
            verify(messageRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("deleteMessage: failure、messageRepository更新失敗時、DeleteMessageDtoを返却する(success=false)")
        void testDeleteMessageFailureErrorUpdatingMessageRepository() {
            // given:
            Long messageId = 1L;

            when(messageRepository.existsById(anyLong())).thenReturn(true);
            doThrow(new RuntimeException()).when(messageRepository).deleteById(anyLong());

            // when
            DeleteMessageDto result = messageService.deleteMessage(messageId);

            // then
            assertEquals(messageId, result.getDeletedMessageId());
            assertEquals(Boolean.FALSE, result.getSuccess());

            verify(messageRepository, times(1)).existsById(anyLong());
            verify(readMessageRepository, times(1)).deleteById(anyLong());
            verify(messageRepository, times(1)).deleteById(anyLong());
        }
    }
}
