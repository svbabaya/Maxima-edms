package com.example.practicalwork.repositories;

import com.example.practicalwork.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocRepository extends JpaRepository<Document, Long> {

}
