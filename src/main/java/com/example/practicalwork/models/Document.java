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
@Table (name = "document")
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private boolean removed;
    private String number;
    private DocTitle docTitle;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    @JoinColumn (name = "document_id")
    private List<DocRelated> docRelatedList;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private DocFile file;

//    // TODO Delete template
//    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "template_id")
//    private DocTemplate template;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id")
    private List<DocField> fields;

//    // TODO Fix contractor
//    @ManyToMany (mappedBy = "documents", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//    private List<Contractor> contractors;

}
