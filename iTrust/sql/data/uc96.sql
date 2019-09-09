INSERT INTO patients (
	MID,
	OBEligibility,
	lastName,
  firstName
)
VALUES (
	801,
	true,
	'Jia',
  'Mother'
);

INSERT INTO users(
  MID,
  password,
  role,
  sQuestion,
  sAnswer
)
VALUES (
  801,
  '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4',
  'patient',
  'what is your favorite color?',
  'red'
);

INSERT INTO appointment(
  appt_id,
  doctor_id,
  patient_id,
  sched_date,
  appt_type,
  comment
)VALUES(
  999,
  9000000012,
  801,
  '2018-11-13 14:00:00',
  'OB Child Birth Visit',
  '%expectedDeliveryType='
);

INSERT INTO appointment(doctor_id,patient_id,sched_date,appt_type,comment)
VALUES
('9000000000', '801', CONCAT(YEAR(NOW())+1, '-06-04 10:30:00'), 'Obstetrics_ChildBirth', '$vaginal delivery');

INSERT INTO obchildbirthvisit(
 MID,
 childBirthVisitID,
 preferredDeliveryType,
 visitType,
 deliveryDate,
 deliveryDuration,
 deliveryType,
 numberOfGirlBabies,
 numberOfBoyBabies,
 pitocin,
 nitrousOxide,
 pethidine,
 epiduralAnaesthesia,
 magnesiumSulfate,
 rhImmuneGlobulin,
 comment)
 VALUES (
    801,
    100,
    "vaginal delivery",
    "appointment",
    makedate(2018, 1),
    maketime(12, 0, 0),
    "vaginal delivery",
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    "test cmmt"
  );