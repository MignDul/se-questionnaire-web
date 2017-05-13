package com.example.web;

import javax.validation.Valid;
import java.util.List;

/**
 * Helper class for creating a reply
 */
public class ReplyForm {

    @Valid
    private List<ReplyFormItem> items;

    public ReplyForm() {}

    public ReplyForm(List<ReplyFormItem> items) {
        this.items = items;
    }

    public List<ReplyFormItem> getItems() {
        return items;
    }

    public void setItems(List<ReplyFormItem> items) {
        this.items = items;
    }
}
