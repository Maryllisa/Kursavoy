package com.example.kursavoy.Controller;
import com.example.kursavoy.Config.Utility;
import com.example.kursavoy.Service.*;
import com.example.kursavoy.repo.ImageRepository;
import com.example.kursavoy.repo.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.bytebuddy.utility.RandomString;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.kursavoy.Model.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.util.Base64;



@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class MainController {

    private final ReaderService readerService;
    private final ImageRepository imageRepository;
    private final CoursService coursService;
    private final StudentService studentService;
    private final AdminService adminService;
    private final MailSender mailSender;

    @GetMapping("/")
    public String getHome(Model model) {
        return "home";
    }

    @GetMapping("/about/{id}")
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

    @GetMapping("/registr") public String getRegistr(Model model) {return "registr";}
    @GetMapping("/activate/admin/{code}")
    public String activate(Model model, @PathVariable String code)
    {
        boolean isActivated = adminService.activateAdmin(code);
        if(isActivated) model.addAttribute("message", "Почта подтверждена");
        else model.addAttribute("msg", "Код активации не найден");
        return "login";
    }
    @GetMapping("/activate/user/{code}")
    public String activateUser(Model model, @PathVariable String code)
    {
        boolean isActivated = adminService.activateAdmin(code);
        if(isActivated) model.addAttribute("message", "Почта подтверждена");
        else model.addAttribute("msg", "Код активации не найден");
        return "login";
    }
    @GetMapping("/login/forgetPassword/")
    public String forgetPassword(Model model){ return "ForgetPassword";}
    @SneakyThrows
    @PostMapping("/login/forgot_password/")
    public String processForgotPassword(@RequestParam String email, @RequestParam String role, Model model) {

        if(role.equals("admin")) {
            if (!adminService.sendEmail(email)) {
                model.addAttribute("msg", "Пользователя с таким адресом не существует!!");
                return "login";
            }
        }
        else if (role.equals("student")) {
            if (!studentService.sendEmail(email)) {
                model.addAttribute("msg", "Пользователя с таким адресом не существует!!");
                return "login";
            }
        }
        else {
            model.addAttribute("msg", "Выберите роль!");
            return "ForgetPassword";
        }
        return "login";
    }
    @GetMapping("/newPasswird/admin/{token}")
    public String showResetPasswordForm(@PathVariable String token, Model model) {
        Admim admin = adminService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        model.addAttribute("user", admin);

        if (admin == null) {
            model.addAttribute("msg", "Invalid Token");
            return "message";
        }

        return "reset_password_form";
    }
    @GetMapping("/newPasswird/user/{token}")
    public String showResetPasswordFormStudent(@PathVariable String token, Model model) {
        Students student = studentService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        model.addAttribute("user", student);
        System.out.println(student.getAuthorities().iterator());
        System.out.println(student.getAuthorities());
        System.out.println(student.getAuthorities().stream().iterator());
        System.out.println(student.getAuthorities().toString());
        if (student == null) {
            model.addAttribute("msg", "Invalid Token");
            return "message";
        }

        return "reset_password_form";
    }
    @PostMapping("/reset_password/admin/{id}")
    public String processResetPassword(@PathVariable int id,
                                       @RequestParam String password,
                                       @RequestParam String token,
                                       Model model) {

        Admim admim = adminService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (admim == null) {
            model.addAttribute("msg", "Invalid Token");
            return "message";
        } else {
            adminService.updatePassword(admim, password);

            model.addAttribute("msg", "You have successfully changed your password.");
        }

        return "login";
    }
    @PostMapping("/reset_password/user/{id}")
    public String processResetPasswordStudent(@PathVariable int id,
                                       @RequestParam String password,
                                       @RequestParam String token,
                                       Model model) {

        Students students = studentService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (students == null) {
            model.addAttribute("msg", "Invalid Token");
            return "message";
        } else {
            studentService.updatePassword(students, password);

            model.addAttribute("msg", "You have successfully changed your password.");
        }

        return "login";
    }

    @SneakyThrows
    @PostMapping("/registr")
    public String setRegistrStudent(@RequestParam String login,
                             @RequestParam String password,
                             @RequestParam String surname,
                             @RequestParam String name,
                             @RequestParam String partonymic,
                             @RequestParam String email,
                             @RequestParam String bth,
                             @RequestParam String level,
                             @RequestParam("file") MultipartFile file,
                             Model model) {
        Students students = new Students();
        students.setLogin(login);
        students.setPassword(password);
        students.setSurname(surname);
        students.setName(name);
        students.setPatronymic(partonymic);
        students.setEmail(email);
        students.setBrth(bth);
        students.setLevel(level);
       if(!studentService.creatStudent(students, file))
       {
           model.addAttribute("msg", "Пользователь с таким логином уже существует");
           return "registr";
       }
        return "redirect:/login";
    }


    @SneakyThrows
    @PostMapping("/registr/admin")
    public String cheakAdmin(@RequestParam String code, Model model){
        if(code.equals("31277")==true) return "redirect:/registr/admin/true" ;
        else
        {
            model.addAttribute("msg", "Неверный код доступа");
            return "registr";
        }
    }
    @GetMapping("/registr/admin/true") String addAdmin(Model model) {return "admin/registr";}

    @SneakyThrows
    @PostMapping("/registr/admin/true")
    public String setRegistr(@RequestParam String login,
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
        admim.setLogin(login);
        admim.setPassword(password);
        admim.setSurname(surname);
        admim.setName(name);
        admim.setPatronymic(partonymic);
        admim.setEMail(email);
        admim.setBrth(bth);
        admim.setJobs(job);
        if(!adminService.creatAdmin(admim, file))
        {
            model.addAttribute("msg", "Пользователь с таким логином уже существует");
            return "admin/registr";
        }

        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login( Model model) {
        return "login";
    }



    @GetMapping("/about")
    public  String readAdmin(Model model)
    {
        Iterable<Admim> admin = readerService.readAdmin();
        Iterable<Image> images = readerService.readImg();
        Iterable<Cours> cours = coursService.readCours();
        model.addAttribute("cours", cours);
        model.addAttribute("admim", admin);
        model.addAttribute("images", images);
        return "About";
    }

    @GetMapping("/cours")
    public String getCours(Model model)
    {
        Iterable<Cours> cours = coursService.readCours();
        model.addAttribute("cours", cours);
        return "Cours";
    }
    @GetMapping("/cn")
    public String getCN(Model model)
    {
        Iterable<Cours> cours = coursService.readCours();
        model.addAttribute("cours", cours);
        return "info";
    }
    @GetMapping("/login/error")
    public String errorLogin(Model model)
    {
        model.addAttribute("msg", "Ошибка!");
        return "login";
    }


}