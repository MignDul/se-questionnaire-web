package com.example.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Reply {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Questionnaire questionnaire;

    @OneToMany(mappedBy = "reply", cascade = {CascadeType.ALL})
    private Set<Answer> answers;

    @Column(nullable = false)
    private Date createdAt;

    protected Reply() {}

    public Reply(Long id) {
        this.id = id;
    }

    public Reply(Questionnaire questionnaire, Date createdAt) {
        this.questionnaire = questionnaire;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
