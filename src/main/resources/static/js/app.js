(function () {
    var set_question_attributes = function ($question, index) {
        var id_prefix = "questions" + index + ".";
        var name_prefix = "questions[" + index + "].";
        $question.find(".question-sequence-number")
            .attr("value", index)
            .attr("id", id_prefix + "sequenceNumber")
            .attr("name", name_prefix + "sequenceNumber");
        $question.find(".question-index").text(index + 1);
        $question.find(".question-content")
            .attr("id", id_prefix + "content")
            .attr("name", name_prefix + "content");
        $question.find(".question-required-select")
            .attr("id", id_prefix + "required")
            .attr("name", name_prefix + "required");
        $question.find(".question-type-select")
            .attr("id", id_prefix + "type")
            .attr("name", name_prefix + "type");
    };
    var get_question_index = function ($question) {
        return parseInt($question.find(".question-sequence-number").attr("value"));
    };
    var set_option_attributes = function ($option, question_index, option_index) {
        var id_prefix = "questions" + question_index + ".options" + option_index + ".";
        var name_prefix = "questions[" + question_index + "].options[" + option_index + "].";
        $option.find(".option-sequence-number")
            .attr("value", option_index)
            .attr("id", id_prefix + "sequenceNumber")
            .attr("name", name_prefix + "sequenceNumber");
        $option.find(".option-index").text(option_index + 1);
        $option.find(".option-label")
            .attr("for", id_prefix + "text");
        $option.find(".option-input")
            .attr("id", id_prefix + "text")
            .attr("name", name_prefix + "text");
    };
    var get_option_indexes = function ($option) {
        var option_index = $option.find(".option-sequence-number").attr("value");
        var question_index = $option.parents(".question").find(".question-sequence-number").attr("value");
        return [parseInt(option_index), parseInt(question_index)];
    };

    var select_question_type_change_listener = function () {
        var $this = $(this);
        var type_index = $this.find("option:selected").index();
        var tab_index = parseInt(type_index / 2);
        var $question = $this.parents(".question");
        var $tab = $question.find(".tab-pane:nth-child(" + (tab_index + 1) + ")");

        if (tab_index == 0 && $tab.find(".option").length == 0) {
            // Make sure there are two empty options
            var $option_add = $tab.find(".option-add");
            $option_add.click();
            $option_add.click();
        } else {
            var name_existed = typeof($question.find(".option-sequence-number").attr("name")) != 'undefined';
            if (tab_index == 0 && !name_existed) {
                // Valid request parameters
                var question_index = get_question_index($question);
                $question.find(".option").each(function (i, e) {
                    set_option_attributes($(e), question_index, i);
                });
            } else if (tab_index = 1 && name_existed) {
                // Invalid request parameters
                // If text types are chosen, these parameters should not be uploaded to the server
                $question.find(".option").each(function (i, e) {
                    $(e).find(".option-sequence-number").removeAttr("name");
                    $(e).find(".option-input").removeAttr("name");
                });
            }
        }
        $question.find(".tab-pane").removeClass("active");
        $tab.addClass("active");
    };
    var remove_question_click_listener = function () {
        var $removed_question = $(this).parents(".question");
        var index_start = get_question_index($removed_question);
        $removed_question.nextAll(".question").each(function (i, e) {
            var $e = $(e);
            // Update attributes
            var sequence_number = index_start + i;
            set_question_attributes($e, sequence_number);
            // Update options' attributes
            $e.find(".option").each(function (oi, oe) {
                set_option_attributes($(oe), sequence_number, oi);
            })
        });

        $removed_question.next().remove(); // Remove <hr/> tag
        $removed_question.remove();
    };

    var add_option_click_listener = function () {
        var $question = $(this).parents(".question");
        var $new_option = $("body > .option").clone();
        $new_option.removeClass("hidden");
        // Set sequence number
        var $options = $question.find(".option");
        var option_count = $options.length;
        set_option_attributes($new_option, get_question_index($question), option_count);
        // Bind listener
        $new_option.find(".option-remove").on("click", remove_option_click_listener);

        // Add Node
        if (option_count == 0) {
            $question.find(".option-container").prepend($new_option);
        } else {
            $options.last().after($new_option);
        }
    };
    var remove_option_click_listener = function () {
        var $removed_option = $(this).parents(".option");
        var indexes = get_option_indexes($removed_option);
        $removed_option.nextAll(".option").each(function (i, e) {
            var $e = $(e);
            // Update attributes
            var sequence_number = indexes[1] + i;
            set_option_attributes($e, indexes[0], sequence_number);
        });

        $removed_option.remove();
    };

    var add_others_option_click_listener = function () {
        var $this = $(this);
        var $new_others_option = $("body > .option-others").clone();
        $new_others_option.removeClass("hidden");
        // Set attributes
        var question_index = get_question_index($this.parents(".question"));
        var id_prefix = "question" + question_index + ".";
        var name_prefix = "questions[" + question_index + "].";
        $new_others_option.find(".option-has-others")
            .attr("value", "true")
            .attr("id", id_prefix + "hasOthersOption")
            .attr("name", name_prefix + "hasOthersOption");
        $new_others_option.find(".option-label")
            .attr("for", id_prefix + "othersOptionText");
        $new_others_option.find(".option-input")
            .attr("id", id_prefix + "othersOptionText")
            .attr("name", name_prefix + "othersOptionText");
        // Bind listener
        $new_others_option.find(".option-others-remove").on("click", remove_others_option_click_listener);

        // Add node
        $this.parents(".option-actions").before($new_others_option);
        // Disable add button
        $this.attr("disabled", "disabled");
    };
    var remove_others_option_click_listener = function () {
        var $option_others = $(this).parents(".option-others");
        // Enable add button
        $option_others.nextAll(".option-actions").find(".option-others-add").removeAttr("disabled");
        // Remove node
        $option_others.remove();
    };

    $("#btn_add_question").click(function () {
        var $new_question = $("body > .question").clone();
        $new_question.removeClass("hidden");
        // Set sequence number
        var question_count = $(".question-container .question").length;
        set_question_attributes($new_question, question_count);
        // Bind listeners
        $new_question.find(".question-type-select").on("change", select_question_type_change_listener);
        $new_question.find(".question-remove").on("click", remove_question_click_listener);
        $new_question.find(".option-add").on("click", add_option_click_listener);
        $new_question.find(".option-others-add").on("click", add_others_option_click_listener);

        // Add nodes
        var $question_container = $(".question-container");
        $question_container.append($new_question);
        $question_container.append($("<hr/>"));

        // Add two option nodes
        var $option_add = $new_question.find(".option-add");
        $option_add.click();
        $option_add.click();
    });

    $(document).ready(function () {
        var $questions = $(".question-container .question");
        if ($questions.length == 0) {
            $("#btn_add_question").click();
        } else {
            $questions.find(".question-type-select").on("change", select_question_type_change_listener);
            $questions.find(".question-remove").on("click", remove_question_click_listener);
            $questions.find(".option-add").on("click", add_option_click_listener);
            $questions.find(".option-others-add").on("click", add_others_option_click_listener);
            $questions.find(".option-remove").on("click", remove_option_click_listener);
            $questions.find(".option-others-remove").on("click", remove_others_option_click_listener);
        }
    });
})();
