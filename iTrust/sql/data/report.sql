INSERT INTO pregnancyRecord(
 patientMID,
 pregnancyterm,
 yearofconception,
 deliverytype
)
VALUES(
 1,
 10,
 2017,
 "vaginal"
);


INSERT INTO initobrecord(recorddate,MID, EDD)
VALUES('2017-10-1',1, '2017-01-01');

INSERT INTO officeVisit(
 patientMID,
 visitDate,
 locationID,
 apptTypeID,
 weight,
 blood_pressure
)
VALUES (
 1,
 '2017-4-30',
 '2',
 7,
 73.5,
 '121/121'
);

INSERT INTO OBOfficeVisit(
  patientMID,
  locationID,
  apptTypeID,
  currentDate,
  numOfWeeks,
  weight,
  bloodPressure,
  fetalHeartRate,
  isMultiplePregnancy,
  numOfMultiplePregnancy,
  isLowLyingPlacentaObserved
)
VALUES(
  1,
  '2',
  7,
  '2017-4-30',
  '10-5',
  73.5,
  '121/121',
  6,
  1,
  2,
  0
);

INSERT INTO diagnosis(
 visitId,
 icdCode,
 obVisitID
)
VALUES
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 1), 'O21', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 1)),
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 1), 'E03', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 1)),
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 1), 'E11', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 1));



INSERT INTO officeVisit(
 patientMID,
 visitDate,
 locationID,
 apptTypeID,
 weight,
 blood_pressure
)
VALUES (
 1,
 '2017-5-15',
 '2',
 7,
 83.5,
 '100/120'
);

INSERT INTO OBOfficeVisit(
  patientMID,
  locationID,
  apptTypeID,
  currentDate,
  numOfWeeks,
  weight,
  bloodPressure,
  fetalHeartRate,
  isMultiplePregnancy,
  numOfMultiplePregnancy,
  isLowLyingPlacentaObserved
)
VALUES(
  1,
  '2',
  7,
  '2017-5-15',
  '11-5',
  83.5,
  '100/120',
  6,
  1,
  2,
  0
);

INSERT INTO diagnosis(
 visitId,
 icdCode,
 obVisitID
)
VALUES
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 1), 'Z88-2', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 1));


INSERT INTO pregnancyRecord(
 patientMID,
 pregnancyterm,
 yearofconception,
 deliverytype
)
VALUES(
 22,
 9,
 2018,
 "vaginal"
);


INSERT INTO initobrecord(MID, EDD)
VALUES(22, '2018-01-15');

INSERT INTO officeVisit(
 patientMID,
 visitDate,
 locationID,
 apptTypeID,
 weight,
 blood_pressure
)
VALUES (
 22,
 '2018-06-20',
 '1',
 6,
 63.2,
 '90/90'
);

INSERT INTO OBOfficeVisit(
  patientMID,
  locationID,
  apptTypeID,
  currentDate,
  numOfWeeks,
  weight,
  bloodPressure,
  fetalHeartRate,
  isMultiplePregnancy,
  numOfMultiplePregnancy,
  isLowLyingPlacentaObserved
)
VALUES(
  22,
  '1',
  6,
  '2018-06-20',
  '9-5',
  63.2,
  '90/90',
  6,
  0,
  1,
  0
);

INSERT INTO diagnosis(
 visitId,
 icdCode,
 obVisitID
)
VALUES
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 22), 'T36', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 22)),
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 22), 'E03', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 22)),
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 22), 'Z79', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 22));



INSERT INTO officeVisit(
 patientMID,
 visitDate,
 locationID,
 apptTypeID,
 weight,
 blood_pressure
)
VALUES (
 22,
 '2018-7-1',
 '1',
 7,
 73.5,
 '100/120'
);

INSERT INTO OBOfficeVisit(
  patientMID,
  locationID,
  apptTypeID,
  currentDate,
  numOfWeeks,
  weight,
  bloodPressure,
  fetalHeartRate,
  isMultiplePregnancy,
  numOfMultiplePregnancy,
  isLowLyingPlacentaObserved
)
VALUES(
  22,
  '2',
  6,
  '2018-7-1',
  '11-5',
  73.5,
  '95/95',
  6,
  0,
  1,
  0
);

INSERT INTO diagnosis(
 visitId,
 icdCode,
 obVisitID
)
VALUES
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 22), 'Z11', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 22));


