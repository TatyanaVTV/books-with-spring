DROP TABLE IF EXISTS books;

CREATE TABLE books
(
    id serial primary key,
    title character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    genre character varying(255) NOT NULL,
    pages bigint NOT NULL DEFAULT 0,
    deleted boolean NOT NULL DEFAULT false
);

ALTER TABLE IF EXISTS books
    OWNER to postgres;

INSERT INTO books(id, title, author, genre, pages, deleted)
	VALUES (1, 'Spring в действии', 'Крейг Уоллс', 'Учебная литература', 543, false);

INSERT INTO books(id, title, author, genre, pages, deleted)
	VALUES (2, 'Head First. Паттерны проектирования', 'Эрик Фримен, Элизабет Робсон', 'Учебная литература', 640, false);

INSERT INTO books(id, title, author, genre, pages, deleted)
	VALUES (3, 'Философия Java', 'Брюс Эккель', 'Учебная литература', 1168, false);

INSERT INTO books(id, title, author, genre, pages, deleted)
	VALUES (4, 'Создание микросервисов', 'Сэм Ньюмен', 'Учебная литература', 624, false);

INSERT INTO books(id, title, author, genre, pages, deleted)
	VALUES (5, 'Удаленная книга', 'Автор неизвестен', 'Жанр неизвестен', 100, true);

SELECT setval('books_id_seq', 6);