--
-- Sample data
--
-- For in-memory database, every time when the project starts, this file will be executed,
-- and data will be imported into the database. When stopping, the database will be removed
-- from the computer memory, so data are lost. This feature is enabled by default for
-- in-memory database.
--
-- NOTICE: For production environment, never use in-memory database.
--

-- =================================================================================================
-- Questionnaire

insert into questionnaire(title, description, created_at) values ('A Questionnaire about Software Development Experience', 'Hello, everyone! I''m Lu Mingming, TA of software engineering course. To prepare the "Coding Tools - Java" lecture content, I made this questionnaire. I hope you can take a few minutes to complete this questionnaire. Thank you for your cooperation!', '2017-05-01 12:00:00');
insert into question(questionnaire_id, sequence_number, type, content, required, has_others_option, others_option_text) values (1, 0, 2, 'Student Number:', TRUE, FALSE, NULL);

insert into question(questionnaire_id, sequence_number, type, content, required, has_others_option, others_option_text) values (1, 1, 1, 'What programming languages are you familiar with (can read and write some code)?', TRUE, TRUE, 'Others (Please specify in the box below.)');
insert into option(question_id, sequence_number, `text`) values (2, 0, 'C/C++');
insert into option(question_id, sequence_number, `text`) values (2, 1, 'Java');
insert into option(question_id, sequence_number, `text`) values (2, 2, 'PHP');

insert into question(questionnaire_id, sequence_number, type, content, required, has_others_option, others_option_text) values (1, 2, 0, 'Did you know Java before?', TRUE, FALSE, NULL);
insert into option(question_id, sequence_number, `text`) values (3, 0, 'Yes');
insert into option(question_id, sequence_number, `text`) values (3, 1, 'No');

insert into question(questionnaire_id, sequence_number, type, content, required, has_others_option, others_option_text) values (1, 3, 3, 'If you have participated in some projects or developed a software by yourself, please write down these experience in the box below.', FALSE, FALSE, NULL);

insert into reply(questionnaire_id, created_at) values (1, '2017-05-02 12:00:00');
insert into answer(reply_id, question_id, option_sequence_number, `text`) values (1, 1, NULL, '1300000');
insert into answer(reply_id, question_id, option_sequence_number, `text`) values (1, 2, 1, NULL);
insert into answer(reply_id, question_id, option_sequence_number, `text`) values (1, 2, -1, 'Python');
insert into answer(reply_id, question_id, option_sequence_number, `text`) values (1, 3, 0, NULL);
insert into answer(reply_id, question_id, option_sequence_number, `text`) values (1, 4, NULL, 'I am good at programming.');

insert into reply(questionnaire_id, created_at) values (1, '2017-05-02 12:10:00');
insert into answer(reply_id, question_id, option_sequence_number, `text`) values (2, 1, NULL, '1300001');
insert into answer(reply_id, question_id, option_sequence_number, `text`) values (2, 2, 0, NULL);
insert into answer(reply_id, question_id, option_sequence_number, `text`) values (2, 3, 1, NULL);
