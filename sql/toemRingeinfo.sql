delete from `call` where calledPhone like '123456%' or calledPhone like '223456%';
update `person` set lastCall=0 where nameEnlister in ('Guffen', 'Dolly', 'Julius');
update `person` set groupID=1 where groupID in(2, 3, 7) and nameEnlister in ('Guffen', 'Dolly', 'Julius');