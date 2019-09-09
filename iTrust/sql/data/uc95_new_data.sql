INSERT INTO pregnancyRecord(
 patientMID,
 pregnancyterm,
 yearofconception,
 deliverytype
)
VALUES(
 5678,
 10,
 2016,
 "vaginal"
);


INSERT INTO initobrecord(MID, EDD)
VALUES(5678, '2019-02-01');

INSERT INTO officeVisit(
 patientMID,
 visitDate,
 locationID,
 apptTypeID,
 weight,
 blood_pressure
)
VALUES (
 5678,
 '2018-10-30',
 '2',
 7,
 73.5,
 '120/120'
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
  5678,
  '2',
  7,
  '2018-10-30',
  '10-5',
  73.5,
  '120/120',
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
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 5678), 'O21', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 5678)),
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 5678), 'E03', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 5678)),
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 5678), 'E11', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 5678));

INSERT INTO officeVisit(
 patientMID,
 visitDate,
 locationID,
 apptTypeID,
 weight,
 blood_pressure
)
VALUES (
 5678,
 '2018-11-30',
 '2',
 7,
 78.5,
 '150/120'
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
  5678,
  '2',
  7,
  '2018-11-30',
  '10-5',
  78.5,
  '150/120',
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
 ((SELECT MAX(visitID) FROM officeVisit WHERE patientMID = 5678), 'C44', (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = 5678));