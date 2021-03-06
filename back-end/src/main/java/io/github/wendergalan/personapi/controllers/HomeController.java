package io.github.wendergalan.personapi.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * The type Home controller.
 */
@RestController
@Profile("development")
public class HomeController {

    /**
     * Redirect de port application to swagger ui
     *
     * @return ModalAndView model and view
     */
    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("redirect:" + "swagger-ui.html");
    }
}
