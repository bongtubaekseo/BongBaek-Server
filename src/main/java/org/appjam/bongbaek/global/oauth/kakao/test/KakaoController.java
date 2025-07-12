package org.appjam.bongbaek.global.oauth.kakao.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoController {
    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code) {
        System.out.println("client_code: " + code);
    }
}
