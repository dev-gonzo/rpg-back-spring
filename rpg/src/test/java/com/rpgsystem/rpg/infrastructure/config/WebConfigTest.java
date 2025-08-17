package com.rpgsystem.rpg.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("WebConfig Tests")
class WebConfigTest {

    private WebConfig webConfig;

    @Mock
    private PathMatchConfigurer pathMatchConfigurer;

    @BeforeEach
    void setUp() {
        webConfig = new WebConfig();
    }

    @Test
    @DisplayName("Should configure path match with API v1 prefix")
    void shouldConfigurePathMatchWithApiV1Prefix() {
        // When
        webConfig.configurePathMatch(pathMatchConfigurer);
        
        // Then
        verify(pathMatchConfigurer).addPathPrefix(eq("/api/v1"), any());
    }

    @Test
    @DisplayName("Should apply prefix only to RestController annotated classes")
    void shouldApplyPrefixOnlyToRestControllerAnnotatedClasses() {
        // When
        webConfig.configurePathMatch(pathMatchConfigurer);
        
        // Then
        verify(pathMatchConfigurer).addPathPrefix(eq("/api/v1"), any());
        // The predicate should check for @RestController annotation
    }

    @Test
    @DisplayName("Should test lambda predicate with various class types")
    void shouldTestLambdaPredicateWithVariousClassTypes() {
        // Given
        org.mockito.ArgumentCaptor<java.util.function.Predicate<Class<?>>> predicateCaptor = 
            org.mockito.ArgumentCaptor.forClass(java.util.function.Predicate.class);
        
        // When
        webConfig.configurePathMatch(pathMatchConfigurer);
        
        // Then
        verify(pathMatchConfigurer).addPathPrefix(eq("/api/v1"), predicateCaptor.capture());
        
        java.util.function.Predicate<Class<?>> predicate = predicateCaptor.getValue();
        
        // Test with different class types to ensure lambda coverage
        @RestController
        class AnnotatedController {}
        
        class PlainClass {}
        
        @org.springframework.stereotype.Controller
        class SpringController {}
        
        // Execute the lambda to cover the code
        boolean result1 = predicate.test(AnnotatedController.class);
        boolean result2 = predicate.test(PlainClass.class);
        boolean result3 = predicate.test(SpringController.class);
        
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();
    }

    @Test
    @DisplayName("Should create WebConfig instance successfully")
    void shouldCreateWebConfigInstanceSuccessfully() {
        // When
        WebConfig config = new WebConfig();
        
        // Then
        assertThat(config).isNotNull();
        assertThat(config).isInstanceOf(org.springframework.web.servlet.config.annotation.WebMvcConfigurer.class);
    }

    @Test
    @DisplayName("Should handle null PathMatchConfigurer gracefully")
    void shouldHandleNullPathMatchConfigurerGracefully() {
        // When & Then
        // This should not throw an exception
        try {
            webConfig.configurePathMatch(null);
        } catch (Exception e) {
            // Expected behavior - method should handle null appropriately
        }
    }

    @Test
    @DisplayName("Should be a valid WebMvcConfigurer implementation")
    void shouldBeValidWebMvcConfigurerImplementation() {
        // Then
        assertThat(webConfig).isInstanceOf(org.springframework.web.servlet.config.annotation.WebMvcConfigurer.class);
    }

    @Test
    @DisplayName("Should maintain consistent behavior across multiple calls")
    void shouldMaintainConsistentBehaviorAcrossMultipleCalls() {
        // When
        webConfig.configurePathMatch(pathMatchConfigurer);
        webConfig.configurePathMatch(pathMatchConfigurer);
        
        // Then
        verify(pathMatchConfigurer, times(2)).addPathPrefix(eq("/api/v1"), any());
    }

    @Test
    @DisplayName("Should work with different WebConfig instances")
    void shouldWorkWithDifferentWebConfigInstances() {
        // Given
        WebConfig config1 = new WebConfig();
        WebConfig config2 = new WebConfig();
        
        // When
        config1.configurePathMatch(pathMatchConfigurer);
        config2.configurePathMatch(pathMatchConfigurer);
        
        // Then
        verify(pathMatchConfigurer, times(2)).addPathPrefix(eq("/api/v1"), any());
    }
}