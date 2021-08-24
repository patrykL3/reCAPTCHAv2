package pl.patryklubik.reCAPTCHAv2.controller;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pl.patryklubik.reCAPTCHAv2.model.ReCaptchaResponse;


/**
 * Create by Patryk Łubik on 23.08.2021.
 */

@Controller
@RequestMapping("/")
public class TemplateController {

    private RestTemplate restTemplate;
    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String RECAPTCHA_PART_PARAMS = "?secret=***&response=";
    private static final String MESSAGE = "Please select captcha";

    public TemplateController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String getFormPage() {
        return "index";
    }

    @PostMapping("next")
    public String getNextPage(@RequestParam(name="g-recaptcha-response") String captchaResponse, Model model) {

        String allParams = RECAPTCHA_PART_PARAMS +captchaResponse;
        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(RECAPTCHA_URL + allParams, HttpMethod.POST, null,
                ReCaptchaResponse.class).getBody();

        if(reCaptchaResponse.isSuccess()) {
            return "next_page";
        } else {
            model.addAttribute("message", MESSAGE);
            return "index";
        }
    }

}
