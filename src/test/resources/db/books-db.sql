DROP TABLE IF EXISTS author_books;
DROP TABLE IF EXISTS books;

CREATE TABLE books
(
    book_id serial primary key,
    title character varying(255) NOT NULL,
--    author character varying(255) NOT NULL,
    genre character varying(255) NOT NULL,
    pages bigint NOT NULL DEFAULT 0,
    deleted boolean NOT NULL DEFAULT false
);

ALTER TABLE IF EXISTS books
    OWNER to postgres;

INSERT INTO books(book_id, title, genre, pages, deleted)
	VALUES (1, 'Spring в действии', 'Учебная литература', 543, false);

INSERT INTO books(book_id, title, genre, pages, deleted)
	VALUES (2, 'Head First. Паттерны проектирования', 'Учебная литература', 640, false);

INSERT INTO books(book_id, title, genre, pages, deleted)
	VALUES (3, 'Философия Java', 'Учебная литература', 1168, false);

INSERT INTO books(book_id, title, genre, pages, deleted)
	VALUES (4, 'Создание микросервисов', 'Учебная литература', 624, false);

INSERT INTO books(book_id, title, genre, pages, deleted)
	VALUES (5, 'Удаленная книга', 'Жанр неизвестен', 100, true);

INSERT INTO books(book_id, title, genre, pages, deleted)
	VALUES (6, 'От монолита к микросервисам', 'Учебная литература', 272, false);

SELECT setval('books_book_id_seq', 7);