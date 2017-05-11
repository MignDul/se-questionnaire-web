package com.example.web;

import com.example.domain.Answer;
import com.example.domain.Questionnaire;
import com.example.domain.Reply;
import com.example.repository.ReplyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReplyController.class)
public class ReplyControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReplyRepository replyRepository;

    @Test
    public void testGetReply() throws Exception {
        Questionnaire questionnaire = PrepareData.generateTestQuestionnaire();
        Reply reply = new Reply(questionnaire, new Date());
        Set<Answer> answers = new HashSet<>();
        reply.setAnswers(answers);

        Answer phoneAnswer = new Answer(reply, questionnaire.getQuestions().get(0));
        phoneAnswer.setText("12312341234");
        answers.add(phoneAnswer);
        Answer genderAnswer = new Answer(reply, questionnaire.getQuestions().get(1));
        genderAnswer.setOptionSequenceNumber(0);
        answers.add(genderAnswer);
        given(replyRepository.findOne(3L)).willReturn(reply);

        mvc.perform(get("/reply/3"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("A Test Questionnaire")))
                .andExpect(content().string(containsString("12312341234")))
                .andExpect(content().string(containsString("checked=\"checked\"")));
    }
}
