package com.agiklo.oracledatabase.security.Registration;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import static com.agiklo.oracledatabase.controller.ApiMapping.REGISTRATION_REST_URL;

@RestController
@RequestMapping(path = REGISTRATION_REST_URL)
@AllArgsConstructor
@EnableWebSecurity
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }


    @GetMapping(path = "confirm")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
