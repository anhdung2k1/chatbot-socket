CREATE TABLE questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(255) NOT NULL UNIQUE,
    answer VARCHAR(255) NOT NULL
);

INSERT INTO questions (question, answer) VALUES 
('What are the common symptoms of the flu?', 'Common symptoms of the flu include fever, chills, cough, sore throat, runny nose, muscle aches, and fatigue.'),
('How can I prevent catching a cold?', 'Wash your hands frequently, avoid touching your face, and maintain a healthy immune system with a balanced diet and exercise.'),
('What are the early signs of diabetes?', 'Early signs of diabetes include frequent urination, excessive thirst, unexplained weight loss, increased hunger, and fatigue.'),
('How can I reduce high blood pressure naturally?', 'Eat a heart-healthy diet, exercise regularly, reduce stress, and limit salt and alcohol consumption.');
