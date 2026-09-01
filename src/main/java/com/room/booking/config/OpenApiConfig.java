package com.room.booking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI roomBookingOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Room Booking API")
                .version("1.0.0")
                .description("Books the smallest conference room that fits the party. "
                        + "Times are 15-minute slots. Maintenance windows and overlapping bookings are rejected. "
                        + "Rooms are locked before a booking is written."));
    }
}
