package com.example.web;

import com.example.domain.Option;
import com.example.domain.Question;
import com.example.domain.Questionnaire;
import com.example.repository.QuestionnaireRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.CollectionUtils;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CreateQuestionnaireController.class)
public class CreateQuestionnaireControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private QuestionnaireRepository questionnaireRepository;

    private Questionnaire questionnaire;

    @Before
    public void setUp() throws Exception {
        questionnaire = PrepareData.generateTestQuestionnaire();
    }

    @Test
    public void testCreateQuestionnaireSuccess() throws Exception {
        given(questionnaireRepository.save(any(Questionnaire.class))).willReturn(questionnaire);

        mvc.perform(buildRequest(questionnaire))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("questionnaireId", 2L));
    }

    @Test
    public void testCreateQuestionnaireFailureDueQuestionsEmpty() throws Exception {
        questionnaire.setQuestions(null);
        given(questionnaireRepository.save(any(Questionnaire.class))).willReturn(questionnaire);

        mvc.perform(buildRequest(questionnaire))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("At least one question is needed.")));
    }

    @Test
    public void testCreateQuestionnaireFailureDueOptionTextEmpty() throws Exception {
        questionnaire.getQuestions().get(1).getOptions().get(0).setText(null);
        given(questionnaireRepository.save(any(Questionnaire.class))).willReturn(questionnaire);

        mvc.perform(buildRequest(questionnaire))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Option text is required.")));
    }

    private MockHttpServletRequestBuilder buildRequest(Questionnaire questionnaire) {
        MockHttpServletRequestBuilder createRequestBuilder = post("/create")
                .param("title", questionnaire.getTitle())
                .param("description", questionnaire.getDescription());
        if (!CollectionUtils.isEmpty(questionnaire.getQuestions())) {
            for (int i = 0; i < questionnaire.getQuestions().size(); i++) {
                Question question = questionnaire.getQuestions().get(i);
                String questionPrefix = "questions[" + i + "].";
                createRequestBuilder
                        .param(questionPrefix + "sequenceNumber", String.valueOf(question.getSequenceNumber()))
                        .param(questionPrefix + "content", question.getContent())
                        .param(questionPrefix + "required", String.valueOf(question.isRequired()))
                        .param(questionPrefix + "type", question.getType().name());
                if (question.isHasOthersOption()) {
                    createRequestBuilder
                            .param(questionPrefix + "hasOthersOption", "true")
                            .param(questionPrefix + "othersOptionText", question.getOthersOptionText());
                }
                if (!CollectionUtils.isEmpty(question.getOptions())) {
                    for (int j = 0; j < question.getOptions().size(); j++) {
                        Option option = question.getOptions().get(j);
                        String optionPrefix = questionPrefix + "options[" + j + "].";
                        createRequestBuilder
                                .param(optionPrefix + "sequenceNumber", String.valueOf(option.getSequenceNumber()))
                                .param(optionPrefix + "text", option.getText());
                    }
                }
            }
        }
        return createRequestBuilder;
    }
}
