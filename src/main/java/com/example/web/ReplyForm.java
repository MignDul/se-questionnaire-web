package com.example.web;

import javax.validation.Valid;
import java.util.List;

/**
 * Helper class for creating a reply
 */
public class ReplyForm {

    private Long questionnaireId;

    @Valid
    private List<ReplyFormItem> items;

    public ReplyForm() {}

    public ReplyForm(Long questionnaireId, List<ReplyFormItem> items) {
        this.questionnaireId = questionnaireId;
        this.items = items;
    }

    public Long getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public List<ReplyFormItem> getItems() {
        return items;
    }

    public void setItems(List<ReplyFormItem> items) {
        this.items = items;
    }
}
