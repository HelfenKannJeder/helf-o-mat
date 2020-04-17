package de.helfenkannjeder.helfomat.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Valentin Zickner
 */
@Controller
@SuppressWarnings("SameReturnValue")
public class ViewController {

    @RequestMapping({
        "result",
        "organization/{organization}",
        "question",
        "location",
        "",
    })
    public String index() {
        return "forward:/index.html";
    }
}
