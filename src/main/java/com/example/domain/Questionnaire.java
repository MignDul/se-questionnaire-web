package com.example.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
public class Questionnaire {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 510)
    @NotEmpty(message = "Title is required.")
    @Size(max = 510, message = "The length of title should be less than or equal to 510.")
    private String title;

    @Column(length = 4094)
    @Size(max = 4094, message = "The length of description should be less than or equal to 4094.")
    private String description;

    @OneToMany(mappedBy = "questionnaire", cascade = {CascadeType.ALL})
    @OrderBy("sequence_number")
    private List<Question> questions;

    @Column(nullable = false)
    private Date createdAt;

    protected Questionnaire() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    protected Questionnaire(Long id) {
        // Id constructor is only used in test.
        this.id = id;
    }

    public Questionnaire(String title, String description, Date createdAt) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
