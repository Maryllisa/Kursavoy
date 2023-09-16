package com.example.kursavoy.Controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@AllArgsConstructor
public class WorkController {

    @GetMapping("/admin/MyWork/")
    public String getWorkAdmin(Model model)
    {
        return "/admin/Work";
    }
}
