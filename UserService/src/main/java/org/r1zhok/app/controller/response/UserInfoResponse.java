package org.r1zhok.app.controller.response;

import java.time.LocalDateTime;

public record UserInfoResponse(String username, String firstname,
                               String lastname, LocalDateTime createdAt, String roles) {}