package com.example.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Question {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Questionnaire questionnaire;

    @Column(nullable = false)
    private int sequenceNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL) // type's values are "0, 1, 2" in the database
    private QuestionType type;

    @Column(nullable = false, length = 4094)
    @NotEmpty(message = "Question title is required.")
    @Size(max = 4094, message = "The length of question's title should be less than or equal to 4094.")
    private String title;

    @Column(nullable = false)
    private boolean required = false;

    @OneToMany(mappedBy = "question")
    @OrderBy("sequence_number")
    private List<Option> options;

    @Column(nullable = false)
    private boolean hasOthersOption = false; // set default value

    @Column(length = 510)
    @Size(max = 510, message = "The length of others option's text should be less than or equal to 510.")
    private String othersOptionText;

    protected Question() {}

    public Question(Questionnaire questionnaire, int sequenceNumber) {
        this.questionnaire = questionnaire;
        this.sequenceNumber = sequenceNumber;
    }

    public Long getId() {
        return id;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public boolean isHasOthersOption() {
        return hasOthersOption;
    }

    public void setHasOthersOption(boolean hasOthersOption) {
        this.hasOthersOption = hasOthersOption;
    }

    public String getOthersOptionText() {
        return othersOptionText;
    }

    public void setOthersOptionText(String othersOptionText) {
        this.othersOptionText = othersOptionText;
    }
}
