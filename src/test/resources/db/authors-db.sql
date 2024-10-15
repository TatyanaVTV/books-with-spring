DROP TABLE IF EXISTS author_books;
DROP TABLE IF EXISTS authors;

CREATE TABLE authors
(
    author_id serial primary key,
    author_name character varying(255) NOT NULL,
    deleted boolean NOT NULL DEFAULT false
);

ALTER TABLE IF EXISTS authors
    OWNER to postgres;

INSERT INTO authors(author_id, author_name, deleted)
	VALUES (1, 'Крейг Уоллс', false);

INSERT INTO authors(author_id, author_name, deleted)
	VALUES (2, 'Эрик Фримен', false);

INSERT INTO authors(author_id, author_name, deleted)
	VALUES (3, 'Элизабет Робсон', false);

INSERT INTO authors(author_id, author_name, deleted)
	VALUES (4, 'Брюс Эккель', false);

INSERT INTO authors(author_id, author_name, deleted)
	VALUES (5, 'Сэм Ньюмен', false);

INSERT INTO authors(author_id, author_name, deleted)
	VALUES (6, 'Автор неизвестен', true);

SELECT setval('authors_author_id_seq', 7);