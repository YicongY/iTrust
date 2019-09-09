INSERT INTO appointment(appt_id, doctor_id, patient_id, sched_date, appt_type, comment, isRated)
VALUES
(2000, '9000000003', '2', CONCAT(YEAR(NOW()), '-10-14 08:00:00'), 'Colonoscopy', NULL, true);

INSERT INTO ratings(appt_id, doctor_id, patient_id, punctuality, attitude, skillfulness, knowledge, efficiency, comment)
VALUES
(2000, '9000000003', '2', 4, 4, 4, 4, 4, 'Ok.');