package com.example.kursavoy.Controller;

import com.example.kursavoy.Model.Admim;
import com.example.kursavoy.Model.Image;
import com.example.kursavoy.Service.AdminService;
import com.example.kursavoy.repo.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.Base64;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ImageRepository imageRepository;

    @GetMapping("/")
    public String getAdminHome(Model model, Principal principal)
    {   Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("admin", admin);
        return "/admin/home";

    }
    @GetMapping("/{id}")
    private ResponseEntity<?> getImageByID(@PathVariable Long id)
    {
        Image image = imageRepository.findById(id).orElse(null);
        Base64.Decoder decoder = Base64.getDecoder();
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(decoder.decode(image.getBytes()))));
    }
    @GetMapping("/profile/{id}")
    public String profile(@PathVariable int id,
                          Model model) {
        Admim admin = adminService.findAdmin(id);
        if(admin!=null) model.addAttribute("admin", admin);
        return "admin/profile";
    }
    @SneakyThrows
    @PostMapping("/change/{id}")
    public String setAdminChange(@PathVariable int id,
                             @RequestParam String login,
                             @RequestParam String password,
                             @RequestParam String surname,
                             @RequestParam String name,
                             @RequestParam String partonymic,
                             @RequestParam String email,
                             @RequestParam String bth,
                             @RequestParam String job,
                             @RequestParam("file") MultipartFile file,
                             Model model) {
        Admim admim = new Admim();
        admim.setIdAdmin(id);
        admim.setLogin(login);
        admim.setPassword(password);
        admim.setSurname(surname);
        admim.setName(name);
        admim.setPatronymic(partonymic);
        admim.setEMail(email);
        admim.setBrth(bth);
        admim.setJobs(job);
        adminService.changeAdmin(admim, file);

        return "redirect:/login";
    }
    @PostMapping("/remove/{id}")
    public String deleteAdmin(@PathVariable int id, Model model)
    {
        adminService.deleteAdmin(id);
        return "redirect:/";
    }
}
