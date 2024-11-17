package com.iiie.server;

import com.iiie.server.service.KakaoService;
import com.iiie.server.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class TestKakaoTokenController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @GetMapping("/kakao")
    public String kakologin(Model model, HttpServletResponse response) {
        response.setContentType(MediaType.TEXT_HTML_VALUE);

        return "html/index";
    }
}
