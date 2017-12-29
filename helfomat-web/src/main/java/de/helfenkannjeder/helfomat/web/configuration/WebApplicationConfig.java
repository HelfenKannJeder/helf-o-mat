package de.helfenkannjeder.helfomat.web.configuration;

import de.helfenkannjeder.helfomat.api.geopoint.GeoPointConverter;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebApplicationConfig extends WebMvcConfigurerAdapter {

    private final GeoPointConverter geoPointConverter;

    public WebApplicationConfig(GeoPointConverter geoPointConverter) {
        this.geoPointConverter = geoPointConverter;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/notFound").setViewName("forward:/index.html");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(geoPointConverter);
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> container
            .addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notFound"));
    }

}