insert into DIRECTOR(NAME)
values ( 'fil' ),
       ('Иванов');

INSERT INTO mpa (NAME) VALUES ('G');
INSERT INTO mpa (NAME) VALUES ('PG');
INSERT INTO mpa (NAME) VALUES ('PG_13');
INSERT INTO mpa (NAME) VALUES ('R');
INSERT INTO mpa (NAME) VALUES ('NC_17');
INSERT INTO mpa (NAME) VALUES ('NR');
insert into FILM_GENRE (FILM_ID, GENRE_ID) values ( 1001, 1 );
insert into FILM_GENRE (FILM_ID, GENRE_ID) values ( 1006, 1 );
insert into FILM_GENRE (FILM_ID, GENRE_ID) values ( 1007, 2 );

insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (1001, 'Test1Name', 'Test1Login', 'test1@yandex.ru', '2010-11-01');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (1002, 'Test2Name', 'Test2Login', 'test2@yandex.ru', '2010-11-02');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (1003, 'Test3Name', 'Test3Login', 'test3@yandex.ru', '2010-11-03');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (1004, 'Test4Name', 'Test4Login', 'test4@yandex.ru', '2014-08-01');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (1005, 'Test5Name', 'Test5Login', 'test5@yandex.ru', '2015-09-01');
insert into USER_FILMORATE (ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values (1006, 'Test5Name', 'Test5Login', 'test5@yandex.ru', '2015-09-01');
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA, DIRECTOR_ID)
values (1000, 'Film1Name', 'Test1Desc', '2001-02-03', 150, 2, 1, 1);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (1001, 'Film1Name', 'Test1Desc', '2001-02-03', 150, 2, 1);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (1002, 'Film2Name', 'Test2Desc', '2002-03-04', 160, 3, 2);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (1003, 'Film3Name', 'Test3Desc', '2004-05-06', 190, 5, 4);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)
values (1004, 'Film4Name', 'Test4Desc', '2004-06-06', 190, 5, 4);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA)

values (1005, 'Film5Name', 'Test5Desc', '2004-08-06', 190, 4, 3);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA, DIRECTOR_ID)

values (1006, 'Film5Name', 'Test5Desc', '2004-08-06', 190, 4, 3, 1);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA, DIRECTOR_ID)
values (1007, 'Film5Name', 'Test5Desc', '2004-08-06', 190, 4, 3, 2);
insert into FILM (ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA, DIRECTOR_ID)
values (1008, 'Film5Name', 'Test5Desc', '2004-08-06', 190, 4, 3, 2);

insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (1001,1002,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (1001,1003,true, true);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (1002,1003,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (1002,1004,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (1002,1005,true, false);
insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, ACCEPT_FIRST, ACCEPT_SECOND)
values (1004,1005,false, true);
insert into LIKES (FILM_ID, USER_ID)
values (1001,1001);
insert into LIKES (FILM_ID, USER_ID)
values (1001,1002);
insert into LIKES (FILM_ID, USER_ID)
values (1001,1003);
insert into LIKES (FILM_ID, USER_ID)
values (1002,1001);
insert into LIKES (FILM_ID, USER_ID)
values (1002,1005);
insert into LIKES (FILM_ID, USER_ID)
values (1002,1003);
insert into LIKES (FILM_ID, USER_ID)
values (1003,1001);
insert into LIKES (FILM_ID, USER_ID)

values (1003,1003);


insert into LIKES (FILM_ID, USER_ID)
values (1005,1002);
insert into LIKES (FILM_ID, USER_ID)
values (1005,1003);
insert into LIKES (FILM_ID, USER_ID)
values (1005,1004);
insert into LIKES (FILM_ID, USER_ID)
values (1005,1005);

INSERT INTO EVENT (EVENT_TIME, USER_ID, EVENT_TYPE, ENTITY_ID, OPERATION) VALUES
     ('2022-06-07 21:09:07.198579',1002,'FRIEND',1100,'ADD'),
     ('2022-06-07 21:09:08.195967',1003,'FRIEND',1200,'REMOVE'),
     ('2022-06-07 21:09:08.195980',1009,'FRIEND',1100,'ADD'),
     ('2022-06-07 21:09:09.857885',1002,'LIKE',1004,'ADD'),
     ('2022-06-07 21:09:09.857885',1009,'LIKE',1004,'ADD');
