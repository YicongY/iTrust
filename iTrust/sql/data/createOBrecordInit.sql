CREATE TABLE initOBrecord (
MID bigint(20) UNSIGNED,
recorddate DATE,
obrecordID bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
yearofconception int,
numberofweekspregnant VARCHAR(255),
numberofhoursinlabor float,

weightgainduringpregnancy float,
deliverytype varchar(255),
pregnancytype int,
LMP DATE,
EDD DATE,
PRIMARY KEY (obrecordID));
