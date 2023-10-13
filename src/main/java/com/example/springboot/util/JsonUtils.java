package com.example.springboot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.nio.file.Path;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @SneakyThrows
    public static String getContent(String fileName) {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        return Files.readString(Path.of(classPathResource.getURI()));
    }

}
