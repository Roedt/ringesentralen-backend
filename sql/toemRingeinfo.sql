delete from `call` where oppringtNummer like '123456%' or oppringtNummer like '223456%';
update `person` set sisteSamtale=0 where oppretta = '2020-08-22 23:29:09';
update `person` set groupID=1 where groupID in(2, 3, 7) and oppretta = '2020-08-22 23:29:09';