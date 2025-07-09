package org.appjam.bongbaek;

import jakarta.servlet.http.HttpServletRequest;
import org.appjam.bongbaek.global.oauth.PrincipalHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/health-check")
    public String HealthCheck() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX");
        return now.format(formatter);
    }

    @GetMapping("/token-check")
    public ResponseEntity<String> tokenCheck(final HttpServletRequest request) {
        return ResponseEntity.ok("token : " + request.getHeader("Authorization"));
    }

    @GetMapping("/token-valid")
    public ResponseEntity<String> tokenValid() {
        return ResponseEntity.ok(PrincipalHandler.getMemberIdFromPrincipal().toString());
    }
}
