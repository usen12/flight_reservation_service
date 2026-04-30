package com.example.flightreservation.model.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageResponse {

    String message;

    public static MessageResponse sendMessage(String message) {
        return new MessageResponse(message);
    }
}
