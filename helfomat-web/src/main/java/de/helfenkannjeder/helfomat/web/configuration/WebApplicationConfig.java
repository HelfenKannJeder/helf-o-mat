package de.helfenkannjeder.helfomat.web.configuration;

import de.helfenkannjeder.helfomat.api.geopoint.GeoPointConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebApplicationConfig implements WebMvcConfigurer {

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

}