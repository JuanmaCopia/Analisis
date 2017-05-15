CREATE TABLE games_questions (
	question_id INT,
	game_id  int,
	constraint fkgames FOREIGN KEY (game_id) REFERENCES games(game_id),
	constraint fkquestions FOREIGN KEY (question_id) REFERENCES questions(question_id)
)ENGINE=InnoDB;