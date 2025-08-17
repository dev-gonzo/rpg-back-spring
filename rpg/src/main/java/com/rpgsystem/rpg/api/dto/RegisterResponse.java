package com.rpgsystem.rpg.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private String id;
    private String name;
    private String email;
    private String message;
}