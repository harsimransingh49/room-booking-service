package com.room.booking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");


        Info information = new Info()
                .title("Conference Room Booking Service APIs")
                .version("1.0")
                .description("These APIs are used for booking conference rooms and checking availability of conference rooms.");
        return new OpenAPI().info(information).servers(List.of(server));
    }
}