package com.assignment.api.controller;

import com.assignment.api.dto.request.CreateChatRequest;
import com.assignment.api.dto.request.CreateMessageRequest;
import com.assignment.api.dto.request.UpdateNotificationSettingsRequest;
import com.assignment.api.dto.response.*;
import com.assignment.api.service.ChatService;
import com.assignment.api.service.MessageService;
import com.assignment.api.service.PushSettingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private PushSettingService pushSettingService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Nested
    @DisplayName("チャット作成エンドポイント(createChat)テスト")
    class createChat {
        @Test
        @DisplayName("createChat: status 200, 1対1チャットで参加人数が2人の時")
        void testCreateChatStatus_200_1() throws Exception {
            // given:
            CreateChatDto response = new CreateChatDto(1L, true);

            when(chatService.createChat(any(CreateChatRequest.class))).thenReturn(response);

            // when:
            mockMvc.perform(post("/v1/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"participantsIds\": [1,2], \"isGroup\": \"true\"}"))
                    // then:
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("createChat: status 200, グループチャットで参加人数が3人の時")
        void testCreateChatStatus_200_2() throws Exception {
            // given:
            CreateChatDto response = new CreateChatDto(1L, true);

            when(chatService.createChat(any(CreateChatRequest.class))).thenReturn(response);

            // when:
            mockMvc.perform(post("/v1/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"participantsIds\": [1,2,3], \"isGroup\": \"true\"}"))
                    // then:
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("createChat: status 400, 1対1のチャットで参加人数が1人の時")
        void testCreateChatStatus_400_1() throws Exception {
            // given:
            when(chatService.createChat(any(CreateChatRequest.class))).thenCallRealMethod();

            // when:
            mockMvc.perform(post("/v1/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"participantsIds\": [1], \"isGroup\": \"false\"}"))
                    // then:
                    .andExpect(status().is(400))
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.message").value("1対1のチャットを作成するには、参加者を2人に設定してください。"));
        }

        @Test
        @DisplayName("createChat: status 400, グループチャットで参加人数が2人の時")
        void testCreateChatStatus_400_2() throws Exception {
            // given:
            when(chatService.createChat(any(CreateChatRequest.class))).thenCallRealMethod();

            // when:
            mockMvc.perform(post("/v1/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"participantsIds\": [1, 2], \"isGroup\": \"true\"}"))
                    // then:
                    .andExpect(status().is(400))
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.message").value("グループチャットを作成するには、3人以上の参加者が必要です。"));
        }
    }

    @Nested
    @DisplayName("メッセージ取得エンドポイント(getMessages)テスト")
    class getMessages {
        @Test
        @DisplayName("getMessages: status 200, 通常リクエストの時")
        void testGetMessages_200() throws Exception {
            // given:
            MessageDto messageDto = new MessageDto(
                    1L, 2L,
                    "Name", "Hi, How are you?",
                    3, "既読");

            when(messageService.getMessages(anyLong(), anyLong())).thenReturn(Collections.singletonList(messageDto));

            // when:
            mockMvc.perform(get("/v1/messages")
                            .param("chatId", "1")
                            .param("requesterId", "1"))
                    // then:
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].messageId").value(1L))
                    .andExpect(jsonPath("$[0].senderId").value(2L))
                    .andExpect(jsonPath("$[0].senderName").value("Name"))
                    .andExpect(jsonPath("$[0].content").value("Hi, How are you?"))
                    .andExpect(jsonPath("$[0].readStatus").value("既読"));
        }

        @Test
        @DisplayName("getMessages: status 400, パラメターchatIdが数字以外の時")
        void testGetMessages_400_1() throws Exception {
            // given:
            when(messageService.getMessages(anyLong(), anyLong())).thenCallRealMethod();
            // when:
            mockMvc.perform(get("/v1/messages")
                            .param("chatId", "a")
                            .param("requesterId", "1"))
                    // then:
                    .andExpect(status().is(400));
        }

        @Test
        @DisplayName("getMessages: status 400, パラメターrequesterIdが数字以外の時")
        void testGetMessages_400_2() throws Exception {
            // given:
            when(messageService.getMessages(anyLong(), anyLong())).thenCallRealMethod();
            // when:
            mockMvc.perform(get("/v1/messages")
                            .param("chatId", "1")
                            .param("requesterId", "a"))
                    // then:
                    .andExpect(status().is(400));
        }
    }

    @Nested
    @DisplayName("メッセージ作成エンドポイント(sendMessage)テスト")
    class sendMessage {
        @Test
        @DisplayName("sendMessage: status 200, 通常リクエストの時")
        void testSendMessage_200() throws Exception {
            // given:
            CreateMessageDto response = new CreateMessageDto(
                    10L, 2L,
                    "田中", "Hi",
                    true);

            when(messageService.sendMessage(anyLong(), any(CreateMessageRequest.class))).thenReturn(response);

            // when:
            mockMvc.perform(post("/v1/{chatId}/messages", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"senderId\": 2, \"content\": \"Hi\"}"))
                    // then:
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.messageId").value(10L))
                    .andExpect(jsonPath("$.senderId").value(2L))
                    .andExpect(jsonPath("$.senderName").value("田中"))
                    .andExpect(jsonPath("$.content").value("Hi"))
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("sendMessage: status 400, パラメターchatIdが数字以外の時")
        void testSendMessage_400_1() throws Exception {
            // given:
            when(messageService.sendMessage(anyLong(), any(CreateMessageRequest.class))).thenCallRealMethod();

            // when:
            mockMvc.perform(post("/v1/{chatId}/messages", "a")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"senderId\": 2, \"content\": \"Hi\"}"))
                    // then:
                    .andExpect(status().is(400));
        }

        @Test
        @DisplayName("sendMessage: status 400, パラメターsenderIdが数字以外の時")
        void testSendMessage_400_2() throws Exception {
            // given:
            when(messageService.sendMessage(anyLong(), any(CreateMessageRequest.class))).thenCallRealMethod();

            // when:
            mockMvc.perform(post("/v1/{chatId}/messages", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"senderId\": a, \"content\": \"Hi\"}"))
                    // then:
                    .andExpect(status().is(400));
        }
    }

    @Nested
    @DisplayName("メッセージ削除エンドポイント(deleteMessage)テスト")
    class deleteMessage {
        @Test
        @DisplayName("deleteMessages: status 200, 通常リクエストの時")
        void testDeleteMessage_200() throws Exception {
            // given:
            DeleteMessageDto response = new DeleteMessageDto(1L, true);

            when(messageService.deleteMessage(anyLong())).thenReturn(response);

            // when:
            mockMvc.perform(delete("/v1/messages/{messageId}", 1))
                    // then:
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.deletedMessageId").value(1L))
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("deleteMessages: status 400, messageIdパラメターリクエストが数字以外の時")
        void testDeleteMessage_400_1() throws Exception {
            // given:
            when(messageService.deleteMessage(anyLong())).thenCallRealMethod();

            // when:
            mockMvc.perform(delete("/v1/messages/{messageId}", "a"))
                    // then:
                    .andExpect(status().is(400));
        }
    }

    @Nested
    @DisplayName("通知設定エンドポイント(updateNotificationSettings)テスト")
    class updateNotificationSettings {
        @Test
        @DisplayName("updateNotificationSettings: status 200, 通常リクエストの時")
        void testUpdateNotificationSettings_200() throws Exception {
            // given:
            PushSettingsDto response = new PushSettingsDto(
                    1L,
                    List.of("Push通知"),
                    true);

            when(pushSettingService.updateNotificationSettings(anyLong(), any(UpdateNotificationSettingsRequest.class)))
                    .thenReturn(response);

            // when:
            mockMvc.perform(put("/v1/{userId}/notification-settings", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"notificationPush\": true, \"notificationEmail\": false }"))
                    // then:
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(1L))
                    .andExpect(jsonPath("$.notification[0]").value("Push通知"))
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("updateNotificationSettings: status 400, パラメターnotificationPushがnullの時")
        void testUpdateNotificationSettings_400_1() throws Exception {
            // given:
            when(pushSettingService.updateNotificationSettings(anyLong(), any(UpdateNotificationSettingsRequest.class)))
                    .thenCallRealMethod();

            // when:
            mockMvc.perform(put("/v1/{userId}/notification-settings", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"notificationPush\": null, \"notificationEmail\": false }"))
                    // then:
                    .andExpect(status().is(400))
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.message").value("Null以外の値を設定してください"));
        }

        @Test
        @DisplayName("updateNotificationSettings: status 400, パラメターusedIdが数字以外の時")
        void testUpdateNotificationSettings_400_2() throws Exception {
            // given:
            when(pushSettingService.updateNotificationSettings(anyLong(), any(UpdateNotificationSettingsRequest.class)))
                    .thenCallRealMethod();

            // when:
            mockMvc.perform(put("/v1/{userId}/notification-settings", "a")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"notificationPush\": true, \"notificationEmail\": false }"))
                    // then:
                    .andExpect(status().is(400));
        }

        @Test
        @DisplayName("updateNotificationSettings: status 400, パラメターnotificationEmailがnullの時")
        void testUpdateNotificationSettings_400_3() throws Exception {
            // given:
            when(pushSettingService.updateNotificationSettings(anyLong(), any(UpdateNotificationSettingsRequest.class)))
                    .thenCallRealMethod();

            // when:
            mockMvc.perform(put("/v1/{userId}/notification-settings", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"notificationPush\": true, \"notificationEmail\": null }"))
                    // then:
                    .andExpect(status().is(400))
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.message").value("Null以外の値を設定してください"));
        }

        @Test
        @DisplayName("updateNotificationSettings: status 400, パラメターnotificationEmailが数字以外、かつnull以外の時")
        void testUpdateNotificationSettings_400_4() throws Exception {
            // given:
            when(pushSettingService.updateNotificationSettings(anyLong(), any(UpdateNotificationSettingsRequest.class)))
                    .thenCallRealMethod();

            // when:
            mockMvc.perform(put("/v1/{userId}/notification-settings", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"notificationPush\": \"aa\", \"notificationEmail\": true }"))
                    // then:
                    .andExpect(status().is(400));
        }

        @Test
        @DisplayName("updateNotificationSettings: status 400, パラメターnotificationEmailが数字以外、かつnull以外の時")
        void testUpdateNotificationSettings_400_5() throws Exception {
            // given:
            when(pushSettingService.updateNotificationSettings(anyLong(), any(UpdateNotificationSettingsRequest.class)))
                    .thenCallRealMethod();

            // when:
            mockMvc.perform(put("/v1/{userId}/notification-settings", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"notificationPush\": true, \"notificationEmail\": \"aa\" }"))
                    // then:
                    .andExpect(status().is(400));
        }
    }
}
