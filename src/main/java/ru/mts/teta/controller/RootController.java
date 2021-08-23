package ru.mts.teta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping()
public class RootController {

    private static final Logger logger = LoggerFactory.getLogger(RootController.class);

    @GetMapping
    public String mainPage(HttpSession session, Authentication authentication) {
        if (authentication != null) {
            logger.info("Request from user '{}'", authentication.getName());
        }
        return "main_page";
    }

    @GetMapping("/access_denied")
    public ModelAndView accessDenied() {
        ModelAndView mav = new ModelAndView("access_denied");
        mav.setStatus(HttpStatus.FORBIDDEN);
        return mav;
    }
}
