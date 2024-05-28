package com.assignment.api.service;

import com.assignment.api.dto.request.CreateMessageRequest;
import com.assignment.api.dto.response.CreateMessageDto;
import com.assignment.api.dto.response.DeleteMessageDto;
import com.assignment.api.dto.response.MessageDto;
import com.assignment.api.entity.ChatTbl;
import com.assignment.api.entity.MessageTbl;
import com.assignment.api.entity.ReadMessageTbl;
import com.assignment.api.entity.UserMst;
import com.assignment.api.exception.ChatNotFoundException;
import com.assignment.api.exception.MessageNotFoundException;
import com.assignment.api.exception.UserNotFoundException;
import com.assignment.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatParticipantsRepository chatParticipantsRepository;

    @Autowired
    private ReadMessageRepository readMessageRepository;

    public CreateMessageDto sendMessage(Long chatId, CreateMessageRequest request) {
        ChatTbl chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("チャットID見つかりませんでした: " + chatId));
        Boolean idExistsInChat = chatParticipantsRepository.existsByChatIdAndUserId(chatId, request.getSenderId())
                .orElseThrow(() -> new UserNotFoundException("ユーザーはこのチャットの参加者ではありません"));
        if (idExistsInChat) {
            UserMst sender = userRepository.findById(request.getSenderId())
                    .orElseThrow(() -> new UserNotFoundException("ユーザーID見つかりませんでした: " + request.getSenderId()));
            MessageTbl messageInfo = new MessageTbl();
            messageInfo.setChat(chat);
            messageInfo.setReadFlag(Boolean.FALSE);
            messageInfo.setSender(sender);
            messageInfo.setContent(request.getContent());

            MessageTbl messageTbl = messageRepository.save(messageInfo);
            return new CreateMessageDto(
                    messageTbl.getId(), messageTbl.getSender().getId(),
                    messageTbl.getSender().getName(), messageTbl.getContent(), Boolean.TRUE);
        } else throw new UserNotFoundException("ユーザーはこのチャットの参加者ではありません");
    }

    public List<MessageDto> getMessages(Long chatId, Long requesterId) {
        chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("チャットID見つかりませんでした: " + chatId));

        UserMst user = userRepository.findById(requesterId)
                .orElseThrow(() -> new UserNotFoundException("ユーザーIDが見つかりませんでした: " + requesterId));

        Boolean idExistsInChat = chatParticipantsRepository.existsByChatIdAndUserId(chatId, requesterId)
                .orElseThrow(() -> new UserNotFoundException("ユーザーはこのチャットを参加していません。"));

        if (idExistsInChat) {
            List<MessageTbl> messagesList = messageRepository.findAllByChatId(chatId)
                    .orElseThrow(() -> new ChatNotFoundException("チャットID見つかりませんでした: " + chatId));

            // 既読・未読を判定するため、readMessageRepositoryを更新
            List<ReadMessageTbl> readMessageList = messagesList.stream().filter(message ->
                            !readMessageRepository.existsByMessageIdAndUserId(message.getId(), requesterId)
                                    && !requesterId.equals(message.getSender().getId()))
                            .map(message -> {
                                ReadMessageTbl readMessageTbl = new ReadMessageTbl();
                                readMessageTbl.setMessage(message);
                                readMessageTbl.setReader(user);
                                return readMessageTbl;
                            }).toList();
            readMessageRepository.saveAll(readMessageList);

            return messagesList.stream().map(message -> {
                Integer readCount = readMessageRepository.countByMessageId(message.getId());
                Integer totalParticipants = readMessageRepository.countParticipantsByChatId(chatId);
                return new MessageDto(
                        message.getId(), message.getSender().getId(),
                        message.getSender().getName(), message.getContent(),
                        readCount, readCount >= (totalParticipants - 1) ? "既読" : "未読");
            }).toList();

        } else throw new UserNotFoundException("ユーザーはこのチャットの参加者ではありません。");
    }

    @Transactional
    public DeleteMessageDto deleteMessage(Long messageId) {
        if (!messageRepository.existsById(messageId))
            throw new MessageNotFoundException("メッセージID見つかりませんでした: " + messageId);
        try {
            readMessageRepository.deleteById(messageId);
            messageRepository.deleteById(messageId);
            return new DeleteMessageDto(messageId, Boolean.TRUE);
        } catch (RuntimeException e) {
            return new DeleteMessageDto(messageId, Boolean.FALSE);
        }
    }
}
