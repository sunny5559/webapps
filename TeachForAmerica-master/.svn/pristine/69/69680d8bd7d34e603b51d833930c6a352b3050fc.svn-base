CREATE TABLESPACE tbs_perm_01
  DATAFILE 'tbs_perm_01.dat' 
    SIZE 200M
  ONLINE;


CREATE TEMPORARY TABLESPACE tbs_temp_01
  TEMPFILE 'tbs_temp_01.dbf'
    SIZE 100M
    AUTOEXTEND ON;

CREATE USER tfauser
  IDENTIFIED BY tfapass
  DEFAULT TABLESPACE tbs_perm_01
  TEMPORARY TABLESPACE tbs_temp_01
  QUOTA 200M on tbs_perm_01;


grant create session to tfauser
GRANT create table TO tfauser;
GRANT create view TO tfauser;
GRANT create any trigger TO tfauser;
GRANT create any procedure TO tfauser;
GRANT create sequence TO tfauser;
GRANT create synonym TO tfauser;
