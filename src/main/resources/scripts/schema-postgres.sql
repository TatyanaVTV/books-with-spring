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

DROP TABLE IF EXISTS books;

CREATE TABLE books
(
    book_id serial primary key,
    title character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    genre character varying(255) NOT NULL,
    pages bigint NOT NULL DEFAULT 0,
    deleted boolean NOT NULL DEFAULT false
);

ALTER TABLE IF EXISTS books
    OWNER to postgres;

CREATE TABLE author_books
(
    author_id bigint NOT NULL,
    book_id bigint NOT NULL,
    CONSTRAINT author_books_pkey PRIMARY KEY (author_id, book_id),
    CONSTRAINT fkkudd737sm6cxhjgn948yk599b FOREIGN KEY (author_id)
        REFERENCES public.authors (author_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkm8j8158lb50ea4juixpc78v13 FOREIGN KEY (book_id)
        REFERENCES public.books (book_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

ALTER TABLE IF EXISTS author_books
    OWNER to postgres;