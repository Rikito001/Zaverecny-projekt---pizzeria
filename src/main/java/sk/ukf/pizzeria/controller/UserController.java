package sk.ukf.pizzeria.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.ukf.pizzeria.entity.User;
import sk.ukf.pizzeria.exception.EmailAlreadyExistsException;
import sk.ukf.pizzeria.security.CustomUserDetails;
import sk.ukf.pizzeria.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/prihlasenie")
    public String loginPage(@RequestParam(required = false) String chyba, Model model) {
//        if (chyba != null) {
//            model.addAttribute("error", "Nespravny email alebo heslo");
//        }
        return "user/login";
    }

    @GetMapping("/registracia")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/registracia")
    public String register(@Valid @ModelAttribute("user") User user,
                          BindingResult bindingResult,
                          @RequestParam String confirmPassword,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        
        if (!user.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("password", "password.mismatch", "Hesla sa nezhoduju");
        }
        
        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        try {
            userService.register(user);
            redirectAttributes.addFlashAttribute("message", "Registracia bola uspesna. Mozete sa prihlasit.");
            return "redirect:/prihlasenie";
        } catch (EmailAlreadyExistsException e) {
            bindingResult.rejectValue("email", "email.exists", e.getMessage());
            return "user/register";
        }
    }

    @GetMapping("/profil")
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getId());
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/profil")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/profile";
        }

        userService.update(userDetails.getId(), user);
        redirectAttributes.addFlashAttribute("message", "Profil bol aktualizovany");
        return "redirect:/profil";
    }

    @GetMapping("/profil/heslo")
    public String changePasswordPage() {
        return "user/change-password";
    }

    @PostMapping("/profil/heslo")
    public String changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Nove hesla sa nezhoduju");
            return "user/change-password";
        }

        if (newPassword.length() < 8) {
            model.addAttribute("error", "Heslo musi mat aspon 8 znakov");
            return "user/change-password";
        }

        userService.updatePassword(userDetails.getId(), newPassword);
        redirectAttributes.addFlashAttribute("message", "Heslo bolo zmenene");
        return "redirect:/profil";
    }
}
