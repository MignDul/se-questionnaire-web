package com.example.repository;

import com.example.domain.Option;
import com.example.domain.Question;
import com.example.domain.QuestionType;
import com.example.domain.Questionnaire;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
// @FixMethodOrder annotation is used for transactional test.
// Make sure that testDelete() is executed before testTransactional().
// By default, test methods are called in random order.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuestionnaireRepositoryTests {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Test
    public void testDataExisted() throws Exception {
        List<Questionnaire> data = new ArrayList<>();
        questionnaireRepository.findAll().forEach(data::add); // transfer iterable to list
        assertThat(data.size()).isEqualTo(1);  // only one questionnaire existed in 'import.sql'
        Questionnaire questionnaire = data.get(0);
        assertThat(questionnaire.getTitle()).isEqualTo("A Questionnaire about Software Development Experience");

        // check questions
        List<Question> questions = questionnaire.getQuestions();
        assertThat(questions.size()).isEqualTo(4);
        Question multipleChoiceQuestion = questions.get(1);
        assertThat(multipleChoiceQuestion.getType()).isEqualTo(QuestionType.MULTIPLE_CHOICE);
        assertThat(multipleChoiceQuestion.isRequired()).isTrue();
        assertThat(multipleChoiceQuestion.isHasOthersOption()).isTrue();

        // check options
        List<Option> options = multipleChoiceQuestion.getOptions();
        assertThat(options.size()).isEqualTo(3);
        assertThat(options.get(0).getText()).isEqualTo("C/C++");
    }

    @Test
    public void testDelete() {
        long countBeforeDelete = questionnaireRepository.count();
        assertThat(countBeforeDelete).isEqualTo(1L);

        // First delete all related, otherwise throw sql error.
        optionRepository.deleteAll();
        questionRepository.deleteAll();
        // Last delete itself.
        questionnaireRepository.delete(1L);

        long countAfterDelete = questionnaireRepository.count();
        assertThat(countAfterDelete).isEqualTo(0L);
    }

    @Test
    public void testTransactional() {
        // Data JPA tests are transactional and rollback at the end of each test.
        long count = questionnaireRepository.count();
        assertThat(count).isEqualTo(1L);
    }
}
