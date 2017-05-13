package com.example.web;

import com.example.domain.Option;
import com.example.domain.Question;
import com.example.domain.Questionnaire;
import com.example.repository.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/create")
public class CreateQuestionnaireController {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @GetMapping
    public ModelAndView getCreateQuestionnaireForm(@ModelAttribute Questionnaire questionnaire) {
        return new ModelAndView("questionnaire/create");
    }

    @PostMapping
    public ModelAndView createQuestionnaire(@Valid @ModelAttribute Questionnaire questionnaire,
                                            BindingResult result,
                                            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("questionnaire/create");
            modelAndView.addObject("formErrors", result.getAllErrors());
            return modelAndView;
        }

        // Save data
        if (questionnaire.getQuestions() != null) {
            for (Question question : questionnaire.getQuestions()) {
                question.setQuestionnaire(questionnaire);
                if (question.getOptions() != null) {
                    for (Option option : question.getOptions()) {
                        option.setQuestion(question);
                    }
                }
            }
        }
        questionnaire.setCreatedAt(new Date());
        questionnaire = questionnaireRepository.save(questionnaire);

        redirect.addFlashAttribute("questionnaireId", questionnaire.getId());
        return new ModelAndView("redirect:/create/finished");
    }

    @GetMapping("/finished")
    public String getQuestionnaireCreateSuccess() {
        return "questionnaire/success";
    }
}
