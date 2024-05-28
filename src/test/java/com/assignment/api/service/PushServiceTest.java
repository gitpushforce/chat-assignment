package com.assignment.api.service;

import com.assignment.api.dto.request.UpdateNotificationSettingsRequest;
import com.assignment.api.dto.response.PushSettingsDto;
import com.assignment.api.entity.UserMst;
import com.assignment.api.exception.ParameterErrorException;
import com.assignment.api.exception.UserNotFoundException;
import com.assignment.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PushServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PushSettingService pushSettingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("updateNotificationSettings: success. 通常の時")
    void testUpdateNotificationSettingsSuccess() {
        // given:
        Long userId = 1L;
        UpdateNotificationSettingsRequest request = new UpdateNotificationSettingsRequest();
        request.setNotificationPush(true);
        request.setNotificationEmail(true);

        UserMst user = new UserMst();
        user.setId(userId);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // when:
        PushSettingsDto result = pushSettingService.updateNotificationSettings(userId, request);

        // then:
        assertEquals(userId, result.getUserId());
        assertEquals(List.of("Push通知", "メール通知"), result.getNotification());
        assertEquals(Boolean.TRUE, result.getSuccess());

        verify(userRepository, times(2)).findById(anyLong());
        verify(userRepository, times(1)).save(any(UserMst.class));
    }

    @Test
    @DisplayName("updateNotificationSettings: failure. paramがnullの時、ParameterErrorExceptionが返却")
    void testUpdateNotificationSettingsFailureNullValues() {
        // given:
        Long userId = 1L;
        UpdateNotificationSettingsRequest request = new UpdateNotificationSettingsRequest();
        request.setNotificationPush(null);
        request.setNotificationEmail(null);

        // when:
        assertThrows(ParameterErrorException.class, () -> pushSettingService.updateNotificationSettings(userId, request));

        // then:
        verify(userRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any(UserMst.class));
    }

    @Test
    @DisplayName("updateNotificationSettings: failure. ユーザーが存在しない時、UserNotFoundExceptionが返却")
    void testUpdateNotificationSettingsFailureUserNotFound() {
        // given:
        Long userId = 1L;
        UpdateNotificationSettingsRequest request = new UpdateNotificationSettingsRequest();
        request.setNotificationPush(true);
        request.setNotificationEmail(true);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when:
        assertThrows(UserNotFoundException.class, () -> pushSettingService.updateNotificationSettings(userId, request));

        // then:
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).save(any(UserMst.class));
    }

    @Test
    @DisplayName("updateNotificationSettings: failure. userRepository更新失敗の時、PushSettingsDtoが返却(success=false)")
    void testUpdateNotificationSettingsFailureRuntimeException() {
        // given:
        Long userId = 1L;
        UpdateNotificationSettingsRequest request = new UpdateNotificationSettingsRequest();
        request.setNotificationPush(true);
        request.setNotificationEmail(true);

        UserMst user = new UserMst();
        user.setId(userId);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("Database error")).when(userRepository).save(any(UserMst.class));

        // when:
        PushSettingsDto result = pushSettingService.updateNotificationSettings(userId, request);

        // then:
        assertEquals(userId, result.getUserId());
        assertEquals(Collections.emptyList(), result.getNotification());
        assertEquals(Boolean.FALSE, result.getSuccess());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(UserMst.class));
    }
}
