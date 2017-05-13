package com.example.web;

import com.example.domain.Questionnaire;
import com.example.domain.Reply;
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
        questionnaire = PrepareData.generateTestQuestionnaire();
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
                .andExpect(redirectedUrl("/questionnaires/2/finished"));
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
