package com.example.practicalwork.repositories;

import com.example.practicalwork.models.DocField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocFieldRepository extends JpaRepository<DocField, Long> {

    public DocField findDocFieldByName(String nameOfField);

}
