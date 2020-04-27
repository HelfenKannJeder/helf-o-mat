package de.helfenkannjeder.helfomat.infrastructure.elasticsearch;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.mapping.MappingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * based on spring data elasticsearch DefaultEntityMapper
 */
class CustomEntityMapper implements EntityMapper {

    private final ObjectMapper objectMapper;

    CustomEntityMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new KotlinModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
    }

    @Override
    public String mapToString(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> mapObject(Object source) {
        try {
            return objectMapper.readValue(mapToString(source), HashMap.class);
        } catch (IOException e) {
            throw new MappingException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
        return objectMapper.readValue(source, clazz);
    }

    @Override
    public <T> T readObject(Map<String, Object> source, Class<T> targetType) {
        try {
            return this.mapToObject(this.mapToString(source), targetType);
        } catch (IOException var4) {
            throw new MappingException(var4.getMessage(), var4);
        }
    }
}
