(function () {
    var set_question_attributes = function ($e, index) {
        $e.attr("id", "question_" + index);
        $e.find(".question-index").text(index + 1);
    };
    var get_question_index = function (question_id) {
        var parts = question_id.split("_");
        return parseInt(parts[1]);
    };
    var set_option_attributes = function ($e, questionIndex, optionIndex) {
        var option_id = "question_" + questionIndex + "_option_" + optionIndex;
        $e.attr("id", option_id);
        $e.find(".option-index").text(optionIndex + 1);
        $e.find("label").attr("for", option_id + "_input");
        $e.find("input").attr("id", option_id + "_input");
    };
    var get_option_indexes = function (option_id) {
        var parts = option_id.split("_");
        return [parseInt(parts[1]), parseInt(parts[3])];
    };

    var select_question_type_change_listener = function () {
        var $this = $(this);
        var type_index = $this.find("option:selected").index();
        var $question = $this.parents(".question");
        var $tab = $question.find(".tab-pane:nth-child(" + (parseInt(type_index / 2) + 1) + ")");
        $question.find(".tab-pane").removeClass("active");
        $tab.addClass("active");
    };
    var remove_question_click_listener = function () {
        var $removed_question = $(this).parents(".question");
        var index_start = get_question_index($removed_question.attr("id"));
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
        set_option_attributes($new_option, get_question_index($question.attr("id")), option_count);
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
        var indexes = get_option_indexes($removed_option.attr("id"));
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
        var $question = $this.parents(".question");
        var $new_others_option = $("body > .option-others").clone();
        $new_others_option.removeClass("hidden");
        // Set attributes
        set_option_attributes($new_others_option, get_question_index($question.attr("id")), -1);
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
        $("#btn_add_question").click();
    });
})();
