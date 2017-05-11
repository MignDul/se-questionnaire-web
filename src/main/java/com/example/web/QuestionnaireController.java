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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView getQuestionnaire(@PathVariable("id") Long id) {
        Questionnaire questionnaire = questionnaireRepository.findOne(id);
        if (questionnaire == null) {
            throw new QuestionnaireNotFoundException(id);
        }

        ModelAndView modelAndView = new ModelAndView("questionnaire/view", "questionnaire", questionnaire);
        List<ReplyFormItem> items = new ArrayList<>();
        for (Question question : questionnaire.getQuestions()) {
            items.add(new ReplyFormItem(question.getId()));
        }
        ReplyForm replyForm = new ReplyForm(questionnaire.getId(), items);
        modelAndView.addObject("replyForm", replyForm);
        return modelAndView;
    }

    @PostMapping("/{id}")
    public ModelAndView createReply(@PathVariable("id") Long id,
                                    @Valid @ModelAttribute("replyForm") ReplyForm replyForm,
                                    BindingResult result,
                                    RedirectAttributes redirect) {
        Questionnaire questionnaire = null;
        if (replyForm.getQuestionnaireId() != null) {
            questionnaire = questionnaireRepository.findOne(replyForm.getQuestionnaireId());
        }
        if (questionnaire == null
                || CollectionUtils.isEmpty(replyForm.getItems())
                || questionnaire.getQuestions().size() != replyForm.getItems().size()) {
            throw new QuestionnaireNotFoundException(id);
        }

        boolean hasRequiredMissing = false;
        for (int i = 0; i < questionnaire.getQuestions().size(); i++) {
            Question question = questionnaire.getQuestions().get(i);
            ReplyFormItem item = replyForm.getItems().get(i);
            // Check id corresponds.
            if (item.getQuestionId() == null
                    || !question.getId().equals(item.getQuestionId())) {
                throw new QuestionnaireNotFoundException(id);
            }
            // Check if required questions are filled.
            if (question.isRequired()
                    && CollectionUtils.isEmpty(item.getSelectedOptions())
                    && StringUtils.isEmpty(item.getInputText())) {
                hasRequiredMissing = true;
            }

            if (item.getInputText() != null && "".equals(item.getInputText().trim())) {
                item.setInputText(null);
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

            Answer othersOptionAnswer = null;
            if (!CollectionUtils.isEmpty(item.getSelectedOptions())) {
                List<Integer> selectedOptions = item.getSelectedOptions();
                for (int optionSequenceNumber : selectedOptions) {
                    Answer answer = new Answer(reply, question);
                    answer.setOptionSequenceNumber(optionSequenceNumber);
                    answers.add(answer);
                    if (optionSequenceNumber == -1) {
                        othersOptionAnswer = answer;
                    }
                }
            }
            if (!StringUtils.isEmpty(item.getInputText())) {
                if (othersOptionAnswer != null) {
                    othersOptionAnswer.setText(item.getInputText());
                } else {
                    Answer answer = new Answer(reply, question);
                    answer.setText(item.getInputText());
                    answers.add(answer);
                }
            }
        }
        reply.setAnswers(answers);
        // Save reply and related answers.
        replyRepository.save(reply);

        redirect.addFlashAttribute("questionnaireId", id);
        return new ModelAndView("redirect:/questionnaires/finished");
    }

    @GetMapping("/finished")
    public String getReplySubmitSuccess() {
        return "reply/success";
    }

    @GetMapping("/{id}/analysis")
    public ModelAndView getAnalysis(@PathVariable("id") Long id) {
        return new ModelAndView("reply/analysis");
    }
}
