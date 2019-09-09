CREATE TABLE ratings(
	appt_id				INT UNSIGNED NOT NULL,
	doctor_id           BIGINT UNSIGNED NOT NULL,
	patient_id          BIGINT UNSIGNED NOT NULL,
	punctuality   float NOT NULL,
	attitude      float NOT NULL,
	skillfulness  float NOT NULL,
	knowledge     float NOT NULL,
	efficiency    float NOT NULL,
	comment       VARCHAR (200),

  PRIMARY KEY (appt_id),
	FOREIGN KEY  (appt_id) REFERENCES appointment (appt_id),
	FOREIGN KEY  (patient_id) REFERENCES patients (MID),
	FOREIGN KEY  (doctor_id) REFERENCES personnel (MID)
) ENGINE=MyISAM;
