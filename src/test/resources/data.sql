insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (1, 'Test1Name', 'Test1Login', 'test1@yandex.ru', '10-11-01');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (2, 'Test2Name', 'Test2Login', 'test2@yandex.ru', '10-11-02');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (3, 'Test3Name', 'Test3Login', 'test3@yandex.ru', '10-11-03');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (4, 'Test4Name', 'Test4Login', 'test4@yandex.ru', '14-08-01');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (5, 'Test5Name', 'Test5Login', 'test5@yandex.ru', '15-09-01');
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (1, 'Film1Name', 'Test1Desc', '01-02-03', 150, 2, 1);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (2, 'Film2Name', 'Test2Desc', '02-03-04', 160, 3, 2);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (3, 'Film3Name', 'Test3Desc', '04-05-06', 190, 5, 4);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (4, 'Film4Name', 'Test4Desc', '04-06-06', 190, 5, 4);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (5, 'Film5Name', 'Test5Desc', '04-08-06', 190, 4, 3);
INSERT INTO mpa (name) VALUES ('G');
INSERT INTO mpa (name) VALUES ('PG');
INSERT INTO mpa (name) VALUES ('PG_13');
INSERT INTO mpa (name) VALUES ('R');
INSERT INTO mpa (name) VALUES ('NC_17');
INSERT INTO mpa (name) VALUES ('NR');
insert into FILM_MPA (FILM_ID, MPA_ID)
values (1,1);
insert into FILM_MPA (FILM_ID, MPA_ID)
values (2,2);
insert into FILM_MPA (FILM_ID, MPA_ID)
values (3,4);
insert into FILM_MPA (FILM_ID, MPA_ID)
values (4,4);
insert into FILM_MPA (FILM_ID, MPA_ID)
values (5,3);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (1,2,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (1,3,true, true);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (2,3,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (2,4,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (2,5,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (4,5,false, true);
insert into LIKES (FILM_ID, USER_ID)
values (1,1);
insert into LIKES (FILM_ID, USER_ID)
values (1,2);
insert into LIKES (FILM_ID, USER_ID)
values (1,3);
insert into LIKES (FILM_ID, USER_ID)
values (2,1);
insert into LIKES (FILM_ID, USER_ID)
values (2,5);
insert into LIKES (FILM_ID, USER_ID)
values (2,3);
insert into LIKES (FILM_ID, USER_ID)
values (3,1);
insert into LIKES (FILM_ID, USER_ID)
values (3,3);