DROP SCHEMA IF EXISTS stackoverflow_db;
CREATE SCHEMA stackoverflow_db;
USE stackoverflow_db;
CREATE TABLE `user` (
                        `id` integer PRIMARY KEY not null AUTO_INCREMENT ,
                        `username` varchar(255),
                        `email` varchar(255),
                        `password` varchar(255),
                        `about` text,
                        `is_moderator` bool,
                        `reputation` double,
                        `is_banned` bool,
                        `img` varchar(255),
                        `creation_date` timestamp
);

CREATE TABLE `post` (
                        `id` integer PRIMARY KEY not null AUTO_INCREMENT ,
                        `created_by_user_id` integer NOT NULL,
                        `parent_question_id` integer,
                        `post_type_id` integer NOT NULL,
                        `post_title_q` varchar(255),
                        `post_content` text,
                        `posted_date` timestamp,
                        `img` varchar(255),
                        `status_q` varchar(255),
                        `accepted_answer_id` integer
);

CREATE TABLE `comment` (
                           `id` integer PRIMARY KEY not null AUTO_INCREMENT ,
                           `post_id` integer NOT NULL,
                           `created_by_user_id` integer NOT NULL,
                           `comment_content` text,
                           `posted_date` timestamp
);

CREATE TABLE `tag` (
                       `id` integer PRIMARY KEY not null AUTO_INCREMENT,
                       `tag_name` varchar(255),
                       `tag_description` text,
                       `created_by_user_id` integer NOT NULL
);

CREATE TABLE `post_tag` (
                            `id` integer PRIMARY KEY not null AUTO_INCREMENT ,
                            `post_id` integer NOT NULL,
                            `tag_id` integer NOT NULL
);

CREATE TABLE `post_type` (
                             `id` integer PRIMARY KEY not null,
                             `type_name` varchar(255)
);

CREATE TABLE `vote` (
                        `id` integer PRIMARY KEY not null AUTO_INCREMENT ,
                        `post_id` integer NOT NULL,
                        `vote_type_id` integer NOT NULL,
                        `voted_by_user_id` integer NOT NULL
);

CREATE TABLE `vote_type` (
                             `id` integer PRIMARY KEY not null AUTO_INCREMENT ,
                             `vote_type` varchar(255)
);

ALTER TABLE `post` ADD FOREIGN KEY (`created_by_user_id`) REFERENCES `user` (`id`);

ALTER TABLE `post` ADD FOREIGN KEY (`parent_question_id`) REFERENCES `post` (`id`);

ALTER TABLE `post` ADD FOREIGN KEY (`post_type_id`) REFERENCES `post_type` (`id`);

#ALTER TABLE `post` ADD FOREIGN KEY (`id`) REFERENCES `post` (`accepted_answer_id`);

ALTER TABLE `comment` ADD FOREIGN KEY (`created_by_user_id`) REFERENCES `user` (`id`);

ALTER TABLE `comment` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

ALTER TABLE `post_tag` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

ALTER TABLE `post_tag` ADD FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`);

ALTER TABLE `vote` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

ALTER TABLE `vote` ADD FOREIGN KEY (`vote_type_id`) REFERENCES `vote_type` (`id`);

ALTER TABLE `vote` ADD FOREIGN KEY (`voted_by_user_id`) REFERENCES `user` (`id`);

ALTER TABLE `tag` ADD FOREIGN KEY (`created_by_user_id`) REFERENCES `user` (`id`);

insert into post_type values
    (1, 'question');

insert into post_type values
    (2, 'answer');

insert into vote_type values
                          (1, 'upvote');
insert into vote_type values
                          (2, 'downvote');


DELIMITER $$

CREATE TRIGGER before_user_delete
    BEFORE DELETE ON `user`
    FOR EACH ROW
BEGIN
    -- Setăm toate tag-urile create de utilizator la NULL
    UPDATE `tag`
    SET `created_by_user_id` = -1
    WHERE `created_by_user_id` = OLD.id;

    -- Setăm toate postările create de utilizator la NULL
    UPDATE `post`
    SET `created_by_user_id` = -1
    WHERE `created_by_user_id` = OLD.id;

    -- Setăm toate comentariile create de utilizator la NULL
    UPDATE `comment`
    SET `created_by_user_id` = -1
    WHERE `created_by_user_id` = OLD.id;

    -- Setăm voturile date de utilizator la NULL
    UPDATE `vote`
    SET `voted_by_user_id` = -1
    WHERE `voted_by_user_id` = OLD.id;
END $$

DELIMITER ;


