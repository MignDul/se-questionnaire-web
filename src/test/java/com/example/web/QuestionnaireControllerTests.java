package com.example.web;

import com.example.domain.*;
import com.example.repository.QuestionnaireRepository;
import com.example.repository.ReplyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(QuestionnaireController.class)
public class QuestionnaireControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private QuestionnaireRepository questionnaireRepository;

    @MockBean
    private ReplyRepository replyRepository;

    private Questionnaire questionnaire;

    @Before
    public void setUp() throws Exception {
        Questionnaire questionnaire = new Questionnaire("A Test Questionnaire", null, new Date()) {
            @Override
            public Long getId() {
                return 2L;
            }
        };

        Question phoneQuestion = new Question(questionnaire, 0) {
            @Override
            public Long getId() {
                return 5L;
            }
        };
        phoneQuestion.setType(QuestionType.SINGLE_LINE_TEXT);
        phoneQuestion.setTitle("Phone number:");
        phoneQuestion.setRequired(true);

        Question genderQuestion = new Question(questionnaire, 1) {
            @Override
            public Long getId() {
                return 6L;
            }
        };
        genderQuestion.setType(QuestionType.SINGLE_CHOICE);
        genderQuestion.setTitle("Gender:");
        List<Option> genderOptions = new ArrayList<>();
        genderOptions.add(newOption(genderQuestion, 0, "Male"));
        genderOptions.add(newOption(genderQuestion, 1, "Female"));
        genderQuestion.setOptions(genderOptions);
        genderQuestion.setHasOthersOption(true);
        genderQuestion.setOthersOptionText("Others");

        questionnaire.setQuestions(Arrays.asList(phoneQuestion, genderQuestion));
        this.questionnaire = questionnaire;
    }

    @Test
    public void testGetQuestionnaire() throws Exception {
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
     *
     * @param question       related question object
     * @param sequenceNumber option's sequence number in the question
     * @param text           option's text
     * @return the created object
     */
    private Option newOption(Question question, int sequenceNumber, String text) {
        Option option = new Option(question, sequenceNumber);
        option.setText(text);
        return option;
    }

    @Test
    public void testCreateReplySuccess() throws Exception {
        given(questionnaireRepository.findOne(2L)).willReturn(questionnaire);
        given(replyRepository.save(any(Reply.class))).willReturn(null);

        mvc.perform(post("/questionnaires/2")
                .param("questionnaireId", "2")
                .param("items[0].questionId", "5")
                .param("items[0].inputText", "12312341234")
                .param("items[1].questionId", "6")
                .param("items[1].selectedOptions", "0")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/questionnaires/finished"))
                .andExpect(flash().attribute("questionnaireId", 2L));
    }

    @Test
    public void testCreateReplyFailureDueRequiredMissing() throws Exception {
        given(questionnaireRepository.findOne(2L)).willReturn(questionnaire);
        given(replyRepository.save(any(Reply.class))).willReturn(null);

        mvc.perform(post("/questionnaires/2")
                .param("questionnaireId", "2")
                .param("items[0].questionId", "5")
                .param("items[0].inputText", "")
                .param("items[1].questionId", "6")
                .param("items[1].selectedOptions", "")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("You must answer the required questions.")));
    }

    @Test
    public void testCreateReplyFailureDueFieldErrors() throws Exception {
        given(questionnaireRepository.findOne(2L)).willReturn(questionnaire);
        given(replyRepository.save(any(Reply.class))).willReturn(null);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4096; i++) {
            sb.append('0');
        }

        mvc.perform(post("/questionnaires/2")
                .param("questionnaireId", "2")
                .param("items[0].questionId", "5")
                .param("items[0].inputText", "12312341234")
                .param("items[1].questionId", "6")
                .param("items[1].selectedOptions", "-1")
                .param("items[1].inputText", sb.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The length of input text should be less than or equal to 4094.")));
    }
}
