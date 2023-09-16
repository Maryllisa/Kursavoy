package com.example.kursavoy.Controller;

import com.example.kursavoy.Model.Admim;
import com.example.kursavoy.Model.Document;
import com.example.kursavoy.Model.Image;
import com.example.kursavoy.Model.Students;
import com.example.kursavoy.Service.StudentService;
import com.example.kursavoy.repo.DocumentRepository;
import com.example.kursavoy.repo.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class StudentController {

    private final StudentService studentService;
    private final ImageRepository imageRepository;
    private final DocumentRepository documentRepository;

    @GetMapping("/")
    public String homeUser(Model model) {
        Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("student", student);
        return "/user/home"; }

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
    @PostMapping("/change/{id}")
    public String setAdminChange(@PathVariable int id,
                                 @RequestParam String login,
                                 @RequestParam String password,
                                 @RequestParam String surname,
                                 @RequestParam String name,
                                 @RequestParam String partonymic,
                                 @RequestParam String email,
                                 @RequestParam String bth,
                                 @RequestParam String level,
                                 @RequestParam("file") MultipartFile file,
                                 Model model) throws IOException {
        Students student = new Students();
        student.setIdStudents(id);
        student.setLogin(login);
        student.setPassword(password);
        student.setSurname(surname);
        student.setName(name);
        student.setPatronymic(partonymic);
        student.setEmail(email);
        student.setBrth(bth);
        student.setLevel(level);
        studentService.changeStudent(student, file);

        return "redirect:/login";
    }
    @GetMapping("/profile/{id}")
    public String profile(@PathVariable int id,
                          Model model) {
        Students students = studentService.findStudent(id);
        if(students!=null) model.addAttribute("student", students);
        return "user/profile";
    }
    @PostMapping("/remove/{id}")
    public String deleteAdmin(@PathVariable int id, Model model)
    {
        studentService.deleteStudent(id);
        return "redirect:/";
    }
    @GetMapping("/pdf/{id}")
    private ResponseEntity<?> getPDF(@PathVariable Long id)
    {
        Document document = documentRepository.findById(id).orElse(null);
        Base64.Decoder decoder = Base64.getDecoder();
        return ResponseEntity.ok()
                .header("fileName", document.getOriginalFileName())
                .contentType(MediaType.valueOf(document.getContentType()))
                .contentLength(document.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(decoder.decode(document.getBytes()))));
    }
}
