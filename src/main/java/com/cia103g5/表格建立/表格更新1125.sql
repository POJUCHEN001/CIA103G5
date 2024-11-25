use fixlife_cia103;

-- ========== DROP TABLES ========== --

ALTER TABLE fav_ft DROP FOREIGN KEY FK_favorite_ft_ft_id;
ALTER TABLE fav_ft DROP FOREIGN KEY FK_favorite_ft_mem_id;
DROP TABLE IF EXISTS fav_ft;


ALTER TABLE ft_service DROP FOREIGN KEY ft_service_ft_info_ft_id_fk;
ALTER TABLE ft_service DROP FOREIGN KEY ft_service_ft_skill_skill_no_fk;
DROP TABLE IF EXISTS ft_service;

ALTER TABLE reservation DROP FOREIGN KEY FK_reservation_fk_no;
ALTER TABLE reservation DROP FOREIGN KEY FK_reservation_ft_id;
ALTER TABLE reservation DROP FOREIGN KEY FK_reservation_mem_id;
DROP TABLE IF EXISTS ft_skill;


-- ========== CREATE mem_ft ========== --

DROP TABLE IF EXISTS mem_ft;
CREATE TABLE mem_ft (
    mem_id INT NOT NULL,
    ft_id INT NOT NULL,
    PRIMARY KEY (mem_id, ft_id),
    CONSTRAINT fk_mem_ft_mem_id FOREIGN KEY (mem_id) REFERENCES member_info(mem_id),
    CONSTRAINT fk_mem_ft_ft_id FOREIGN KEY (ft_id) REFERENCES ft_info(ft_id)
);

LOCK TABLES `mem_ft` WRITE;

INSERT INTO mem_ft (mem_id, ft_id) VALUES (33, 13);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (29, 14);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (25, 9);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (9, 16);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (13, 16);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (6, 16);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (4, 9);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (25, 8);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (22, 4);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (18, 4);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (3, 6);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (21, 7);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (18, 8);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (16, 14);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (24, 17);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (33, 15);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (31, 10);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (6, 17);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (18, 14);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (32, 16);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (22, 17);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (30, 13);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (24, 6);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (20, 13);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (26, 10);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (11, 11);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (29, 8);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (12, 11);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (26, 9);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (25, 6);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (14, 8);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (30, 10);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (13, 14);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (8, 16);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (3, 10);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (20, 10);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (15, 13);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (5, 17);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (35, 16);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (7, 1);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (30, 16);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (19, 10);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (22, 13);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (36, 6);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (26, 8);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (1, 9);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (1, 6);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (19, 11);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (30, 7);
INSERT INTO mem_ft (mem_id, ft_id) VALUES (18, 15);

UNLOCK TABLES;


-- ========== CREATE avaliable_time ========== --

DROP TABLE IF EXISTS avaliable_time;
CREATE TABLE avaliable_time (
    avaliable_time_no INT AUTO_INCREMENT PRIMARY KEY,
    ft_id INT NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    status ENUM('avaliable', 'booked', 'off') DEFAULT 'avaliable',
    CONSTRAINT fk_avaliable_time_ft_id FOREIGN KEY (ft_id) REFERENCES ft_info (ft_id)
);

LOCK TABLES `avaliable_time` WRITE;

INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (7, '2024-12-22 09:00:00', '2024-12-22 10:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (10, '2024-12-14 10:00:00', '2024-12-14 11:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (7, '2024-12-29 20:00:00', '2024-12-29 21:00:00', 'off');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (16, '2024-12-16 13:00:00', '2024-12-16 14:00:00', 'off');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (8, '2024-12-30 21:00:00', '2024-12-30 22:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (1, '2024-12-10 17:00:00', '2024-12-10 18:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (4, '2024-12-04 17:00:00', '2024-12-04 18:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (1, '2024-12-04 07:00:00', '2024-12-04 08:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (9, '2024-12-08 20:00:00', '2024-12-08 21:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (13, '2024-12-09 21:00:00', '2024-12-09 22:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (13, '2024-12-25 10:00:00', '2024-12-25 11:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (7, '2024-12-13 21:00:00', '2024-12-13 22:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (11, '2024-12-30 20:00:00', '2024-12-30 21:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (3, '2024-12-31 13:00:00', '2024-12-31 14:00:00', 'off');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (6, '2024-12-22 08:00:00', '2024-12-22 09:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (9, '2024-12-17 17:00:00', '2024-12-17 18:00:00', 'off');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (16, '2024-12-25 18:00:00', '2024-12-25 19:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (8, '2024-12-07 18:00:00', '2024-12-07 19:00:00', 'off');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (4, '2024-12-28 20:00:00', '2024-12-28 21:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (3, '2024-12-02 13:00:00', '2024-12-02 14:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (5, '2024-12-26 16:00:00', '2024-12-26 17:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (2, '2024-12-23 07:00:00', '2024-12-23 08:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (10, '2024-12-25 22:00:00', '2024-12-25 23:00:00', 'off');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (10, '2024-12-20 15:00:00', '2024-12-20 16:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (5, '2024-12-31 15:00:00', '2024-12-31 16:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (2, '2024-12-26 15:00:00', '2024-12-26 16:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (7, '2024-12-19 12:00:00', '2024-12-19 13:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (9, '2024-12-07 16:00:00', '2024-12-07 17:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (15, '2024-12-10 14:00:00', '2024-12-10 15:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (11, '2024-12-29 13:00:00', '2024-12-29 14:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (5, '2024-12-13 13:00:00', '2024-12-13 14:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (4, '2024-12-23 19:00:00', '2024-12-23 20:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (6, '2024-12-22 09:00:00', '2024-12-22 10:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (8, '2024-12-18 08:00:00', '2024-12-18 09:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (7, '2024-12-16 13:00:00', '2024-12-16 14:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (16, '2024-12-31 10:00:00', '2024-12-31 11:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (11, '2024-12-08 19:00:00', '2024-12-08 20:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (15, '2024-12-24 21:00:00', '2024-12-24 22:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (5, '2024-12-23 09:00:00', '2024-12-23 10:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (4, '2024-12-01 21:00:00', '2024-12-01 22:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (1, '2024-12-03 15:00:00', '2024-12-03 16:00:00', 'off');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (5, '2024-12-31 10:00:00', '2024-12-31 11:00:00', 'off');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (1, '2024-12-03 19:00:00', '2024-12-03 20:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (1, '2024-12-01 08:00:00', '2024-12-01 09:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (2, '2024-12-10 16:00:00', '2024-12-10 17:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (16, '2024-12-10 14:00:00', '2024-12-10 15:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (5, '2024-12-27 10:00:00', '2024-12-27 11:00:00', 'booked');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (5, '2024-12-23 20:00:00', '2024-12-23 21:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (6, '2024-12-07 08:00:00', '2024-12-07 09:00:00', 'avaliable');
INSERT INTO avaliable_time (ft_id, start_time, end_time, status) VALUES (6, '2024-12-25 10:00:00', '2024-12-25 11:00:00', 'booked');

UNLOCK TABLES;


-- ========== CREATE skill ========== --

DROP TABLE IF EXISTS skill;
CREATE TABLE skill (
    skill_no INT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(100) NOT NULL
);

LOCK TABLES `skill` WRITE;

INSERT INTO skill (skill_name) VALUES ('塔羅牌');
INSERT INTO skill (skill_name) VALUES ('命理學');
INSERT INTO skill (skill_name) VALUES ('水晶球占卜');
INSERT INTO skill (skill_name) VALUES ('星座分析');
INSERT INTO skill (skill_name) VALUES ('手相解讀');
INSERT INTO skill (skill_name) VALUES ('夢境解析');
INSERT INTO skill (skill_name) VALUES ('面相分析');
INSERT INTO skill (skill_name) VALUES ('紫微斗數');
INSERT INTO skill (skill_name) VALUES ('八字命盤');
INSERT INTO skill (skill_name) VALUES ('解籤');
INSERT INTO skill (skill_name) VALUES ('姓名學');
INSERT INTO skill (skill_name) VALUES ('占星術');
INSERT INTO skill (skill_name) VALUES ('靈擺占卜');
INSERT INTO skill (skill_name) VALUES ('易經分析');
INSERT INTO skill (skill_name) VALUES ('風水指引');
INSERT INTO skill (skill_name) VALUES ('測字');
INSERT INTO skill (skill_name) VALUES ('盧恩符文');
INSERT INTO skill (skill_name) VALUES ('運勢占卜');
INSERT INTO skill (skill_name) VALUES ('生命靈數');
INSERT INTO skill (skill_name) VALUES ('事業指引');
INSERT INTO skill (skill_name) VALUES ('開運補財庫');
INSERT INTO skill (skill_name) VALUES ('神諭卡');
INSERT INTO skill (skill_name) VALUES ('神祕學');
INSERT INTO skill (skill_name) VALUES ('催桃花斬桃花');
INSERT INTO skill (skill_name) VALUES ('流年測算');

UNLOCK TABLES;


-- ========== CREATE ft_skill ========== --

DROP TABLE IF EXISTS ft_skill;
CREATE TABLE ft_skill (
    ft_id INT NOT NULL,
    skill_no INT NOT NULL,
    PRIMARY KEY (ft_id, skill_no),
    CONSTRAINT fk_ft_skill_ft_id FOREIGN KEY (ft_id) REFERENCES ft_info(ft_id),
    CONSTRAINT fk_ft_skill_skill_no FOREIGN KEY (skill_no) REFERENCES skill(skill_no)
);

LOCK TABLES `ft_skill` WRITE;

INSERT INTO ft_skill (ft_id, skill_no) VALUES (9, 3);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (9, 7);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (9, 14);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (9, 24);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (16, 4);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (16, 7);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (16, 10);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (16, 19);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (14, 4);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (14, 6);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (14, 9);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (14, 10);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (14, 13);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (14, 14);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (14, 16);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (14, 19);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (13, 6);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (13, 11);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (13, 17);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (11, 13);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (11, 22);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (5, 2);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (5, 11);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (5, 25);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (8, 9);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (8, 12);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (8, 17);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (4, 13);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (3, 10);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (3, 13);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (3, 16);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (3, 19);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (15, 12);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (10, 5);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (7, 20);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (7, 3);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (15, 9);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (17, 5);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (2, 13);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (2, 22);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (1, 22);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (7, 25);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (10, 16);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (17, 13);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (1, 3);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (1, 17);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (6, 12);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (7, 24);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (2, 7);
INSERT INTO ft_skill (ft_id, skill_no) VALUES (2, 15);

UNLOCK TABLES;


-- ========== ALTER TABLE reservation ========== --

ALTER TABLE reservation 
ADD CONSTRAINT FK_reservation_fk_no FOREIGN KEY (skill_no) REFERENCES skill(skill_no);
ALTER TABLE reservation 
ADD CONSTRAINT FK_reservation_ft_id FOREIGN KEY (ft_id) REFERENCES ft_info(ft_id);
ALTER TABLE reservation 
ADD CONSTRAINT FK_reservation_mem_id FOREIGN KEY (mem_id) REFERENCES member_info(mem_id);
