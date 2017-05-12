package com.example.web;

import com.example.domain.Option;
import com.example.domain.Question;
import com.example.domain.QuestionType;
import com.example.domain.Questionnaire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PrepareData {

    public static Questionnaire generateTestQuestionnaire() {
        Questionnaire questionnaire = new Questionnaire(2L);
        questionnaire.setTitle("A Test Questionnaire");
        questionnaire.setCreatedAt(new Date());

        Question phoneQuestion = new Question(5L);
        phoneQuestion.setQuestionnaire(questionnaire);
        phoneQuestion.setSequenceNumber(0);
        phoneQuestion.setType(QuestionType.SINGLE_LINE_TEXT);
        phoneQuestion.setContent("Phone number:");
        phoneQuestion.setRequired(true);

        Question genderQuestion = new Question(6L);
        phoneQuestion.setQuestionnaire(questionnaire);
        phoneQuestion.setSequenceNumber(1);
        genderQuestion.setType(QuestionType.SINGLE_CHOICE);
        genderQuestion.setContent("Gender:");
        List<Option> genderOptions = new ArrayList<>();
        genderOptions.add(newOption(genderQuestion, 0, "Male"));
        genderOptions.add(newOption(genderQuestion, 1, "Female"));
        genderQuestion.setOptions(genderOptions);
        genderQuestion.setHasOthersOption(true);
        genderQuestion.setOthersOptionText("Others");

        questionnaire.setQuestions(Arrays.asList(phoneQuestion, genderQuestion));
        return questionnaire;
    }

    /**
     * A helper function to create an option object
     *
     * @param question       related question object
     * @param sequenceNumber option's sequence number in the question
     * @param text           option's text
     * @return the created object
     */
    private static Option newOption(Question question, int sequenceNumber, String text) {
        Option option = new Option(question, sequenceNumber);
        option.setText(text);
        return option;
    }

    private PrepareData() {}
}
