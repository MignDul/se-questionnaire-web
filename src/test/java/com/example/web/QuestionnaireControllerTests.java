package com.example.web;

import com.example.domain.Option;
import com.example.domain.Question;
import com.example.domain.QuestionType;
import com.example.domain.Questionnaire;
import com.example.repository.QuestionnaireRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(QuestionnaireController.class)
public class QuestionnaireControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private QuestionnaireRepository questionnaireRepository;

    @Test
    public void testGet() throws Exception {
        Questionnaire questionnaire = new Questionnaire("A Test Questionnaire", null, new Date());
        List<Question> questions = new ArrayList<>();

        Question phoneQuestion = new Question(questionnaire, 0);
        phoneQuestion.setType(QuestionType.SINGLE_LINE_TEXT);
        phoneQuestion.setTitle("Phone number:");
        phoneQuestion.setRequired(true);

        Question genderQuestion = new Question(questionnaire, 0);
        genderQuestion.setType(QuestionType.SINGLE_CHOICE);
        genderQuestion.setTitle("Gender:");
        List<Option> genderOptions = new ArrayList<>();
        genderOptions.add(newOption(genderQuestion, 0, "Male"));
        genderOptions.add(newOption(genderQuestion, 1, "Female"));
        genderQuestion.setOptions(genderOptions);
        genderQuestion.setHasOthersOption(true);
        genderQuestion.setOthersOptionText("Others");

        questions.add(phoneQuestion);
        questions.add(genderQuestion);
        questionnaire.setQuestions(questions);

        given(questionnaireRepository.findOne(2L)).willReturn(questionnaire);

        mvc.perform(get("/questionnaires/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("A Test Questionnaire")))
                .andExpect(content().string(containsString("required")))
                .andExpect(content().string(containsString("Phone number:")))
                .andExpect(content().string(containsString("Gender:")))
                .andExpect(content().string(containsString("Male")))
                .andExpect(content().string(containsString("Others")));
    }

    /**
     * A helper function to create an option object
     * @param question related question object
     * @param sequenceNumber option's sequence number in the question
     * @param text option's text
     * @return the created object
     */
    private Option newOption(Question question, int sequenceNumber, String text) {
        Option option = new Option(question, sequenceNumber);
        option.setText(text);
        return option;
    }
}
