-- 1. SUBJECT TABLE
CREATE TABLE subjects (
    subject_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(255) NOT NULL UNIQUE,
    subject_description VARCHAR(255)
);

-- 2. QUESTIONS TABLE
CREATE TABLE questions (
    questions_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_id INT NOT NULL,
    question VARCHAR(255) NOT NULL UNIQUE,
    answer VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE(subject_id, question),
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- Insert Subjects
INSERT INTO subjects (subject_name, subject_description) VALUES
('General Health', 'Basic health-related questions'),
('Nutrition', 'Food and diet-related inquiries'),
('Fitness', 'Exercise and physical activity guidance');

-- Insert Questions under 'General Health' (subject_id = 1)
INSERT INTO questions (subject_id, question, answer) VALUES 
(1, 'What are the common symptoms of the flu?', 
 'Common symptoms of the flu include fever, chills, cough, sore throat, runny nose, muscle aches, and fatigue.'),
(1, 'How can I prevent catching a cold?', 
 'Wash your hands frequently, avoid touching your face, and maintain a healthy immune system with a balanced diet and exercise.'),
(1, 'What are the early signs of diabetes?', 
 'Early signs of diabetes include frequent urination, excessive thirst, unexplained weight loss, increased hunger, and fatigue.'),
(1, 'How can I reduce high blood pressure naturally?', 
 'Eat a heart-healthy diet, exercise regularly, reduce stress, and limit salt and alcohol consumption.');

INSERT INTO questions (subject_id, question, answer) VALUES 
(2, 'What are the benefits of eating fruits and vegetables?', 
 'Fruits and vegetables provide essential vitamins, minerals, antioxidants, and fiber that support immune function, reduce the risk of chronic diseases, and promote digestive health.'),
(2, 'What is a balanced diet?', 
 'A balanced diet includes a variety of foods in the right proportions to provide the body with necessary nutrients such as carbohydrates, proteins, fats, vitamins, and minerals.'),
(2, 'How much water should I drink daily?', 
 'It is recommended to drink at least 8 glasses (about 2 liters) of water a day, but individual needs may vary depending on activity level, climate, and health conditions.'),
(2, 'What are healthy sources of protein?', 
 'Healthy protein sources include lean meats, fish, eggs, beans, lentils, tofu, and nuts.');


INSERT INTO questions (subject_id, question, answer) VALUES 
(3, 'What are the benefits of regular exercise?', 
 'Regular exercise improves cardiovascular health, boosts mood, strengthens muscles, helps manage weight, and reduces the risk of chronic diseases such as diabetes and heart disease.'),
(3, 'How often should I exercise to stay fit?', 
 'It is recommended to exercise at least 150 minutes per week of moderate-intensity exercise or 75 minutes of vigorous-intensity exercise, along with strength training exercises twice a week.'),
(3, 'What is the best way to build muscle?', 
 'To build muscle, engage in strength training exercises, progressively increase resistance or weight, eat a protein-rich diet, and ensure proper rest and recovery.'),
(3, 'How can I stay motivated to work out?', 
 'Set realistic fitness goals, track your progress, find an exercise routine you enjoy, work out with a partner, and celebrate small milestones to stay motivated.');