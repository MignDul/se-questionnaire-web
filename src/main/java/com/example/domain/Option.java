package com.example.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * The options for one question
 */
@Entity
public class Option {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Question question;

    @Column(nullable = false)
    private int sequenceNumber;

    @Column(nullable = false, length = 510)
    @NotEmpty(message = "Option's text is required.")
    @Size(max = 510, message = "The length of option's text should be less than or equal to 510.")
    private String text;

    public Option() {}

    public Option(Long id) {
        this.id = id;
    }

    public Option(Question question, int sequenceNumber) {
        this.question = question;
        this.sequenceNumber = sequenceNumber;
    }

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
