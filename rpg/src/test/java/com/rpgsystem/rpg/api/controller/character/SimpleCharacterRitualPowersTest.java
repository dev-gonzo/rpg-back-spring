package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.controller.character.CharacterRitualPowers;
import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerResponse;
import com.rpgsystem.rpg.api.exception.GlobalExceptionHandler;
import com.rpgsystem.rpg.application.service.character.CharacterRitualPowerService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SimpleCharacterRitualPowersTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CharacterRitualPowerService characterRitualPowerService;

    @BeforeEach
    void setUp() {
        CharacterRitualPowers controller = new CharacterRitualPowers(characterRitualPowerService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void simpleTest() throws Exception {
        // Given
        List<CharacterRitualPowerResponse> ritualPowers = Arrays.asList();
        when(characterRitualPowerService.getAll(anyString(), any(User.class)))
                .thenReturn(ritualPowers);
        
        User mockUser = User.builder()
                 .id("user123")
                 .name("testuser")
                 .email("test@example.com")
                 .password("password")
                 .build();

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(mockUser);

            mockMvc.perform(get("/characters/123/ritual-powers")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }
}