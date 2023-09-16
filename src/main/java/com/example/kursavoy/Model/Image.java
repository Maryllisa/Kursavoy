package com.example.kursavoy.Model;
;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Base64;


@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "originalFileName")
    private String originalFileName;
    @Column(name = "size")
    private Long size;
    @Column(name = "contentType")
    private String contentType;
    @Column(name = "isPreviewImage")
    private boolean isPreviewImage;
    @Lob
    private String bytes;

    public String decoder(String encoded)
    {
        Base64.Decoder decoder = Base64.getDecoder();
        String decoded = new String(decoder.decode(encoded));
        return decoded;
    }
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Admim admim;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Students students;
}
