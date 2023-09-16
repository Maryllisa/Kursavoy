package com.example.kursavoy.Controller;

import com.example.kursavoy.Model.*;
import com.example.kursavoy.Service.BlogService;
import com.example.kursavoy.Service.GroupService;
import com.example.kursavoy.repo.DocumentRepository;
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
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/blog")
public class BlogStudentController {

    private final BlogService blogService;
    private final GroupService groupService;
    private final DocumentRepository documentRepository;

    @GetMapping("/")
    public String getBlogs(Model model)
    {
        Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(student.getForgingroup()!=null) {
            Iterable<Blog> blogs = blogService.findByGroup(student.getForgingroup().getIdForginGroup());
            model.addAttribute("blog", blogs);
        }
        else {
            Blog blogs = null;
            model.addAttribute("blog", blogs);
        }
        model.addAttribute("student", student);
        return "user/blog";
    }
    @GetMapping("/search/")
    public String getBlogsSearch(@RequestParam String title, Model model)
    {
            Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Iterable<Blog> blogs = blogService.findByGroupSearch(student.getForgingroup().getIdForginGroup(), title);
            model.addAttribute("student", student);
            model.addAttribute("blog", blogs);
            return "user/blog";

    }
    @GetMapping("/sort/")
    public String getBlogsSort(@RequestParam String sort, Model model)
    {
        if(sort.equals("sortByFromA"))
        {
            Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Iterable<Blog> blogs = blogService.findByGroupFromA(student.getForgingroup().getIdForginGroup());
            model.addAttribute("student", student);
            model.addAttribute("blog", blogs);
            return "user/blog";
        }
        else if(sort.equals("sortByToA"))
        {
            Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Iterable<Blog> blogs = blogService.findByGroupToA(student.getForgingroup().getIdForginGroup());
            model.addAttribute("student", student);
            model.addAttribute("blog", blogs);
            return "user/blog";
        }
        else  return "redirect:/user/blog/";
    }
    @GetMapping("/{id}")
    public String getBlog(@PathVariable long id, Model model)
    {
        Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Blog blogs = blogService.readBlog(id);
        model.addAttribute("student", student);
        model.addAttribute("blog", blogs);
        return "user/blog_info";
    }
    @GetMapping("/pdf/{id}")
    private ResponseEntity<?> getImageByID(@PathVariable Long id)
    {
        Document document = documentRepository.findById(id).orElse(null);
        Base64.Decoder decoder = Base64.getDecoder();
        return ResponseEntity.ok()
                .header("fileName", document.getOriginalFileName())
                .contentType(MediaType.valueOf(document.getContentType()))
                .contentLength(document.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(decoder.decode(document.getBytes()))));
    }
    @SneakyThrows
    @PostMapping("/add/")
    public String AddBlog(      @RequestParam String title,
                                @RequestParam String info,
                                @RequestParam MultipartFile file,
                                @RequestParam int group,
                                Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Blog blogs = blogService.AddBlog(title, info, group, file);
        model.addAttribute("user", admin);
        model.addAttribute("blog", blogs);
        return "redirect:/admin/blog/";
    }
    @GetMapping("/delete/{id}")
    public String AddChangeBlog(@PathVariable long id, Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        blogService.deleteBlog(id);
        model.addAttribute("user", admin);
        return "redirect:/admin/blog/";
    }
}
