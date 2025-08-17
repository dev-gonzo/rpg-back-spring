package com.rpgsystem.rpg.domain.entity;

import com.rpgsystem.rpg.domain.enums.RoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGetAuthorities() {
        User user = new User();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testGetUsername() {
        User user = new User();
        user.setEmail("test@example.com");
        
        assertEquals("test@example.com", user.getUsername());
    }

    @Test
    void testGetRoleWhenMaster() {
        User user = new User();
        user.setMaster(true);
        
        assertEquals(RoleEnum.MASTER, user.getRole());
    }

    @Test
    void testGetRoleWhenPlayer() {
        User user = new User();
        user.setMaster(false);
        
        assertEquals(RoleEnum.PLAYER, user.getRole());
    }

    @Test
    void testIsAccountNonExpired() {
        User user = new User();
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        User user = new User();
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        User user = new User();
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        User user = new User();
        assertTrue(user.isEnabled());
    }

    @Test
    void testBuilderPattern() {
        Instant now = Instant.now();
        
        User user = User.builder()
                .id("123")
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .createdAt(now)
                .updatedAt(now)
                .isMaster(true)
                .country("Brazil")
                .city("São Paulo")
                .build();
        
        assertEquals("123", user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertTrue(user.isMaster());
        assertEquals("Brazil", user.getCountry());
        assertEquals("São Paulo", user.getCity());
    }

    @Test
    void testAllArgsConstructor() {
        Instant now = Instant.now();
        
        User user = new User("123", "Test User", "test@example.com", "password123", 
                           now, now, true, "Brazil", "São Paulo");
        
        assertEquals("123", user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertTrue(user.isMaster());
        assertEquals("Brazil", user.getCountry());
        assertEquals("São Paulo", user.getCity());
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        Instant now = Instant.now();
        
        user.setId("456");
        user.setName("Another User");
        user.setEmail("another@example.com");
        user.setPassword("newpassword");
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setMaster(false);
        user.setCountry("USA");
        user.setCity("New York");
        
        assertEquals("456", user.getId());
        assertEquals("Another User", user.getName());
        assertEquals("another@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertFalse(user.isMaster());
        assertEquals("USA", user.getCountry());
        assertEquals("New York", user.getCity());
    }
}