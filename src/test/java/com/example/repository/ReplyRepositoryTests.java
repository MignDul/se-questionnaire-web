package com.example.repository;

import com.example.domain.Questionnaire;
import com.example.domain.Reply;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testFindAllByQuestionnaire() throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        List<Reply> replies = replyRepository.findAllByQuestionnaire(new Questionnaire(1L), sort);

        assertThat(replies).hasSize(2);
        assertThat(replies.get(0).getCreatedAt()).isAfterOrEqualsTo(replies.get(1).getCreatedAt());
    }
}
