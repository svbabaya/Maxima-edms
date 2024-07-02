package com.example.practicalwork.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (name = "template")
public class DocTemplate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private boolean removed;
    private String title;
    private String version;
    private DocTitle docTitle;

//    // TODO Delete document
//    @OneToOne (fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL,
//            mappedBy = "template")
//    private Document document;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id")
    private List<DocField> fields;

}
