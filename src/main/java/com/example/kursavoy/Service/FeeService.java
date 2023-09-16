package com.example.kursavoy.Service;

import com.example.kursavoy.Model.Document;
import com.example.kursavoy.Model.Fee;
import com.example.kursavoy.Model.Students;
import com.example.kursavoy.repo.DocumentRepository;
import com.example.kursavoy.repo.FeeRepository;
import com.example.kursavoy.repo.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeeService {
    private final FeeRepository feeRepository;
    private final DocumentRepository documentRepository;
    private final StudentRepository studentRepository;

    public Iterable<Fee> readAllFeeByGroup(int[] gr) {
        List<Fee> fees = new ArrayList<>();
        for (int i = 0; i < gr.length; i++) {
            List<Fee> feeList = feeRepository.findAllByGroup(gr[i]);
            for (int j = 0; j < feeList.size(); j++) {
                fees.add(feeList.get(j));
            }
        }
        return fees;
    }

    public Iterable<Fee> readByStudent(int idStudents) {
        return feeRepository.findByStudent(idStudents);
    }
    public Document addDocument(MultipartFile file) throws IOException {
        Document document = new Document();
        document.setName(file.getName());
        document.setOriginalFileName(file.getOriginalFilename());
        document.setContentType(file.getContentType());
        document.setSize(file.getSize());
        Base64.Encoder encoder = Base64.getEncoder();
        String encoded = encoder.encodeToString(file.getBytes());
        document.setBytes(encoded);
        documentRepository.save(document);
        return document;
    }
    public void addFee(int idStudents, Date date, MultipartFile file) throws IOException {
        Fee fee = new Fee();
        Students students = studentRepository.findById(idStudents).orElse(null);
        fee.setDate(date);
        fee.setStudentsByIdStudent(students);
        fee.setDocument(addDocument(file));
        documentRepository.save(fee.getDocument());
        feeRepository.save(fee);

    }

    public Iterable<Fee> readByStudentByDateAsc(int idStudents) {
        return feeRepository.findByStudentByDateAsc(idStudents);
    }
    public Iterable<Fee> readByStudentByDateDesc(int idStudents) {
        return feeRepository.findByStudentByDateDesc(idStudents);
    }

    public Object readAllFeeByGroupBySNP(int[] gr, String surname, String name, String patronymic) {
        List<Fee> fees = new ArrayList<>();
        for (int i = 0; i < gr.length; i++) {
            List<Fee> feeList = feeRepository.findAllByGroupBySNP(gr[i], surname, name, patronymic);
            for (int j = 0; j < feeList.size(); j++) {
                fees.add(feeList.get(i));
            }
        }
        return fees;
    }
}
