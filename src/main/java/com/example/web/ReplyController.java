package com.example.web;

import com.example.domain.*;
import com.example.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("/replies")
public class ReplyController {

    @Autowired
    private ReplyRepository replyRepository;

    @GetMapping("/{id}")
    public ModelAndView getReply(@PathVariable("id") Long id) {
        Reply reply = replyRepository.findOne(id);
        if (reply == null) {
            throw new ReplyNotFoundException(id);
        }

        Questionnaire questionnaire = reply.getQuestionnaire();
        ModelAndView modelAndView = new ModelAndView("reply/view", "questionnaire", questionnaire);

        List<ReplyFormItem> items = new ArrayList<>();
        for (int i = 0; i < questionnaire.getQuestions().size(); i++) {
            items.add(new ReplyFormItem());
        }

        // Transform domain object (Reply) to request attribute object (ReplyForm).
        Set<Answer> answers = reply.getAnswers();
        for (Answer answer : answers) {
            ReplyFormItem item = items.get(answer.getQuestion().getSequenceNumber());
            if (answer.getOptionSequenceNumber() != null) {
                List<Integer> selectedOptions = item.getSelectedOptions();
                if (selectedOptions == null) {
                    selectedOptions = new ArrayList<>();
                    item.setSelectedOptions(selectedOptions);
                }
                selectedOptions.add(answer.getOptionSequenceNumber());
            }
            if (answer.getText() != null) {
                item.setInputText(answer.getText());
            }
        }
        ReplyForm replyForm = new ReplyForm(items);
        modelAndView.addObject("replyForm", replyForm);
        return modelAndView;
    }
}
