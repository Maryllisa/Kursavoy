package com.example.kursavoy.Service;

import com.example.kursavoy.Model.Image;
import com.example.kursavoy.Model.Admim;
import com.example.kursavoy.enums.Role;
import com.example.kursavoy.repo.AdminRepository;
import com.example.kursavoy.repo.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReaderService {

    private final AdminRepository adminRepository;
    private final ImageRepository imageRepository;

    public Iterable<Admim> readAdmin()
    {
        Iterable<Admim> ad = adminRepository.findAll();
        return ad;
    }
    public  Iterable<Image>  readImg()
    {
        Iterable<Image> images = imageRepository.findAll();
        return  images;
    }
}

