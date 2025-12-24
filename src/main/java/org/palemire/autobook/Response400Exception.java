package org.palemire.autobook;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class Response400Exception extends RuntimeException {
    public Response400Exception(String message) {
        super(message);
    }
}
