package com.example.repository;

import com.example.domain.Questionnaire;
import com.example.domain.Reply;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReplyRepository extends CrudRepository<Reply, Long> {

    List<Reply> findAllByQuestionnaire(Questionnaire questionnaire, Sort sort);
}
