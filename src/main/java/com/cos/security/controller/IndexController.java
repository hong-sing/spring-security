package com.cos.security.controller;

import com.cos.security.config.auth.PrincipalDetails;
import com.cos.security.model.User;
import com.cos.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("/test/login=================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());

        System.out.println("userDetails : " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("/test/oauth/login=================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes());
        System.out.println("oauth2User :" + oauth.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // OAuth 로그인을 해도 PrincipalDetails
    // 일반 로그인을 해도 PrincipalDetails
    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails : " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    // 현재는 스프링 시큐리티가 낚아채간다.
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);  // 회원가입 잘됨. 하지만 비밀번호가 1234(유저가 입력한 정보 그대로)로 들어감 -> 시큐리티로 로그인 할 수 없음
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    @ResponseBody
    public String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    @ResponseBody
    public String data() {
        return "데이터정보";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @GetMapping("/joinProc")
    @ResponseBody
    public String joinProc() {
        return "회원가입 완료됨!";
    }
}
