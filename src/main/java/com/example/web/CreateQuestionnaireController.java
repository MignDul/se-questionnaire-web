package com.example.web;

import com.example.repository.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/create")
public class CreateQuestionnaireController {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @GetMapping
    public ModelAndView getCreateQuestionnaireForm() {
        return new ModelAndView("questionnaire/create");
    }
}
