package org.xapps.services.gatewayservice.routers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class MainRouter {

    private Logger logger = LoggerFactory.getLogger(MainRouter.class);

    @Bean
    public RouteLocator provideRouteLocator(RouteLocatorBuilder builder) {
        logger.info("provideRouteLocator");
        return builder.routes()
                .route(r -> r
                        .path("/security/login")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f
                                .rewritePath("/(?<segment>.*)", "/segment")
                        )
                        .uri("lb://usersservice")
                )
                .route(r -> r
                        .path("/users/security/login")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f
                                .rewritePath("/users/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://usersservice")
                )
                .route(r -> r
                        .path("/users/actuator/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .rewritePath("/users/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://usersservice")
                )
                .route(r -> r
                        .path("/users/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .rewritePath("/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://usersservice")
                )
                .route(r -> r
                        .path("/products/actuator/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .rewritePath("/products/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://productsservice")
                )
                .route(r -> r
                        .path("/categories/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .rewritePath("/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://productsservice")
                )
                .route(r -> r
                        .path("/products/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .rewritePath("/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://productsservice")
                )
                .route(r -> r
                        .path("/comments/actuator/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .rewritePath("/comments/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://commentsservice")
                )
                .route(r -> r
                        .path("/comments/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .rewritePath("/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://commentsservice")
                )
                .build();
    }

}
