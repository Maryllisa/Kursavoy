package com.example.kursavoy.Model;

import com.example.kursavoy.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admim implements UserDetails{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_admin")
    private int idAdmin;
    @Basic
    @Column(name = "brth")
    private String brth;
    @Basic
    @Column(name = "e_mail")
    private String eMail;
    @Basic
    @Column(name = "login", unique = true)
    private String login;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "patronymic")
    private String patronymic;
    @Basic
    @Column(name = "surname")
    private String surname;
    @Basic
    @Column(name="job")
    private String jobs;
    @Basic
    @Column(name="active")
    private boolean active;
    private String activationCode;
    private String reset_password_token;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "role_admin", joinColumns = @JoinColumn(name = "id_admin"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "admin")
    private List<Forgingroup> forgingroup = new ArrayList<>();
    public void setForgingroupADD(Forgingroup f) {
        f.setAdmin(this);
        forgingroup.add(f);}
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "admim")
    private List<Image> images = new ArrayList<>();
    private Long previewImageId;
    private LocalDateTime dateOfCreated;


    @PrePersist
    private void init()
    {
        dateOfCreated = LocalDateTime.now();
    }
    public void addImagetoAdmin(Image image)
    {
        images.clear();
        image.setAdmim(this);
        images.add(image);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }



}
