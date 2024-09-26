package com.room.booking.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class ResponseDto<T> {

    private String statusCode;

    private String message;

    private T data;
}
