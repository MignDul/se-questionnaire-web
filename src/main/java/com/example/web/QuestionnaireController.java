package com.example.web;

import com.example.domain.Answer;
import com.example.domain.Question;
import com.example.domain.Questionnaire;
import com.example.domain.Reply;
import com.example.repository.QuestionnaireRepository;
import com.example.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/questionnaires")
public class QuestionnaireController {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @GetMapping("/{id}")
    public ModelAndView getQuestionnaire(@PathVariable("id") Long id,
                                         @ModelAttribute("replyForm") ReplyForm replyForm) {
        Questionnaire questionnaire = questionnaireRepository.findOne(id);
        if (questionnaire == null) {
            throw new QuestionnaireNotFoundException(id);
        }
        return new ModelAndView("questionnaire/view", "questionnaire", questionnaire);
    }

    @PostMapping("/{id}")
    public ModelAndView createReply(@PathVariable("id") Long id,
                                    @Valid @ModelAttribute("replyForm") ReplyForm replyForm,
                                    BindingResult result) {
        Questionnaire questionnaire = questionnaireRepository.findOne(id);
        if (questionnaire == null) {
            throw new QuestionnaireNotFoundException(id);
        }

        boolean hasRequiredMissing = false;
        for (int i = 0; i < questionnaire.getQuestions().size(); i++) {
            Question question = questionnaire.getQuestions().get(i);
            ReplyFormItem item = replyForm.getItems().get(i);
            // Check if required questions are filled.
            if (question.isRequired()
                    && CollectionUtils.isEmpty(item.getSelectedOptions())
                    && StringUtils.isEmpty(item.getInputText())) {
                hasRequiredMissing = true;
            }
        }
        // If there are some required questions not filled, or validation error existed, show error message.
        if (hasRequiredMissing || result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("questionnaire/view");
            modelAndView.addObject("questionnaire", questionnaire);
            modelAndView.addObject("hasRequiredMissing", hasRequiredMissing);
            modelAndView.addObject("formErrors", result.getAllErrors());
            return modelAndView;
        }

        // Transform web form object (ReplyForm) to domain object (Reply).
        Reply reply = new Reply(questionnaire, new Date());
        Set<Answer> answers = new HashSet<>();
        for (int i = 0; i < questionnaire.getQuestions().size(); i++) {
            Question question = questionnaire.getQuestions().get(i);
            ReplyFormItem item = replyForm.getItems().get(i);

            if (!CollectionUtils.isEmpty(item.getSelectedOptions())) {
                List<Integer> selectedOptions = item.getSelectedOptions();
                for (int optionSequenceNumber : selectedOptions) {
                    Answer answer = new Answer(reply, question);
                    answer.setOptionSequenceNumber(optionSequenceNumber);
                    if (optionSequenceNumber == -1) {
                        answer.setText(item.getInputText());
                    }
                    answers.add(answer);
                }
            } else if (!StringUtils.isEmpty(item.getInputText())) {
                Answer answer = new Answer(reply, question);
                answer.setText(item.getInputText());
                answers.add(answer);
            }
        }
        reply.setAnswers(answers);
        // Save reply and related answers.
        replyRepository.save(reply);

        return new ModelAndView("redirect:/questionnaires/{id}/finished", "id", id);
    }

    @GetMapping("/{id}/finished")
    public ModelAndView getReplySubmitSuccess(@PathVariable("id") Long id) {
        return new ModelAndView("reply/success", "questionnaireId", id);
    }

    @GetMapping("/{id}/analysis")
    public ModelAndView getAnalysis(@PathVariable("id") Long id) {
        return new ModelAndView("reply/analysis");
    }
}
