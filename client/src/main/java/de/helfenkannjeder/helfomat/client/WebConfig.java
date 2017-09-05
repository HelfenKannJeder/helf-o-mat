package de.helfenkannjeder.helfomat.client;

import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

/**
 * @author Valentin Zickner
 */
@Configuration
public class WebConfig {

    @Bean
    public ErrorViewResolver supportPathBasedLocationStrategyWithoutHashes() {
        return WebConfig::handleErrorRequest;
    }

    private static ModelAndView handleErrorRequest(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        if (status == HttpStatus.NOT_FOUND) {
            return new ModelAndView("index.html", Collections.emptyMap(), HttpStatus.OK);
        }
        return null;
    }
}