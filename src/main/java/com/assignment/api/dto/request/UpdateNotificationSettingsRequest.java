package com.assignment.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNotificationSettingsRequest {
    private Boolean notificationPush;
    private Boolean notificationEmail;
}
