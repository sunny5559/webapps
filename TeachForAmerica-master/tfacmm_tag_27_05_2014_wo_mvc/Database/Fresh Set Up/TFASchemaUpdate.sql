ALTER TABLE tfauser.MTLD  ADD (MD_TLD  VARCHAR2(1024 CHAR));


update tfauser.mtld set LONGITUDE='' , LATITUDE='';
alter table tfauser.mtld modify (LATITUDE number(12,8) , LONGITUDE number(12,8));


update tfauser.school set LONGITUDE='' , LATITUDE='';
alter table tfauser.school modify (LATITUDE number(12,8) , LONGITUDE number(12,8));

update tfauser.cohort_detail set criteria_score ='';
ALTER TABLE tfauser.cohort_detail MODIFY criteria_score NVARCHAR2(2000);


update tfauser.criteria_category set name='Geographic' where name='Geography';

alter table tfauser.cohort add overDistance number(1);
alter table tfauser.cohort_detail add is_NotFitForCohort number(1);

alter table tfauser.mtld drop column school_id;

alter table tfauser.cohort add basicsCriteriaPer varchar2(20);

alter table tfauser.cohort add contentCriteriaPer varchar2(20);

alter table tfauser.cohort add geographicCriteriaPer varchar2(20);

alter table tfauser.cohort add relationshipsCriteriaPer varchar2(20);

alter table tfauser.cohort add max_distance varchar2(20);

alter table tfauser.COHORT_DETAIL add  FIRST_SEEDING_CORPSMEMBER  CHAR(1);

commit;

ALTER TABLE tfauser.CORP_MEMBER
ADD (ethnicity VARCHAR2(255 BYTE));

alter table tfauser.mtld drop column  SCHOOL_DISTRICT_CORPS;



/*23/04/2014-@Author-Dheeraj 
Purpose- CM_Filter table is no more in use.*/
alter table tfauser.corp_member drop column filter_id;


/*24/04/2014- @Author - Arun
Purpose- Added new column to track user logged in status.
*/
ALTER TABLE tfauser.TFA_USER
ADD (is_logged_in NUMBER(1));

/*24/04/2014- @Author - Arun
Purpose- Renamed the column names and changed the datatypes.
*/
ALTER TABLE tfauser.CORP_MEMBER ADD CORPS_YEARS NUMBER(10);
UPDATE tfauser.CORP_MEMBER SET CORPS_YEARS=CORPSYEAR;
ALTER TABLE tfauser.CORP_MEMBER DROP COLUMN CORPSYEAR;

ALTER TABLE tfauser.CORP_MEMBER ADD PERSON_COLOR NUMBER(1);
UPDATE tfauser.CORP_MEMBER SET PERSON_COLOR=PERSON_OF_COLOR;
ALTER TABLE tfauser.CORP_MEMBER DROP COLUMN PERSON_OF_COLOR;


ALTER TABLE tfauser.MTLD ADD CURRENT_TENURE1 NUMBER(10);
UPDATE tfauser.MTLD SET CURRENT_TENURE1=CURRENT_TENURE;
ALTER TABLE tfauser.MTLD DROP COLUMN CURRENT_TENURE;
ALTER TABLE tfauser.MTLD RENAME COLUMN CURRENT_TENURE1 TO CURRENT_TENURE;

ALTER TABLE tfauser.MTLD ADD LOW_INCOME_BACKGROUND1 NUMBER(1);
ALTER TABLE tfauser.MTLD DROP COLUMN LOW_INCOME_BACKGROUND;
ALTER TABLE tfauser.MTLD RENAME COLUMN LOW_INCOME_BACKGROUND1 TO LOW_INCOME_BACKGROUND;

ALTER TABLE tfauser.MTLD ADD PERSON_COLOR1 NUMBER(1);
update tfauser.mtld set person_color = '0' where person_color='N';
update tfauser.mtld set person_color = '1' where person_color='Y';
UPDATE tfauser.MTLD SET PERSON_COLOR1=PERSON_COLOR;
ALTER TABLE tfauser.MTLD DROP COLUMN PERSON_COLOR;
ALTER TABLE tfauser.MTLD RENAME COLUMN PERSON_COLOR1 TO PERSON_COLOR;

alter table tfauser.CORP_MEMBER drop (DOB,GENDER,HIRING_STAGE,HOME_PHONE,PROVINCE,SSN);
/*24/04/2014- @Author - Arun Ends*/


/*24/04/2014- @Author - Dheeraj
Purpose- Index created for the columns region_id,school_id,cohort_id of tables mtld,cohort,school,cohort_detail
*/

CREATE INDEX cohort_region_id ON tfauser.Cohort(region_id);
CREATE INDEX mld_region_id ON tfauser.mtld(region_id);
CREATE INDEX school_region_id ON tfauser.school(region_id);
CREATE INDEX cohort_detail_cohort_id ON tfauser.Cohort_detail(cohort_id);
CREATE INDEX corp_member_school_id ON tfauser.corp_member(school_id);

/*
Added by Lovely 30/04/2014
*/
alter table TFAUSER.REGION drop column cohort_count;
alter table TFAUSER.corp_member drop column is_hired;

ALTER TABLE TFAUSER.COHORT RENAME COLUMN basicsCriteriaPer TO BASICS_CRITERIA_PER;
ALTER TABLE TFAUSER.COHORT RENAME COLUMN contentCriteriaPer TO CONTENT_CRITERIA_PER;
ALTER TABLE TFAUSER.COHORT RENAME COLUMN geographicCriteriaPer TO GEOGRAPHIC_CRITERIA_PER;
ALTER TABLE TFAUSER.COHORT RENAME COLUMN RELATIONSHIPSCRITERIAPER TO RELATIONSHIPS_CRITERIA_PER;
/*
Added by Lovely 05/05/2014
*/
ALTER TABLE TFAUSER.CORP_MEMBER ADD HIRED_STATUS varchar2(50);



/*12/05/2014- @Author - Dheeraj
Purpose- Index created for the column is_final_cohort of table cohort
*/
create index cohort_is_final_cohort_idx on cohort(is_final_cohort);



/*14/05/2014- @Author - Dheeraj
Purpose- Removed unused column cell_phone from corp_member table
*/
alter table corp_member drop column cell_phone;

/*26/05/2014- @Author - Lovely
Purpose- update CLASS_API column value in   CRITERIA table for criteria id =16
*/
update TFAUSER.CRITERIA set CLASS_API = 'MTLDDistrictScoringCriteria' where CRITERIA_ID=16;