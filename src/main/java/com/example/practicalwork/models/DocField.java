package com.example.practicalwork.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "field")
public class DocField implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private boolean removed;
    private String name;
    private String type;
    private String placeholder;
    @Column(length = 5000)
    private String defaultValue;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
