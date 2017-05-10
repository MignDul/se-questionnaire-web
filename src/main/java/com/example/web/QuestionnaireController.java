package com.example.web;

import com.example.domain.Questionnaire;
import com.example.repository.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/questionnaires")
public class QuestionnaireController {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @GetMapping("/{id}")
    public ModelAndView get(@PathVariable("id") Long id) {
        Questionnaire questionnaire = questionnaireRepository.findOne(id);
        return new ModelAndView("questionnaire/view", "questionnaire", questionnaire);
    }
}
