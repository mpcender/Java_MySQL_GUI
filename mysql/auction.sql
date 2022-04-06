USE auction;

CREATE TABLE FACILITY(
  Facility_ID           INTEGER      NOT NULL,
  Facility_User_Name    VARCHAR(32)  NOT NULL,
  Contact               VARCHAR(32)  NOT NULL,
  Created               DATE         NOT NULL,
  Last_Activity         DATE,
  Company_Name          VARCHAR(32),
  PRIMARY KEY(Facility_ID)
);

CREATE TABLE BIDDER(
   ID            INTEGER      NOT NULL,
   First_Name    VARCHAR(32)  NOT NULL,
   Last_Name     VARCHAR(32)  NOT NULL,
   User_Name     VARCHAR(32)  NOT NULL,
   Email         VARCHAR(64)  NOT NULL,
   Phone         VARCHAR(16)  NOT NULL,
   Street        VARCHAR(64)  NOT NULL,
   City          VARCHAR(32)  NOT NULL,
   State         VARCHAR(16)  NOT NULL,
   Zip           VARCHAR(16)  NOT NULL,
   Country       VARCHAR(32)  NOT NULL,
   Created       DATE         NOT NULL,
   Last_Activity DATE         NOT NULL,
   Company_Name  VARCHAR(64),
   
   PRIMARY KEY(ID),
   CONSTRAINT pk_bidder_info UNIQUE (User_Name, First_Name, Last_Name, Email, Phone) 
);

CREATE TABLE INVOICE(
   Invoice_Number      INTEGER  NOT NULL PRIMARY KEY, 
   Issued              DATE,
   User_Name           VARCHAR(32),
   Customer_First_Name VARCHAR(32),
   Customer_Last_Name  VARCHAR(32),
   Email               VARCHAR(64),
   Phone               VARCHAR(16),
   Address             VARCHAR(64),
   Preferred_Transfer  VARCHAR(24),
   City                VARCHAR(32),
   State               VARCHAR(16),
   Zip                 VARCHAR(16),
   Country             VARCHAR(32),
   Premium             VARCHAR(8),
   Non_Taxable         VARCHAR(8),
   Balance             VARCHAR(8),
   Tax                 VARCHAR(8),
   Total               VARCHAR(10),
   Payment             VARCHAR(10),
   Extra_Charges       DATE,
   Taxable             VARCHAR(8),
   Bidder_Number       INTEGER,
   Auction_Number      INTEGER,
   Auction_Name        VARCHAR(512),

   CONSTRAINT fk_bidder_info 
   FOREIGN KEY (User_Name, Customer_First_Name, Customer_Last_Name, Email, Phone) 
   REFERENCES bidder (User_Name, First_Name, Last_Name, Email, Phone) 
   ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE ITEM(
   Id              INTEGER  NOT NULL,
   Seller          INTEGER  NOT NULL,
   Sale            INTEGER  NOT NULL,
   Live            BIT  NOT NULL,
   Title           VARCHAR(512) NOT NULL,
   Date            VARCHAR(32) NOT NULL,
   Description     VARCHAR(512),
   Price           BIT  NOT NULL,
   Discount        BIT  NOT NULL,
   Quantity        BIT  NOT NULL,
   Image           BIT  NOT NULL,
   Category        INTEGER,
   Taxable         BIT  NOT NULL,
   Viewed          INTEGER  NOT NULL,
   Featured_Ad     BIT  NOT NULL,
   Tax             BIT  NOT NULL,
   Email           VARCHAR(64),
   Mapping_Address VARCHAR(64) NOT NULL,
   Mapping_City    VARCHAR(32) NOT NULL,
   Mapping_State   INTEGER  NOT NULL,
   Mapping_Country INTEGER  NOT NULL,
   Mapping_Zip     INTEGER  NOT NULL,
   Minimum_Bid     NUMERIC(7,2) NOT NULL,
   Starting_Bid    NUMERIC(7,2) NOT NULL,
   High_Bid        NUMERIC(7,2) NOT NULL,
   Final_Price     BIT  NOT NULL,
   Start_Time      VARCHAR(32)  NOT NULL,
   End_Time        VARCHAR(32)  NOT NULL,
   Lot_Number      VARCHAR(16) NOT NULL,
   Consignor       INTEGER NOT NULL,
   High_Bidder     VARCHAR(32),
   Invoice_Number  INTEGER,
   
   PRIMARY KEY (Id),
   FOREIGN KEY(Consignor) REFERENCES FACILITY(Facility_ID),
   FOREIGN KEY(Invoice_Number) REFERENCES INVOICE(Invoice_Number)
);

INSERT INTO BIDDER
VALUES
    (28,'SKIP','KELLER','NUGGET','B.T.KELLER@ATT.NET','5208203740','11891 W RANCHITO VERDE','TUCSON','AZ','85743','UNITED STATES','2018-01-13','2022-03-23','MR.'),
    (30,'JOHNNIE','RICE','JOHNNIERICE','JOHNNIERICE@MSN.COM','720-842-2864','9250 N JESSY LANE','TUCSON','AZ','85742','UNITED STATES','2018-01-13','2022-03-24',NULL),
    (43,'DARRELL','STEWART','RICCIROBBI','STEWART2001@MSN.COM','5202417544','8321 N ROSE MARIE LN','TUCSON','AZ','85742','UNITED STATES','2018-01-15','2022-03-25','R & D DEVELOPMENT, LLC'),
    (48,'GUILLERMO','GALINDO','GGALINDO','GGALINDO1000@GMAIL.COM','520-991-3551','4340 S KHE SANH LN','TUCSON','AZ','85735','UNITED STATES','2018-01-15','2022-03-25','TRANSFER TO TUCSON'),
    (50,'RAY','KENDRICK','SUGARAYK','SUGARAYK@AOL.COM','7929484','6302 S PARK AVE','TUCSON','AZ','85706','UNITED STATES','2018-01-15','2022-03-14',NULL),
    (53,'STEVEN','SCHULZE','GALLET','IMWATCHENU2@YAHOO.COM','602-295-3305','937 S VISTA RD','APACHE JUNCTION','AZ','85119','UNITED STATES','2018-01-15','2022-03-25','TRANSFER TO TEMPE'),
    (54,'JIM','BUCHANAN','GYMMYB5678','J.BUCHANAN50@YAHOO.COM','5206031002','3919 E. GUAYMAS PLACE','TUCSON','AZ','85711','UNITED STATES','2018-01-15','2022-03-25',NULL),
    (57,'DELORES','PRITCHARD','DWIGHTEHMAN@GMAIL.COM','DWIGHTEHMAN@GMAIL.COM','520-870-8353','8808 E. OLD SPANISH TR.','TUCSON','AZ','85710','UNITED STATES','2018-01-15','2022-03-25','QUALITY STEAM'),
    (60,'AARON','SHEPHERD','31000','GLEMAX97@YAHOO.COM','5202410099','7394 W SHINING AMBER LANE','TUCSON','AZ','85743','UNITED STATES','2018-01-16','2022-03-25','TRANSFER TO TUCSON'),
    (61,'LADD','VANCURA','AUCTIONS','REMEMBERLADD@YAHOO.COM','5208613756','3411 S CAMINO SECO','TUCSON','AZ','85730-2842','UNITED STATES','2018-01-16','2022-03-25','LADDS CUSTOM INSTALLATIONS'),
    (62,'WILLIAM','LAEMMEL','GONZO','3RDBASEBARAZ@GMAIL.COM','5209823248','3711 E. MILTON #12','TUCSON','AZ','85706','UNITED STATES','2018-01-16','2022-03-25',NULL),
    (1015,'JON','GORDON','YOURMOVE','JOHNCODEBLUE16@GMAIL.COM','520-307-7163','PO BOX 37022','TUCSON','AZ','85740','UNITED STATES','2018-06-18','2022-03-20',NULL);

INSERT INTO ITEM
VALUES
    (381532,471,5114,1,'Adjustable Swivel Desk Chair','03-01-2022 at 07:12:13',NULL,0,0,1,1,18,1,31,0,0,'j.buchanan50@buchanan50@yahoo.com','1441 E. 17th St','Tucson',161,226,85719,12.5,5,10,0,'02-24-2022 at 12:46:00','03-10-2022 at 07:48:30','2713',1,'Gymmyb5678',129306),
	(381509,471,5114,1,'Adjustable Swivel Desk Chair','03-01-2022 at 07:12:12',NULL,0,0,1,1,18,1,9,0,0,'j.buchanan50@yahoo.com','1441 E. 17th St','Tucson',161,226,85719,12.5,5,10,0,'02-24-2022 at 12:46:00','03-10-2022 at 07:37:00','2714',2,'Gymmyb5678',129306),
	(381525,471,5114,1,'Royal 1620Mx 16Sheet Crosscut Shredder','03-01-2022 at 07:12:13',NULL,0,0,1,1,18,1,5,0,0,'Blelc262@aol.com','1441 E. 17th St','Tucson',161,226,85719,20,5,17.5,0,'02-24-2022 at 12:46:00','03-10-2022 at 07:45:00','2732',320,'Custer',129307),
	(381506,471,5114,1,'Foldable Chair With Bag And Foot Rest','03-01-2022 at 07:12:12',NULL,0,0,1,1,18,1,5,0,0,'janju10@sbcglobal.net','1441 E. 17th St','Tucson',161,226,85719,8,5,7,0,'02-24-2022 at 12:46:00','03-10-2022 at 07:35:30','2711',270,'janju10',129308),
	(381498,471,5114,1,'6Ft X3Ft x 11.5"  6 Tier Shelf','03-01-2022 at 07:12:12',NULL,0,0,1,1,18,1,18,0,0,'johncodeblue16@gmail.com','1441 E. 17th St','Tucson',161,226,85719,30,5,25,0,'02-24-2022 at 12:46:00','03-10-2022 at 07:31:30','2703',324,'yourmove',129309),
	(381507,471,5114,1,'71" x 3Ft x 30" Oval Table-Desk','03-01-2022 at 07:12:12',NULL,0,0,1,1,18,1,25,0,0,'johncodeblue16@gmail.com','1441 E. 17th St','Tucson',161,226,85719,30,5,25,0,'02-24-2022 at 12:46:00','03-10-2022 at 07:36:00','2712',416,'yourmove',129309),
	(381544,471,5114,1,'Adjustable Swivel Desk Chair','03-01-2022 at 07:12:13',NULL,0,0,1,1,18,1,14,0,0,'johncodeblue16@gmail.com','1441 E. 17th St','Tucson',161,226,85719,15.11,5,12.61,0,'02-24-2022 at 12:46:00','03-10-2022 at 07:54:30','2716',319,'yourmove',129309);
    
INSERT INTO INVOICE
VALUES
    (129306,'2022-03-11','NUGGET','SKIP','KELLER','B.T.KELLER@ATT.NET','5208203740','3919 E. Guaymas Place',NULL,'Tucson','Arizona','85711','United States','$4.50','$0','$0','$3','$37.50','$37.50','2000-01-01','$30',54,5114,'TUCSON Computer And Office Equipment Auction <font color = 00ccff> Thursday  7:30PM 3/10/22 </font> ID: 5114  <font color = green> (ADDITIONAL LOTS ADDED 3/1/22!) </font>  (TUC)'),
    (129307,'2022-03-11','RICCIROBBI','DARRELL','STEWART','STEWART2001@MSN.COM','5202417544','1985 W Calle Estio. Green Valley AZ','Transfer To Tucson','Green Valley','Arizona','85622','United States','$9.38','$0','$0','$6.25','$78.13','$78.13','2000-01-01','$62.50',114,5114,'TUCSON Computer And Office Equipment Auction <font color = 00ccff> Thursday  7:30PM 3/10/22 </font> ID: 5114  <font color = green> (ADDITIONAL LOTS ADDED 3/1/22!) </font>  (TUC)'),
    (129308,'2022-03-11','GONZO','WILLIAM','LAEMMEL','3RDBASEBARAZ@GMAIL.COM','5209823248','405 Skyward Dr','Transfer To Tucson','Aptos','California','950033014','United States','$1.05','$0','$0','$0.70','$8.75','$8.75','2000-01-01','$7',357,5114,'TUCSON Computer And Office Equipment Auction <font color = 00ccff> Thursday  7:30PM 3/10/22 </font> ID: 5114  <font color = green> (ADDITIONAL LOTS ADDED 3/1/22!) </font>  (TUC)'),
    (129309,'2022-03-11','yourmove','Jon','Gordon','johncodeblue16@gmail.com','520-307-7163','PO Box 37022',NULL,'Tucson','Arizona','85740','United States','$28.97','$0','$0','$19.31','$241.41','$241.41','2000-01-01','$193.13',1015,5114,'TUCSON Computer And Office Equipment Auction <font color = 00ccff> Thursday  7:30PM 3/10/22 </font> ID: 5114  <font color = green> (ADDITIONAL LOTS ADDED 3/1/22!) </font>  (TUC)');

INSERT INTO FACILITY
VALUES
    (1,'supportauction-tucso','Mike Cummings','2018-01-11',NULL,'Auction Tucson'),
    (2,'rdid132','Enzo Rdid','2018-01-11','2018-10-01','HZLA Liquidation'),
    (320,'bobbyapocalypsegloba','Bobby Sutton','2020-07-20','2020-07-28','Screamers Inc'),
    (270,'(KAR)','Kelly Reed','2019-12-12',NULL,'-'),
    (324,'(CCL)','Grant Cummings','2020-08-25','2021-03-05','Auction Phoenix'),
    (416,'(BHA)','Nicole Boddie','2021-06-21',NULL,'The Humble Bee Thrift Store'),
    (319,'(BEL)','Matt Rowan','2020-07-15',NULL,'Nine Lives'),
    (431,'(PGW)','Worldwide','2021-08-18',NULL,'PG WorldWide'),
    (450,'(EMP)','Anthony Page','2021-11-17',NULL,'Empire Liquidators');