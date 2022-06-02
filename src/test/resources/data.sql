insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (10, 'Test1Name', 'Test1Login', 'test1@yandex.ru', '2010-11-01');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (20, 'Test2Name', 'Test2Login', 'test2@yandex.ru', '2010-11-02');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (30, 'Test3Name', 'Test3Login', 'test3@yandex.ru', '2010-11-03');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (40, 'Test4Name', 'Test4Login', 'test4@yandex.ru', '2014-08-01');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (50, 'Test5Name', 'Test5Login', 'test5@yandex.ru', '2015-09-01');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (60, 'Test0Name', 'Test0Login', 'test0@yandex.ru', '2000-09-01');
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (10, 'Film1Name', 'Test1Desc', '2001-02-03', 150, 2, 1);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (20, 'Film2Name', 'Test2Desc', '2002-03-04', 160, 3, 2);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (30, 'Film3Name', 'Test3Desc', '2004-05-06', 190, 5, 4);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (40, 'Film4Name', 'Test4Desc', '2004-06-06', 190, 5, 4);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (50, 'Film5Name', 'Test5Desc', '2004-08-06', 190, 4, 3);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (60, 'Film6Name', 'Test6Desc', '2003-08-06', 110, 1, 1);
INSERT INTO mpa (title) VALUES ('G');
INSERT INTO mpa (title) VALUES ('PG');
INSERT INTO mpa (title) VALUES ('PG_13');
INSERT INTO mpa (title) VALUES ('R');
INSERT INTO mpa (title) VALUES ('NC_17');
INSERT INTO mpa (title) VALUES ('NR');
insert into FILM_MPA (FILM_ID, MPA_ID)
values (10,1);
insert into FILM_MPA (FILM_ID, MPA_ID)
values (20,2);
insert into FILM_MPA (FILM_ID, MPA_ID)
values (30,4);
insert into FILM_MPA (FILM_ID, MPA_ID)
values (40,4);
insert into FILM_MPA (FILM_ID, MPA_ID)
values (50,3);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (10,20,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (10,30,true, true);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (20,30,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (20,40,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (20,50,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (40,50,false, true);
insert into LIKES (FILM_ID, USER_ID)
values (10,10);
insert into LIKES (FILM_ID, USER_ID)
values (10,20);
insert into LIKES (FILM_ID, USER_ID)
values (10,30);
insert into LIKES (FILM_ID, USER_ID)
values (20,10);
insert into LIKES (FILM_ID, USER_ID)
values (20,50);
insert into LIKES (FILM_ID, USER_ID)
values (20,30);
insert into LIKES (FILM_ID, USER_ID)
values (30,10);
insert into LIKES (FILM_ID, USER_ID)
values (30,30);