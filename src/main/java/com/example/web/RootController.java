package com.example.web;

import com.example.domain.Questionnaire;
import com.example.repository.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class RootController {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @GetMapping("/")
    public ModelAndView getIndex() {
        List<Questionnaire> questionnaires = questionnaireRepository.findByOrderByCreatedAtDesc();
        return new ModelAndView("index", "questionnaires", questionnaires);
    }

    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }
}
