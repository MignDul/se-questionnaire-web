package com.example.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
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
    @Valid
    @NotEmpty(message = "At least one question is needed.")
    private List<Question> questions;

    @Column(nullable = false)
    private Date createdAt;

    public Questionnaire() {
        // no-args constructor required by JPA spec
    }

    public Questionnaire(Long id) {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
