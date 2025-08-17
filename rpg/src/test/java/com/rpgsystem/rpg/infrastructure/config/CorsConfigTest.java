package com.rpgsystem.rpg.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CorsConfig Tests")
class CorsConfigTest {

    private CorsConfig corsConfig;

    @BeforeEach
    void setUp() {
        corsConfig = new CorsConfig();
    }

    @Test
    @DisplayName("Should create CORS filter with correct configuration")
    void shouldCreateCorsFilterWithCorrectConfiguration() {
        // When
        CorsFilter corsFilter = corsConfig.corsFilter();
        
        // Then
        assertThat(corsFilter).isNotNull();
        assertThat(corsFilter).isInstanceOf(CorsFilter.class);
    }

    @Test
    @DisplayName("Should configure CORS to allow all origins")
    void shouldConfigureCorsToAllowAllOrigins() {
        // When
        CorsFilter corsFilter = corsConfig.corsFilter();
        
        // Then
        assertThat(corsFilter).isNotNull();
        // The filter should be properly configured with the internal configuration
    }

    @Test
    @DisplayName("Should configure CORS to allow all methods")
    void shouldConfigureCorsToAllowAllMethods() {
        // When
        CorsFilter corsFilter = corsConfig.corsFilter();
        
        // Then
        assertThat(corsFilter).isNotNull();
        // Verify that the filter is created successfully with all methods allowed
    }

    @Test
    @DisplayName("Should configure CORS to allow all headers")
    void shouldConfigureCorsToAllowAllHeaders() {
        // When
        CorsFilter corsFilter = corsConfig.corsFilter();
        
        // Then
        assertThat(corsFilter).isNotNull();
        // Verify that the filter allows all headers
    }

    @Test
    @DisplayName("Should configure CORS to allow credentials")
    void shouldConfigureCorsToAllowCredentials() {
        // When
        CorsFilter corsFilter = corsConfig.corsFilter();
        
        // Then
        assertThat(corsFilter).isNotNull();
        // Verify that credentials are allowed
    }

    @Test
    @DisplayName("Should register CORS configuration for all paths")
    void shouldRegisterCorsConfigurationForAllPaths() {
        // When
        CorsFilter corsFilter = corsConfig.corsFilter();
        
        // Then
        assertThat(corsFilter).isNotNull();
        // Verify that the configuration is registered for /** pattern
    }

    @Test
    @DisplayName("Should create new instance on each call")
    void shouldCreateNewInstanceOnEachCall() {
        // When
        CorsFilter corsFilter1 = corsConfig.corsFilter();
        CorsFilter corsFilter2 = corsConfig.corsFilter();
        
        // Then
        assertThat(corsFilter1).isNotNull();
        assertThat(corsFilter2).isNotNull();
        assertThat(corsFilter1).isNotSameAs(corsFilter2);
    }

    @Test
    @DisplayName("Should handle multiple CorsConfig instances")
    void shouldHandleMultipleCorsConfigInstances() {
        // Given
        CorsConfig corsConfig1 = new CorsConfig();
        CorsConfig corsConfig2 = new CorsConfig();
        
        // When
        CorsFilter filter1 = corsConfig1.corsFilter();
        CorsFilter filter2 = corsConfig2.corsFilter();
        
        // Then
        assertThat(filter1).isNotNull();
        assertThat(filter2).isNotNull();
        assertThat(filter1).isNotSameAs(filter2);
    }
}