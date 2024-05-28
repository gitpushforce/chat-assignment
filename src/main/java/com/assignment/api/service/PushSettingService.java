package com.assignment.api.service;

import com.assignment.api.dto.request.UpdateNotificationSettingsRequest;
import com.assignment.api.dto.response.PushSettingsDto;
import com.assignment.api.entity.UserMst;
import com.assignment.api.exception.ParameterErrorException;
import com.assignment.api.exception.UserNotFoundException;
import com.assignment.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class PushSettingService {

    @Autowired
    private UserRepository userRepository;
    @Transactional
    public PushSettingsDto updateNotificationSettings(Long userId, UpdateNotificationSettingsRequest request) {

        if (Objects.isNull(request.getNotificationEmail()) ||
        Objects.isNull(request.getNotificationPush())) throw new ParameterErrorException("Null以外の値を設定してください");

        UserMst user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ユーザーID見つかりませんでした: " + userId));

        try {
            user.setNotificationPush(request.getNotificationPush());
            user.setNotificationEmail(request.getNotificationEmail());
            userRepository.save(user);

            UserMst userAfterUpdate = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("ユーザーID見つかりませんでした: " + userId));

            List<String> notification = new ArrayList<>();
            if (userAfterUpdate.getNotificationPush()) notification.add("Push通知");
            if (userAfterUpdate.getNotificationEmail()) notification.add("メール通知");

            return new PushSettingsDto(userId, notification, Boolean.TRUE);
        } catch (RuntimeException e) {
            return new PushSettingsDto(userId, Collections.emptyList(), Boolean.FALSE);
        }
    }
}
