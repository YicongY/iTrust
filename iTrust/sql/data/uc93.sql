INSERT INTO patients (
	MID,
	OBEligibility
)
VALUES (
	800,
	true
)
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(
  MID,
  password,
  role,
  sQuestion,
  sAnswer
)
VALUES (
  800,
  '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4',
  'patient',
  'what is your favorite color?',
  'red'
)
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO initOBrecord(
  MID,
  recorddate,
  yearofconception,
  numberofweekspregnant,
  numberofhoursinlabor,
  weightgainduringpregnancy,
  deliverytype,
  pregnancytype,
  LMP,
  EDD
)
VALUES(
  1234,
  Now(),
  2017,
  '28-1',
  10.1,
  30.5,
  'vaginal delivery',
  1,
  '2017-1-1',
  '2017-10-10'
);


INSERT INTO initOBrecord(
  MID,
  recorddate,
  yearofconception,
  numberofweekspregnant,
  numberofhoursinlabor,
  weightgainduringpregnancy,
  deliverytype,
  pregnancytype,
  LMP,
  EDD
)
VALUES(
  1,
  '2018-10-10',
  2018,
  '9-0',
  10.1,
  30.5,
  'vaginal delivery',
  1,
  '2018-10-10',
  '2019-09-16'
);