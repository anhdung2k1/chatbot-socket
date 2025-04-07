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
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE
);

-- Insert Subjects
INSERT INTO subjects (subject_name, subject_description) VALUES
('General Health', 'Basic health-related questions'),
('Nutrition', 'Food and diet-related inquiries'),
('Fitness', 'Exercise and physical activity guidance');

-- Insert Questions under 'General Health' (subject_id = 1)
INSERT INTO questions (subject_id, question, answer) VALUES
(1, 'What is the normal range for adult blood pressure?', 'A normal blood pressure reading for adults is typically around 120/80 mm Hg.'),
(1, 'How often should adults have a general health check-up?', 'Adults should have a general health check-up at least once a year.'),
(1, 'What are common signs of dehydration?', 'Common signs include thirst, dark urine, fatigue, dizziness, and dry mouth.'),
(1, 'How can I boost my immune system?', 'Maintain a balanced diet, exercise regularly, get adequate sleep, and manage stress.'),
(1, 'What is body mass index (BMI)?', 'BMI is a measure of body fat based on height and weight.'),
(1, 'How can I manage stress effectively?', 'Practice relaxation techniques, exercise regularly, and ensure adequate rest.'),
(1, 'What are the benefits of regular health screenings?', 'They help in early detection and prevention of diseases.'),
(1, 'How does smoking affect overall health?', 'Smoking increases the risk of chronic diseases like heart disease and cancer.'),
(1, 'What is the importance of hand hygiene?', 'Proper hand hygiene prevents the spread of infections and diseases.'),
(1, 'How can I maintain healthy skin?', 'Protect your skin from the sun, stay hydrated, and use appropriate skincare products.'),
(1, 'What are the effects of alcohol on the body?', 'Excessive alcohol consumption can lead to liver disease, heart problems, and addiction.'),
(1, 'How can I improve my posture?', 'Practice regular stretching, strengthen core muscles, and be mindful of body alignment.'),
(1, 'What are the benefits of quitting smoking?', 'Improved lung function, reduced risk of diseases, and better overall health.'),
(1, 'How does air pollution affect health?', 'It can cause respiratory issues, cardiovascular diseases, and other health problems.'),
(1, 'What is the role of vaccinations in health?', 'Vaccinations prevent infectious diseases and protect public health.'),
(1, 'How can I prevent common colds?', 'Wash hands regularly, avoid close contact with sick individuals, and maintain a healthy lifestyle.'),
(1, 'What are the signs of a stroke?', 'Sudden numbness, confusion, trouble speaking, and severe headache.'),
(1, 'How does obesity impact health?', 'Increases the risk of heart disease, diabetes, and other chronic conditions.'),
(1, 'What is the importance of hydration?', 'Proper hydration is essential for bodily functions and overall health.'),
(1, 'How can I reduce my risk of developing cancer?', 'Avoid tobacco, limit alcohol, maintain a healthy diet, and get regular screenings.');

INSERT INTO questions (subject_id, question, answer) VALUES
(2, 'What are the benefits of omega-3 fatty acids?', 'They support heart health, reduce inflammation, and improve brain function.'),
(2, 'How does fiber benefit digestion?', 'Fiber aids in bowel regularity and prevents constipation.'),
(2, 'What are antioxidants and their benefits?', 'Antioxidants protect cells from damage and reduce the risk of chronic diseases.'),
(2, 'How can I reduce sugar intake?', 'Limit sugary drinks, read labels, and choose natural sweeteners.'),
(2, 'What are the effects of trans fats on health?', 'Trans fats increase bad cholesterol and risk of heart disease.'),
(2, 'How does sodium affect blood pressure?', 'Excess sodium can raise blood pressure, increasing heart disease risk.'),
(2, 'What are Probities and their benefits?', 'Probities are beneficial bacteria that support gut health.'),
(2, 'How can I ensure adequate vitamin D intake?', 'Spend time in sunlight and consume fortified foods or supplements.'),
(2, 'What is the role of iron in the body?', 'Iron is essential for oxygen transport in the blood.'),
(2, 'How does caffeine affect the body?', 'Caffeine can boost alertness but may cause insomnia or jitteriness in excess.'),
(2, 'What are the benefits of whole grains?', 'Whole grains provide fiber, vitamins, and minerals, supporting overall health.'),
(2, 'How can I maintain a healthy weight through diet?', 'Balance calorie intake with physical activity and choose nutrient-dense foods.'),
(2, 'What is the importance of meal planning?', 'Meal planning helps ensure balanced nutrition and can prevent unhealthy eating.'),
(2, 'How do artificial sweeteners impact health?', 'They can aid in reducing sugar intake but should be consumed in moderation.'),
(2, 'What are the benefits of plant-based diets?', 'They can reduce the risk of chronic diseases and support environmental sustainability.'),
(2, 'How does alcohol consumption affect nutrition?', 'Alcohol provides empty calories and can interfere with nutrient absorption.'),
(2, 'What is the significance of portion control?', 'Portion control helps manage calorie intake and maintain a healthy weight.'),
(2, 'How can I identify food allergies?', 'Symptoms may include hives, swelling, digestive issues, and anaphylaxis.'),
(2, 'What are the benefits of eating breakfast?', 'Breakfast kickstart metabolism and provides energy for the day.'),
(2, 'How does dehydration affect the body?', 'Dehydration can cause fatigue, dizziness, and impair bodily functions.');

INSERT INTO questions (subject_id, question, answer) VALUES
(3, 'What is the difference between aerobic and anaerobic exercise?', 'Aerobic exercise uses oxygen for energy, while anaerobic does not.'),
(3, 'How can I improve cardiovascular endurance?', 'Engage in regular aerobic activities like running, cycling, or swimming.'),
(3, 'What are the benefits of strength training?', 'Increases muscle mass, strengthens bones, and boosts metabolism.'),
(3, 'How does flexibility impact overall fitness?', 'Improves range of motion, reduces injury risk, and enhances performance.'),
(3, 'What is the importance of a warm-up before exercise?', 'Prepares the body for activity and reduces injury risk.'),
(3, 'How can I prevent workout injuries?', 'Use proper form, start slowly, and listen to your body.'),
(3, 'What are the benefits of high-intensity interval training (HIT)?', 'Burns calories efficiently and improves cardiovascular fitness.'),
(3, 'How does rest and recovery affect fitness progress?', 'Allows muscles to repair and grow, preventing overtraining.'),
(3, 'What is the role of hydration during exercise?', 'Maintains performance and prevents dehydration-related issues.'),
(3, 'How can I stay consistent with my workout routine?', 'Set realistic goals, find enjoyable activities, and track progress.'),
(3, 'What are the signs of overtraining?', 'Persistent fatigue, decreased performance, and increased injury risk.'),
(3, 'What is the best time of day to work out?', 'The best time to work out is the time that fits your schedule and allows you to be consistent.'),
(3, 'Can walking help in weight loss?', 'Yes, regular brisk walking can burn calories and aid in weight loss.'),
(3, 'How does strength training benefit older adults?', 'It improves muscle mass, bone density, balance, and reduces fall risk.'),
(3, 'What is functional fitness?', 'Functional fitness exercises train your muscles to work together and prepare them for daily tasks.'),
(3, 'Should I work out if I am sore?', 'Light exercise and stretching can help relieve soreness, but avoid intense activity until recovered.'),
(3, 'How can I track my fitness progress?', 'Use fitness apps, take body measurements, track workout performance, and monitor how you feel.'),
(3, 'How important is breathing during exercise?', 'Proper breathing improves performance and reduces the risk of dizziness or injury.'),
(3, 'What is the role of core strength in fitness?', 'Core strength enhances stability, posture, and reduces risk of injury.'),
(3, 'How often should I change my workout routine?', 'It is good to switch up your routine every 4 to 6 weeks to prevent plateaus and keep progress steady.');