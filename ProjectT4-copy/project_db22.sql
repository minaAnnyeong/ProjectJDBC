
DROP TABLE admin_acc CASCADE CONSTRAINTS;

CREATE TABLE admin_acc (
    admin_id VARCHAR2(20) PRIMARY KEY,
    admin_pw VARCHAR2(20),
    rest_id    NUMBER(5),
    CONSTRAINT fk_rest_id FOREIGN KEY ( rest_id )
        REFERENCES resta_se ( rest_id ) 
);
-- 어드민 계정 테이블 O

-- 예시 : 테스트용 admin계정 : admin_id = 'hansik1' , admin_pw = 'passwod1!', rest_id = 1
--                           >> '식당1' 의 운영자 역할
insert into admin_acc values ('korean', 'password1!', 1);

insert into admin_acc values ('indian', 'password1!', 2);

insert into admin_acc values ('french', 'password1!', 3);


DROP TABLE user_acc CASCADE CONSTRAINTS;

CREATE TABLE user_acc (
    user_id    VARCHAR2(20) PRIMARY KEY,
    user_pw    VARCHAR2(20),
    user_name  VARCHAR2(20),
    user_phone VARCHAR2(20)
);
-- 유저 계정 테이블 O

select * from user_acc;


DROP TABLE resta_se CASCADE CONSTRAINTS;

CREATE TABLE resta_se (
    rest_id   NUMBER(5) PRIMARY KEY,
    rest_name VARCHAR2(20)
);
-- 음식점 (체인점) 예약 O
-- 아래는 가게 테이블 입력 값
insert into resta_se
values (1,'한식');
insert into resta_se
values (2,'인도식');
insert into resta_se
values (3,'프랑스식');

--select * from resta_se;


DROP TABLE menu CASCADE CONSTRAINTS;

CREATE TABLE menu (
    menu_id    NUMBER(5) PRIMARY KEY,
    rest_id    NUMBER(5),
    menu_name  VARCHAR2(20),
    menu_price NUMBER(5),
    img_path  VARCHAR2(50), ------------------
    CONSTRAINT fk_rest_id_1 FOREIGN KEY ( rest_id )
        REFERENCES resta_se ( rest_id )
);
-- 메뉴 테이블 O

-- 아래는 가게 테이블 입력 값


-- 아래는 메뉴 테이블 입력 값

insert into menu
values (01,1,'팔절판',15000, '/food1/food1.jpg');
insert into menu
values (02,1,'불고기',12000, '/food1/food2.jpg');
insert into menu
values (03,1,'비빔밥',9000, '/food1/food3.jpg');
insert into menu
values (04,1,'김치찌개',8000, '/food1/food4.jpg');
insert into menu
values (05,1,'삼계탕',13000, '/food1/food5.jpg');
insert into menu
values (06,1,'삼겹살',12000, '/food1/food6.jpg');
insert into menu
values (07,1,'김치볶음밥',8000, '/food1/food7.jpg');
insert into menu
values (08,1,'수육',15000, '/food1/food8.jpg');
insert into menu
values (09,1,'김밥',3000, '/food1/food9.jpg');
insert into menu
values (10,1,'떡볶이',5000, '/food1/food10.jpg');
insert into menu
values (11,1,'곱창',13000, '/food1/food11.jpg');
insert into menu
values (12,1,'잡채',8000, '/food1/food12.jpg');


-- 식당2 메뉴 정보

insert into menu
values (13,2,'마살라',15000, '/food2/food1.jpg');
insert into menu
values (14,2,'난',3000, '/food2/food2.jpg');
insert into menu
values (15,2,'비리야니',15000, '/food2/food3.jpg');
insert into menu
values (16,2,'사모사',9000, '/food2/food4.jpg');
insert into menu
values (17,2,'파코라',8000, '/food2/food5.jpg');
insert into menu
values (18,2,'굴랍 자문',9000, '/food2/food6.jpg');
insert into menu
values (19,2,'코르마',15000, '/food2/food7.jpg');
insert into menu
values (20,2,'코프타',14000, '/food2/food8.jpg');
insert into menu
values (21,2,'파니 푸리',8000, '/food2/food9.jpg');
insert into menu
values (22,2,'자레비',6000, '/food2/food10.jpg');
insert into menu
values (23,2,'카주 카틀리',9000, '/food2/food11.jpg');
insert into menu
values (24,2,'라자마',13000, '/food2/food12.jpg');

--식당 3 메뉴 정보
insert into menu
values (25,3,'크로아상',4500, '/food3/food1.jpg');
insert into menu
values (26,3,'라따뚜이',15000, '/food3/food2.jpg');
insert into menu
values (27,3,'크레페',12000, '/food3/food3.jpg');
insert into menu
values (28,3,'푸아그라',40000, '/food3/food4.jpg');
insert into menu
values (29,3,'마카롱',3500, '/food3/food5.jpg');
insert into menu
values (30,3,'갈레트',11000, '/food3/food6.jpg');
insert into menu
values (31,3,'크루통',3000, '/food3/food7.jpg');
insert into menu
values (32,3,'퐁듀',20000, '/food3/food8.jpg');
insert into menu
values (33,3,'키시',12000, '/food3/food9.jpg');
insert into menu
values (34,3,'클라푸티',8000, '/food3/food10.jpg');
insert into menu
values (35,3,'바게트',12000, '/food3/food11.jpg');
insert into menu
values (36,3,'수플레',13000, '/food3/food12.jpg');

select * from menu;



DROP TABLE user_rsv CASCADE CONSTRAINTS;

CREATE TABLE user_rsv (
    user_rsv_id NUMBER(5) PRIMARY KEY,
    user_id     VARCHAR(20),
    rest_id     NUMBER(5),
    user_count  NUMBER(5),
    rev_time    VARCHAR2(20),
    CONSTRAINT fk_user_id FOREIGN KEY ( user_id )
        REFERENCES user_acc ( user_id ),
    CONSTRAINT fk_rest_id_2 FOREIGN KEY ( rest_id )
        REFERENCES resta_se ( rest_id )
);
-- 유저 예약 테이블 O

DROP SEQUENCE user_rsv_seq;

CREATE SEQUENCE user_rsv_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

select * from user_rsv order by rev_time;

select * from user_rsv order by user_rsv_id;

insert into user_rsv values (3, 'hong', 2, 2, '2025-4-23 12:00');

--select * from user_rsv where user_id = 'hong' order by rev_time;

DROP TABLE user_rsv_menu CASCADE CONSTRAINTS;

CREATE TABLE user_rsv_menu (
    user_rsv_menu_id NUMBER(5) PRIMARY KEY,
    user_rsv_id      NUMBER(5),
    menu_id          NUMBER(5),
    menu_count       NUMBER(5),
    CONSTRAINT fk_menu_id FOREIGN KEY ( menu_id )
        REFERENCES menu ( menu_id ),
    CONSTRAINT fk_user_rsv_id FOREIGN KEY ( user_rsv_id )
        REFERENCES user_rsv ( user_rsv_id )
);
-- 유저 예약 메뉴 테이블 o


DROP SEQUENCE user_rsv_menu_seq;

CREATE SEQUENCE user_rsv_menu_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;



select * from user_rsv_menu order by user_rsv_id;


--DROP TABLE admin_rsv CASCADE CONSTRAINTS;
--
--CREATE TABLE admin_rsv (
--    admin_rsv_id NUMBER(5) PRIMARY KEY,
--    user_rsv_id  NUMBER(5),
--    admin_id     VARCHAR2(20),
--    CONSTRAINT fk_user_rsv_id_2 FOREIGN KEY ( user_rsv_id )
--        REFERENCES user_rsv ( user_rsv_id ),
--    CONSTRAINT fk_admin_id FOREIGN KEY ( admin_id )
--        REFERENCES admin_acc ( admin_id )
--);
-- 어드민 예약 확인 테이블 


commit;
--

--
--select * 
--from admin_acc, resta_se
--where admin_acc.rest_id = resta_se.rest_id
--order by admin_acc.rest_id;
--
--select * from user_acc;
--
--select * from resta_se;
--
--select * from menu;
--
--select * from admin_rsv;
--
--select * from user_rsv;
--
--select * from user_rsv_menu;


-- select문

