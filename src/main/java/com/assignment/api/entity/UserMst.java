package com.assignment.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USER_MST")
@Getter
@Setter
public class UserMst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "notification_push", nullable = false)
    private Boolean notificationPush;
    @Column(name = "notification_email", nullable = false)
    private Boolean notificationEmail;
}
