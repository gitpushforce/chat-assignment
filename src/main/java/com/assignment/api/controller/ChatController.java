package com.assignment.api.controller;

import com.assignment.api.dto.request.CreateChatRequest;
import com.assignment.api.dto.request.CreateMessageRequest;
import com.assignment.api.dto.request.UpdateNotificationSettingsRequest;
import com.assignment.api.dto.response.*;
import com.assignment.api.service.ChatService;
import com.assignment.api.service.MessageService;
import com.assignment.api.service.PushSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class ChatController {

    @Autowired
    ChatService chatService;

    @Autowired
    MessageService messageService;

    @Autowired
    PushSettingService pushSettingService;

    @PostMapping(value="/create")
    public ResponseEntity<CreateChatDto> createChat(@RequestBody CreateChatRequest request) {
        CreateChatDto chat = chatService.createChat(request);
        return ResponseEntity.ok(chat);
    }

    /***
     *   Request:
     *     /v1/messages?chatId={chatId}&requesterId={requesterId}
    ***/
    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getMessages(@RequestParam Long chatId,
                                                        @RequestParam Long requesterId) {
        List<MessageDto> messages = messageService.getMessages(chatId, requesterId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<CreateMessageDto> sendMessage(@PathVariable Long chatId, @RequestBody CreateMessageRequest request) {
        CreateMessageDto message = messageService.sendMessage(chatId, request);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<DeleteMessageDto> deleteMessage(@PathVariable Long messageId) {
        DeleteMessageDto deletedMessage = messageService.deleteMessage(messageId);
        return ResponseEntity.ok(deletedMessage);
    }

    @PutMapping("/{userId}/notification-settings")
    public ResponseEntity<PushSettingsDto> updateNotificationSettings(
            @PathVariable Long userId,
            @RequestBody UpdateNotificationSettingsRequest request) {
        PushSettingsDto pushSettingsDto = pushSettingService.updateNotificationSettings(userId, request);
        return ResponseEntity.ok(pushSettingsDto);
    }
}
