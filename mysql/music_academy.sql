-- Init Database --
CREATE DATABASE IF NOT EXISTS music_academy;
use music_academy;

-- ENTITIES --

-- Entity: OWNER --
-- ("Superclass" UNION of PERSON, BAND) --
CREATE TABLE `OWNER` (
	`Owner_Id` 	INT		AUTO_INCREMENT	PRIMARY KEY
);

-- Entity: PERSON --
-- ("Superclass" of INSTRUMENT_TEACHER, INSTRUMENT_MAINTAINER, MUSICIAN) --
-- ("Subclass" of OWNER) --
CREATE TABLE `PERSON` (
	`SSN`		VARCHAR(11)		NOT NULL	PRIMARY KEY,
	`Address` 	VARCHAR(255)    NOT NULL,
	`Phone`		VARCHAR(20)		NOT NULL,
	`Owner_Id`	INT,

	FOREIGN KEY (`Owner_Id`) REFERENCES `OWNER`(`Owner_Id`)
);

-- Entity: BAND --
-- ("Subclass" of OWNER) --
CREATE TABLE `BAND` (
	`Reg#`		INT		AUTO_INCREMENT	PRIMARY KEY,
    `Address` 	VARCHAR(255)    NOT NULL,
	`Phone`		VARCHAR(20)		NOT NULL,
    `Name`		VARCHAR(255)    NOT NULL,
    `Owner_Id`	INT,
    
    FOREIGN KEY (`Owner_Id`) REFERENCES `OWNER`(`Owner_Id`)
);

-- Entity: INSTRUMENT_TEACHER --
-- ("Subclass" of PERSON (FK = PK)) --
CREATE TABLE `INSTRUMENT_TEACHER` (
  `SSN`			VARCHAR(11)     NOT NULL	PRIMARY KEY,
  `Pay`			DECIMAL(13,2) 	NOT NULL,
  `Skill`		VARCHAR(32)    	NOT NULL,
  `Shift`		VARCHAR(32),
  
  FOREIGN KEY (`SSN`) REFERENCES `PERSON`(`SSN`)
);

-- Entity: INSTRUMENT_MAINTAINER --
-- ("Subclass" of PERSON (FK = PK)) --
-- (Relationship 1-M maintins INSTRUMENT_TYPE) --
-- (Relationship M-N REPAIRS REPAIR_ORDER ) --
CREATE TABLE `INSTRUMENT_MAINTAINER` (
  `SSN`			VARCHAR(11)     NOT NULL	PRIMARY KEY,
  `Wages`		DECIMAL(13,2) 	NOT NULL,
  `Hours`		VARCHAR(32),
  
  FOREIGN KEY (`SSN`) REFERENCES `PERSON`(`SSN`)
);

-- Entity: MUSICIAN --
-- ("Subclass" of PERSON (FK = PK)) --
-- (Relationship M-N PLAYS INSTRUMENT_TYPE  ) --
CREATE TABLE `MUSICIAN` (
  `SSN`				VARCHAR(11)     NOT NULL	PRIMARY KEY,
  `Specialization`	VARCHAR(32),
  
  FOREIGN KEY (`SSN`) REFERENCES `PERSON`(`SSN`)
);

-- Entity: INSTRUMENT_TYPE --
-- ("Subclass" of PERSON) --
-- (Relationship INSTRUMENT_TEACHER 	TEACHES  	M-N ) --
-- (Relationship INSTRUMENT_MAINTAINER 	maintains 	1-N (Include FK) ) --
-- (Relationship MUSICIAN				PLAYS		M-N ) --
-- (Relationship MUSICAL_INSTRUMENT		of_type		N-1 ) --
CREATE TABLE `INSTRUMENT_TYPE` (
	`Model`			VARCHAR(64)		NOT NULL	PRIMARY KEY,
    `Weight`		VARCHAR(32)		NOT NULL,
    `Maintainer` 	VARCHAR(11)     NOT NULL,
    
    FOREIGN KEY (`Maintainer`) REFERENCES `INSTRUMENT_MAINTAINER`(`SSN`)
);

-- Entity: ROOMS --
-- (Relationship MUSICAL_INSTRUMENT		stored_in		N-1 ) --
CREATE TABLE `ROOMS` (
	`Number`	INT		NOT NULL	PRIMARY KEY,
    `Capacity`	INT		NOT NULL
);

-- Entity: MUSICAL_INSTRUMENT --
-- (Relationship INSTRUMENT_TYPE	of_type		1-N (Include FK) ) --
-- (Relationship ROOMS				stored_in	1-N (Include FK) ) --
-- (Relationship OWNER				OWNS		M-N 			 ) --
CREATE TABLE `MUSICAL_INSTRUMENT` (
	`No`	INT			NOT NULL,
    `Name`	VARCHAR(64)	NOT NULL,
    `Model`	VARCHAR(64)	NOT NULL,
    `Room`	INT			NOT NULL,
    
    FOREIGN KEY (`Model`) REFERENCES `INSTRUMENT_TYPE`(`Model`),
    FOREIGN KEY (`Room`) REFERENCES `ROOMS`(`Number`),
    
    PRIMARY KEY (`No`, Model, Room)
);

-- Weak Entity: REPAIR_ORDER --
CREATE TABLE `REPAIR_ORDER` (
	`Code`			INT				AUTO_INCREMENT,
	`Date`			DATE			NOT NULL,
    `Instrument`	INT				NOT NULL,
	
    FOREIGN KEY (`Instrument`) REFERENCES `MUSICAL_INSTRUMENT`(`No`),
    PRIMARY KEY (`Code`, `Date`, `Instrument`)
); 

-- RELATIONSHIPS --
-- Relationship: REPAIRS (Many to Many) --
CREATE TABLE `REPAIRS` (
	`Maintainer`	VARCHAR(11)     NOT NULL,
    `Code`			INT				NOT NULL,
	`Date`			DATE			NOT NULL,
    FOREIGN KEY (`Maintainer`) REFERENCES `INSTRUMENT_MAINTAINER`(`SSN`),
	FOREIGN KEY (`Code`, `Date`) REFERENCES `REPAIR_ORDER`(`Code`, `Date`),
    PRIMARY KEY (`Code`, `Date`, `Maintainer`)
);

-- Relationship: OWNS (Many to Many) --
CREATE TABLE `OWNS` (
	 `Owner_Id`	INT		NOT NULL,
     `No`		INT		NOT NULL,
     `rdate` 		DATE	NOT NULL,
     
     FOREIGN KEY (`Owner_Id`) REFERENCES `OWNER`(`Owner_Id`),
     FOREIGN KEY (`No`) REFERENCES `MUSICAL_INSTRUMENT`(`No`),
     PRIMARY KEY (`Owner_Id`, `No`, `rdate`)
);

-- Relationship: TEACHES (Many to Many) --
CREATE TABLE `TEACHES`(
	`SSN`			VARCHAR(11)     NOT NULL,
    `Model`		VARCHAR(64)		NOT NULL,
	
    FOREIGN KEY (`SSN`) REFERENCES `INSTRUMENT_TEACHER`(`SSN`),
	FOREIGN KEY (`Model`) REFERENCES `INSTRUMENT_TYPE`(`Model`),
    PRIMARY KEY (`SSN`, `Model`)
);

-- Relationship: PLAYS (Many to Many) --
CREATE TABLE `PLAYS` (
	`Musician`		VARCHAR(11)     NOT NULL,
    `Instrument`	VARCHAR(64)		NOT NULL,
	
    FOREIGN KEY (`Musician`) REFERENCES `MUSICIAN`(`SSN`),
	FOREIGN KEY (`Instrument`) REFERENCES `INSTRUMENT_TYPE`(`Model`),
    PRIMARY KEY (`Musician`, `Instrument`)
);




