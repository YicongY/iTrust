CREATE TABLE OBChildBirthVisit
(
  MID                   BIGINT(20) UNSIGNED NOT NULL,
  childBirthVisitID     BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  preferredDeliveryType VARCHAR(40),
  visitType             VARCHAR(20)         NOT NULL,

  deliveryDate          DATE                NOT NULL,
  deliveryDuration      TIME                NOT NULL,
  deliveryType          VARCHAR(40)         NOT NULL,
  numberOfGirlBabies    INT                 NOT NULL DEFAULT 0,
  numberOfBoyBabies     INT                 NOT NULL DEFAULT 0,

  pitocin               INT                 NOT NULL,
  nitrousOxide          INT                 NOT NULL,
  pethidine             INT                 NOT NULL,
  epiduralAnaesthesia   INT                 NOT NULL,
  magnesiumSulfate      INT                 NOT NULL,
  rhImmuneGlobulin      INT                 NOT NULL,

  comment               VARCHAR(400),
  PRIMARY KEY (childBirthVisitID),
  FOREIGN KEY (MID) REFERENCES patients (MID)
)
  ENGINE = MyISAM;