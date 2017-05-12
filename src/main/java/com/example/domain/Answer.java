package com.example.domain;

import javax.persistence.*;

@Entity
public class Answer {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Reply reply;

    @ManyToOne(optional = false)
    private Question question;

    @Column
    private Integer optionSequenceNumber;

    @Column(length = 4094)
    private String text;

    public Answer() {}

    public Answer(Long id) {
        this.id = id;
    }

    public Answer(Reply reply, Question question) {
        this.reply = reply;
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Integer getOptionSequenceNumber() {
        return optionSequenceNumber;
    }

    public void setOptionSequenceNumber(Integer optionSequenceNumber) {
        this.optionSequenceNumber = optionSequenceNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
