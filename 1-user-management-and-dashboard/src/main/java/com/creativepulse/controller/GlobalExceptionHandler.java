package com.creativepulse.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Turns unexpected errors into a friendly message instead of a white error page. */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleNotFound(IllegalArgumentException ex, RedirectAttributes ra) {
        ra.addFlashAttribute("error", ex.getMessage());
        return "redirect:/dashboard";
    }

    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public String handleBigFile(RedirectAttributes ra) {
        ra.addFlashAttribute("error", "The selected file is too large. Maximum allowed size is 10 MB.");
        return "redirect:/advertisements";
    }
}
