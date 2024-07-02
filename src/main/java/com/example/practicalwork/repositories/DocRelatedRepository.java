package com.example.practicalwork.repositories;

import com.example.practicalwork.models.DocRelated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocRelatedRepository extends JpaRepository<DocRelated, Long> {

}
