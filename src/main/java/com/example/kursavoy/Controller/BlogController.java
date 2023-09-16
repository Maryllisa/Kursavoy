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
@RequestMapping("/admin/blog")
public class BlogController {

    private final BlogService blogService;
    private final GroupService groupService;
    private final DocumentRepository documentRepository;

    @GetMapping("/")
    public String getBlogs(Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Forgingroup> forgingroupList = groupService.findAllAdminsGroup(admin.getIdAdmin());
        int length = forgingroupList.size();
        int []gr = new int[length];
        for (int i = 0; i < length; i++) {
            gr[i] = forgingroupList.get(i).getIdForginGroup();
        }
        Iterable<Blog> blogs = blogService.readAllByGroup(gr);
        model.addAttribute("admin", admin);
        model.addAttribute("blog", blogs);
        return "blog";
    }
    @GetMapping("/search/")
    public String getBlogsSearch(@RequestParam String title, Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Iterable<Blog> blogs = blogService.findByGroupSearch(title);
        model.addAttribute("admin", admin);
        model.addAttribute("blog", blogs);
        return "blog";

    }
    @GetMapping("/sort/")
    public String getBlogsSort(@RequestParam String sort, Model model)
    {
        if(sort.equals("sortByFromA"))
        {
            Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Iterable<Blog> blogs = blogService.findByGroupFromA();
            model.addAttribute("admin", admin);
            model.addAttribute("blog", blogs);
            return "admin/blog";
        }
        else if(sort.equals("sortByToA"))
        {
            Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Iterable<Blog> blogs = blogService.findByGroupToA();
            model.addAttribute("admin", admin);
            model.addAttribute("blog", blogs);
            return "admin/blog";
        }
        else  return "redirect:/admin/blog/";
    }
    @GetMapping("/{id}")
    public String getBlog(@PathVariable long id, Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Blog blogs = blogService.readBlog(id);
        model.addAttribute("admin", admin);
        model.addAttribute("blog", blogs);
        return "admin/blog_info";
    }
    @PostMapping("/change/{id}")
    public String AddChangeBlog(@PathVariable long id,
                                @RequestParam String title,
                                @RequestParam String info,
                                @RequestParam MultipartFile file,
                                @RequestParam int group,
                                Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Blog blogs = blogService.changeBlog(id, title, group, info,  file);
        model.addAttribute("user", admin);
        model.addAttribute("blog", blogs);
        return "redirect:/admin/blog/";
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
