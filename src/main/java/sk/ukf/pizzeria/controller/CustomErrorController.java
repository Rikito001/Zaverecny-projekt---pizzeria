package sk.ukf.pizzeria.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error", "Stranka nebola najdena");
                return "error/404";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("error", "Pristup zamietnuty");
                return "error/403";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", "Interna chyba servera");
                return "error/500";
            }
        }
        
        model.addAttribute("error", "Nastala neocakavana chyba");
        return "error/500";
    }
}
