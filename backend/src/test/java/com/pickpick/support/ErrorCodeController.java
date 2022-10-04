package com.pickpick.support;

import com.pickpick.config.dto.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ErrorCodeController {

    @GetMapping("/error-code")
    public ErrorResponse errorCode() {
        return new ErrorResponse("ERROR_CODE", "MESSAGE");
    }
}
