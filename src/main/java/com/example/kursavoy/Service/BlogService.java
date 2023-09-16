package com.example.kursavoy.Service;

import com.example.kursavoy.Model.*;
import com.example.kursavoy.enums.Role;
import com.example.kursavoy.repo.BlogRepository;
import com.example.kursavoy.repo.DocumentRepository;
import com.example.kursavoy.repo.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogService {
    private final GroupRepository groupRepository;
    private final BlogRepository blogRepository;
    private final DocumentRepository documentRepository;

    public Iterable<Blog> readAllByGroup(int[] gr) {
        List<Blog> blogs = new ArrayList<>();
        for (int i = 0; i < gr.length; i++) {
            List<Blog> blogsList = blogRepository.findAllByGroup(gr[i]);
            for (int j = 0; j < blogsList.size(); j++) {
                blogs.add(blogsList.get(i));
            }
        }
        return blogs;
    }

    public Blog readBlog(long id) { return blogRepository.findById(id).orElse(null);}

    public boolean deleteBlog(long id) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog==null) return false;
        else {
            Document document = documentRepository.findById(blog.getDocument().getId()).orElse(null);
            if(document==null) return false;
            blogRepository.delete(blog);
            documentRepository.delete(document);

            return true;
        }

    }

    @SneakyThrows
    public Blog changeBlog(long id, String title, int group, String info, MultipartFile file) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog == null) return null;
        if(blog.getTitle().equals(title) == false && title.equals("") == false) blog.setTitle(title);
        if(blog.getInfo().equals(title) == false && info.equals("")== false) blog.setInfo(info);
        if(blog.getForgingroup().getIdForginGroup() != group && group!=0)
        {
            Forgingroup forgingroup = groupRepository.findById(group).orElse(null);
            if(forgingroup==null) return null;
            else {
                blog.setForgingroup(forgingroup);
            }
        }
        if(file.getSize()!=0)
        {
           blog.setDocument(addDocument(file));
        }
        blogRepository.save(blog);
        blog = blogRepository.findById(id).orElse(null);
        return blog;
    }

    public Blog AddBlog(String title, String info, int group, MultipartFile file) throws IOException {

        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setInfo(info);
        Forgingroup forgingroup = groupRepository.findById(group).orElse(null);
        if(forgingroup==null) return null;
        blog.setForgingroup(forgingroup);
        blogRepository.save(blog);
        Blog blogFromDB = blogRepository.findById(blog.getId_blog()).orElse(null);
        blog.setDocument(addDocument(file));
        blogRepository.save(blog);
        blog = blogRepository.findByTitleAndInfo(title, info);
        return blog;
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

    public Iterable<Blog> findByGroup(int idForginGroup) {
        return blogRepository.findAllByGroup(idForginGroup);
    }

    public Iterable<Blog> findByGroupFromA(int idForginGroup) {
        return blogRepository.findAllByGroupFromA(idForginGroup);
    }
    public Iterable<Blog> findByGroupToA(int idForginGroup) {
        return blogRepository.findAllByGroupToA(idForginGroup);
    }

    public Iterable<Blog> findByGroupSearch(int idForginGroup, String title) {
        return blogRepository.findAllByGroupSearch(idForginGroup, title);
    }

    public Iterable<Blog> findByGroupSearch(String title) {
        return blogRepository.findAllByGroupSearch(title);
    }

    public Iterable<Blog> findByGroupFromA() {
        return blogRepository.findAllByGroupFromA();
    }
    public Iterable<Blog> findByGroupToA() {
        return blogRepository.findAllByGroupToA();
    }
}
