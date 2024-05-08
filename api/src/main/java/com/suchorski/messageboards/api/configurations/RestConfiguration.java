package com.suchorski.messageboards.api.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@AllArgsConstructor
@Log4j2
public class RestConfiguration implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        log.debug("Configuring repository REST...");
        final var metamodel = entityManager.getMetamodel();
        final var classes = metamodel.getEntities().stream().map(e -> e.getJavaType()).toArray(Class[]::new);
        config.exposeIdsFor(classes);
        config.setExposeRepositoryMethodsByDefault(false);
        config.setRepositoryDetectionStrategy(RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED);
    }

}
