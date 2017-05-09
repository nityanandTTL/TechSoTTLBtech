DROP TABLE IF EXISTS order_details;
CREATE TABLE order_details (VisitId character varying(100),Slot character varying(20),Response character varying (200),SlotId integer,BrandId integer,OrderNo character varying(50),Address character varying(500),Pincode character varying(10),Mobile character varying(20),Email character varying(50),PayType character varying(50),AmountDue integer,Margin integer,Discount integer,Refcode character varying(50),ProjId character varying(50),ReportHC integer,isTestEdit character varying(20),isAddBen character varying(20),kits character varying(500),Distance integer,Latitude character varying(10),Longitude character varying(10),Status character varying(50),createdAt bigint,createdBy character varying(100),updatedAt bigint,updatedBy character varying(100),recordStatus character varying(10),syncAction character varying(10),syncStatus character varying(10));

DROP TABLE IF EXISTS beneficiary_details;
CREATE TABLE beneficiary_details (benId integer,OrderNo character varying(50),Name character varying(100),Age integer,Gender character varying(20),tests character varying(200),Fasting character varying(50),Venepuncture BLOB,barcodedtl json,sampleType json,testsCode character varying(500),testsList json,createdAt bigint,createdBy character varying(100),updatedAt bigint,updatedBy character varying(100),recordStatus character varying(10),syncAction character varying(10),syncStatus character varying(10));

DROP TABLE IF EXISTS test_rate_master;
CREATE TABLE test_rate_master (TestId integer,BrandId integer,BrandName character varying(50),TestCode character varying(50),TestType character varying(50),Fasting character varying(50),sampltype json,LastMealEat integer,Rate integer,Discount integer,Incentive integer,Description character varying(200),chldtests json,tstSkills json,tstClinicalHistory json,createdAt bigint,createdBy character varying(100),updatedAt bigint,updatedBy character varying(100),recordStatus character varying(10),syncAction character varying(10),syncStatus character varying(10));

DROP TABLE IF EXISTS brand_master;
CREATE TABLE brand_master(BrandId integer,BrandName character varying(100),BrandAddress character varying(500),cancellationFee integer,bookingInterval integer,collectionInterval integer,isAuthentication character varying(10),Wallet character varying(10),multiplePayments character varying(10),crncydetls json,Response character varying(100),createdAt bigint,createdBy character varying(100),updatedAt bigint,updatedBy character varying(100),recordStatus character varying(10),syncAction character varying(10),syncStatus character varying(10));

DROP TABLE IF EXISTS lab_alert_master;
CREATE TABLE lab_alert_master(LabAlertId integer,LabAlert character varying(200),createdAt bigint,createdBy character varying(100),updatedAt bigint,updatedBy character varying(100),recordStatus character varying(10),syncAction character varying(10),syncStatus character varying(10));

