package com.rpgsystem.rpg;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

class RpgSystemApplicationTest {

    @Test
    void testMainMethodExists() {
        // Verify that the main method exists
        Method[] methods = RpgSystemApplication.class.getDeclaredMethods();
        boolean mainMethodFound = false;
        
        for (Method method : methods) {
            if ("main".equals(method.getName()) && 
                method.getParameterTypes().length == 1 &&
                method.getParameterTypes()[0] == String[].class) {
                mainMethodFound = true;
                break;
            }
        }
        
        assertTrue(mainMethodFound, "Main method should exist");
    }

    @Test
    void testApplicationClassStructure() {
        // Test that the class is properly annotated and structured
        assertNotNull(RpgSystemApplication.class);
        assertTrue(RpgSystemApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void testMainMethodSignature() throws NoSuchMethodException {
        // Test that main method has correct signature
        Method mainMethod = RpgSystemApplication.class.getDeclaredMethod("main", String[].class);
        
        assertNotNull(mainMethod);
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
        assertEquals(void.class, mainMethod.getReturnType());
    }

    @Test
    void testMainMethodExecution() {
        // Test that main method can be executed with mocked SpringApplication
        try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
            mockedSpringApplication.when(() -> SpringApplication.run(eq(RpgSystemApplication.class), any(String[].class)))
                    .thenReturn(mockContext);
            
            // Execute main method
            assertDoesNotThrow(() -> RpgSystemApplication.main(new String[]{"--spring.main.web-environment=false"}));
            
            // Verify SpringApplication.run was called
            mockedSpringApplication.verify(() -> SpringApplication.run(eq(RpgSystemApplication.class), any(String[].class)));
        }
    }

    @Test
    void testConstructor() {
        // Test that constructor can be instantiated
        assertDoesNotThrow(() -> {
            RpgSystemApplication application = new RpgSystemApplication();
            assertNotNull(application);
        });
    }
}