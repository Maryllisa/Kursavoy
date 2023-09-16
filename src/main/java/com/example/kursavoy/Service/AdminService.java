package com.example.kursavoy.Service;

import com.example.kursavoy.Config.Utility;
import com.example.kursavoy.Model.*;
import com.example.kursavoy.enums.Role;
import com.example.kursavoy.repo.AdminRepository;
import com.example.kursavoy.repo.CoursRepository;
import com.example.kursavoy.repo.GroupRepository;
import com.example.kursavoy.repo.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final CoursRepository coursRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;
    private final GroupRepository groupRepository;
    private final MailSender mailSender;

    public Image addImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        Base64.Encoder encoder = Base64.getEncoder();
        String encoded = encoder.encodeToString(file.getBytes());
        image.setBytes(encoded);
        return image;
    }
    public void updateResetPasswordToken(String token, String email) throws Exception {
        Admim admim = adminRepository.findByEmail(email).orElse(null);
        if (admim != null) {
            admim.setReset_password_token(token);
            adminRepository.save(admim);
        } else {
            throw new Exception("Could not find any customer with the email " + email);
        }
    }
    @SneakyThrows
    public boolean sendEmail(String email)
    {
        Admim admin = adminRepository.findByEmail(email).orElse(null);
        if (admin==null) return false;
        String token = RandomString.make(30);
        updateResetPasswordToken(token, email);
        String message = String.format("Здравствуйте, %s %s !\n" +
                        "Вами была пройдена попытка сброса пароля на сайте языковой школы ИнЯЗ. Пожалуйста перейдите по этой ссылке для сброса пароля:\n" +
                        "http://localhost:4040/newPasswird/admin/%s",
                admin.getSurname(), admin.getName(), token
        );
        mailSender.send(admin.getEMail(), "Сброс пароля", message);
        return true;
    }

    public Admim getByResetPasswordToken(String token) {
        return adminRepository.findByReset_password_token(token).orElse(null);
    }

    public void updatePassword(Admim admim, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        admim.setPassword(encodedPassword);

        admim.setReset_password_token(null);
        adminRepository.save(admim);
    }
    public Image addImage(MultipartFile file, Long id) throws IOException {
        Image images = imageRepository.findById(id).orElse(null);
        if(images!=null) imageRepository.delete(images);
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        Base64.Encoder encoder = Base64.getEncoder();
        String encoded = encoder.encodeToString(file.getBytes());
        image.setBytes(encoded);
        return image;
    }
    public boolean creatAdmin(Admim admin, MultipartFile file) throws IOException {
        if(adminRepository.findByLogin(admin.getLogin())!=null) return false;
        admin.setActive(true);
         admin.getRoles().add(Role.ADMIN);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        Image image;
        if(file.getSize()!=0){
            image = addImage(file);
            image.setPreviewImage(true);
            admin.addImagetoAdmin(image);
        }
            admin.setActivationCode(UUID.randomUUID().toString());
            log.info("Ну типо: {}", admin.getLogin(), admin.getPassword());
            Admim adminFromDB = adminRepository.save(admin);
            adminFromDB.setPreviewImageId(adminFromDB.getImages().get(0).getId());
            adminRepository.save(admin);
            if(!StringUtils.isEmpty(admin.getEMail()))
            {
                String message = String.format("Здравствуйте, %s %s !\n" +
                        "Вами была пройдена регистрация, как преподавателя в языковой школе ИнЯЗ. Пожалуйста подтвердите регистрацию перейдя по ссылке:\n" +
                        "http://localhost:4040/activate/admin/%s",
                        admin.getSurname(), admin.getName(), admin.getActivationCode()
                );
                mailSender.send(admin.getEMail(), "Код активации", message);
            }
        return true;
    }
    public boolean changeAdmin(Admim admin, MultipartFile file) throws IOException {
        Admim adminOld = adminRepository.findById(admin.getIdAdmin()).orElse(null);
        if(adminOld.getLogin().equals(admin.getLogin())==false) {
            if(adminRepository.findByLogin(admin.getLogin())!=null) return false;
            adminOld.setLogin(admin.getLogin());
        }
        if(admin.getPassword().equals("")==false)
            adminOld.setPassword(passwordEncoder.encode(admin.getPassword()));
        if(adminOld.getSurname().equals(admin.getSurname())==false)
            adminOld.setSurname(admin.getSurname());
        if(adminOld.getName().equals(admin.getName())==false)
            adminOld.setName(admin.getName());
        if(adminOld.getPatronymic().equals(admin.getPatronymic())==false)
            adminOld.setPatronymic(admin.getPatronymic());
        if(adminOld.getJobs().equals(admin.getJobs())==false)
            adminOld.setJobs(admin.getJobs());
        if(adminOld.getBrth().equals(admin.getBrth())==false)
            adminOld.setBrth(admin.getBrth());
        if(file.getSize()!=0){
            Image image;
            image = addImage(file, adminOld.getPreviewImageId());
            image.setPreviewImage(true);
            adminOld.addImagetoAdmin(image);
            Admim adminFromDB = adminRepository.save(adminOld);
            adminFromDB.setPreviewImageId(adminFromDB.getImages().get(0).getId());
        }
        adminRepository.save(adminOld);
        return true;
    }
    public Admim getAdminByPrincipal(Principal principal) {
        if (principal == null) return new Admim();
        return adminRepository.findByLogin(principal.getName());
    }
    public Admim findAdmin(int id)
    {
        Admim admim = adminRepository.findById(id).orElse(null);
        return admim;
    }
    public void deleteAdmin(int id)
    {
     Admim admin = adminRepository.findById(id).orElse(null);
     if(admin!=null) adminRepository.delete(admin);
    }

    public Iterable<Admim> readAdmin() {
        Iterable<Admim> admims = adminRepository.findAll();
        return admims;
    }

    public void addNewGroup(int id, int idAdmin) {
        Admim adminFromDB = adminRepository.findById(idAdmin).orElse(null);
        Forgingroup forgingroupFromDB = groupRepository.findById(id).orElse(null);
        adminFromDB.setForgingroupADD(forgingroupFromDB);
        forgingroupFromDB.setAdmin(adminFromDB);
        adminRepository.save(adminFromDB);
        groupRepository.save(forgingroupFromDB);
    }
    public void deleteGroup(int id)
    {
        Forgingroup forgingroup = groupRepository.findById(id).orElse(null);
        forgingroup.setAdmin(null);
        groupRepository.save(forgingroup);

    }

    public Admim getMyCours(int idAdmin) {
        return adminRepository.findById(idAdmin).orElse(null);
    }

    public boolean activateAdmin(String code) {
        Admim admin = adminRepository.findByActivationCode(code);
        if(admin==null) return false;
        admin.setActivationCode(null);
        adminRepository.save(admin);
        return true;
    }

}
