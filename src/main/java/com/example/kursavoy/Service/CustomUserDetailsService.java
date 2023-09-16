package com.example.kursavoy.Service;

import com.example.kursavoy.repo.AdminRepository;
import com.example.kursavoy.repo.StudentRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    public final AdminRepository adminRepository;
    public final StudentRepository studentRepository;
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        if(adminRepository.findByLogin(login)!=null) return adminRepository.findByLogin(login);
        else if (studentRepository.findByLogin(login)!=null) return studentRepository.findByLogin(login);
        else throw new UsernameNotFoundException("No user found with email:"+login);
    }
}
