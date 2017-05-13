package com.example.repository;

import com.example.domain.Questionnaire;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionnaireRepository extends CrudRepository<Questionnaire, Long> {

    List<Questionnaire> findByOrderByCreatedAtDesc();
}
