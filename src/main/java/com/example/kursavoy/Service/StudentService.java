package com.example.kursavoy.Service;

import com.example.kursavoy.Model.*;
import com.example.kursavoy.enums.Role;
import com.example.kursavoy.repo.FeeRepository;
import com.example.kursavoy.repo.GroupRepository;
import com.example.kursavoy.repo.ImageRepository;
import com.example.kursavoy.repo.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {
    private final FeeRepository feeRepository;
    private final StudentRepository studentRepository;
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
    public boolean creatStudent(Students students, MultipartFile file) throws IOException {
        if(studentRepository.findByLogin(students.getLogin())!=null) return false;
        students.setActive(true);
        students.getRoles().add(Role.USER);
        students.setPassword(passwordEncoder.encode(students.getPassword()));
        Image image;
        if(file.getSize()!=0){
            image = addImage(file);
            image.setPreviewImage(true);
            students.addImagetoStudent(image);
        }
        students.setActivationCode(UUID.randomUUID().toString());
        Students adminFromDB = studentRepository.save(students);
        adminFromDB.setPreviewImageId(adminFromDB.getImages().get(0).getId());
        studentRepository.save(students);

        if(!StringUtils.isEmpty(students.getEmail()))
        {
            String message = String.format("Здравствуйте, %s %s !\n" +
                            "Вами была пройдена регистрация, как студента в языковой школе ИнЯЗ. Пожалуйста подтвердите регистрацию перейдя по ссылке:\n" +
                            "http://localhost:4040/activate/user/%s",
                    students.getSurname(), students.getName(), students.getActivationCode()
            );
            mailSender.send(students.getEmail(), "Код активации", message);
        }
        return true;
    }
    @SneakyThrows
    public void updateResetPasswordToken(String token, String email)  {
        Students student = studentRepository.findByEmail(email).orElse(null);
        if (student != null) {
            student.setReset_password_token(token);
            studentRepository.save(student);
        } else {
            throw new Exception("Could not find any customer with the email " + email);
        }
    }

    public Students getByResetPasswordToken(String token) {
        return studentRepository.findByReset_password_token(token).orElse(null);
    }

    public void updatePassword(Students students, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        students.setPassword(encodedPassword);

        students.setReset_password_token(null);
        studentRepository.save(students);
    }
    public boolean changeStudent(Students students, MultipartFile file) throws IOException {
        Students studentOld = studentRepository.findById(students.getIdStudents()).orElse(null);
        if(studentOld.getLogin().equals(students.getLogin())==false) {
            if(studentRepository.findByLogin(students.getLogin())!=null) return false;
            studentOld.setLogin(students.getLogin());
        }
        if(students.getPassword().equals("")==false)
            studentOld.setPassword(passwordEncoder.encode(students.getPassword()));
        if(studentOld.getSurname().equals(students.getSurname())==false)
            studentOld.setSurname(students.getSurname());
        if(studentOld.getName().equals(students.getName())==false)
            studentOld.setName(students.getName());
        if(studentOld.getPatronymic().equals(students.getPatronymic())==false)
            studentOld.setPatronymic(students.getPatronymic());
        if(studentOld.getLevel().equals(students.getLevel())==false)
            studentOld.setLevel(students.getLevel());
        if(studentOld.getBrth().equals(students.getBrth())==false)
            studentOld.setBrth(students.getBrth());
        if(file.getSize()!=0){
            Image image;
            image = addImage(file, studentOld.getPreviewImageId());
            image.setPreviewImage(true);
            studentOld.addImagetoStudent(image);
            Students studentFromDB = studentRepository.save(studentOld);
            studentFromDB.setPreviewImageId(studentFromDB.getImages().get(0).getId());
        }
        studentRepository.save(studentOld);
        return true;
    }
    public Students findStudent(int id)
    {

        Students students = studentRepository.findById(id).orElse(null);
        return students;
    }
    public Iterable<Students> findByWithoutGroup()
    {
        return studentRepository.findStudentsWithoutGroup();
    }
    public Iterable<Students> findByWithGroup()
    {
        return studentRepository.findStudentsWithGroup();
    }
    public void deleteStudent(int id)
    {
        Students students = studentRepository.findById(id).orElse(null);
        if(students!=null) studentRepository.delete(students);
    }

    public Iterable<Students> readStudent() {
        Iterable<Students> students = studentRepository.findAll();
        return students;
    }

    public void deleteGroup(int id) {
        Students students = studentRepository.findById(id).orElse(null);
        students.setForgingroup(null);
        studentRepository.save(students);

    }

    public void addStudentInGroup(int id, String group) {
        Students students = studentRepository.findById(id).orElse(null);
        Forgingroup forgingroup = groupRepository.findById(Integer.valueOf(group)).orElse(null);
        students.setForgingroup(forgingroup);
        studentRepository.save(students);
    }

    public Iterable<Students> findByWithGroup(int idForginGroup) {
        Iterable<Students> students = studentRepository.findByGroup(idForginGroup);
        return students;
    }

    public List<Students> findStudent(String surname, String name, String patronymic, String brth) {
        return studentRepository.findBySNPAndB(surname, name, patronymic, brth);
    }

    public boolean changeStatusfeeFalse(int id, long id_fee) {
        Students students = studentRepository.findById(id).orElse(null);
        if (students == null) return false;
        students.setActive(false);
        Fee fee = feeRepository.findById(id_fee).orElse(null);
        if(fee == null) return false;
        fee.setStatus_fee(false);
        studentRepository.save(students);
        feeRepository.save(fee);
        return true;
    }
    public boolean changeStatusfeeTrue(int id, long id_fee) {
        Students students = studentRepository.findById(id).orElse(null);
        if (students == null) return false;
        students.setActive(true);
        Fee fee = feeRepository.findById(id_fee).orElse(null);
        if(fee == null) return false;
        fee.setStatus_fee(true);
        studentRepository.save(students);
        feeRepository.save(fee);
        return true;
    }

    public Iterable<Students> readAllStudentByGroup(int[] gr) {
        List<Students> students = new ArrayList<>();
        for (int i = 0; i < gr.length; i++) {
            List<Students> studentsList = studentRepository.findAllByGroup(gr[i]);
            for (int j = 0; j < studentsList.size(); j++) {
                students.add(studentsList.get(j));
            }
        }
        return students;
    }

    public boolean changeAction(int id) {
        Students students = studentRepository.findById(id).orElse(null);
        if(students==null) return false;
        if(students.isEnabled()==true) students.setActive(false);
        else students.setActive(true);
        studentRepository.save(students);
        return true;
    }

    public Iterable<Students> readStudentByGroup(int idForginGroup) {
        return studentRepository.findByGroup(idForginGroup);
    }
    public boolean activateAdmin(String code) {
        Students students = studentRepository.findByActivationCode(code);
        if(students==null) return false;
        students.setActivationCode(null);
        studentRepository.save(students);
        return true;
    }

    @SneakyThrows
    public boolean sendEmail(String email)
    {
        Students student = studentRepository.findByEmail(email).orElse(null);
        if (student==null) return false;
        String token = RandomString.make(30);
        updateResetPasswordToken(token, email);
        String message = String.format("Здравствуйте, %s %s !\n" +
                        "Вами была пройдена попытка сброса пароля на сайте языковой школы ИнЯЗ. Пожалуйста перейдите по этой ссылке для сброса пароля:\n" +
                        "http://localhost:4040/newPasswird/user/%s",
                student.getSurname(), student.getName(), token
        );
        mailSender.send(student.getEmail(), "Сброс пароля", message);
        return true;
    }
}
