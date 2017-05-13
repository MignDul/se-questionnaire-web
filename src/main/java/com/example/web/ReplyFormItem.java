package com.example.web;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Helper class for creating a reply. This class is used to map the question's answers
 */
public class ReplyFormItem {

    private List<Integer> selectedOptions;

    @Size(max = 4094, message = "The length of input text should be less than or equal to 4094.")
    private String inputText;

    public ReplyFormItem() {}

    public List<Integer> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<Integer> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
}
