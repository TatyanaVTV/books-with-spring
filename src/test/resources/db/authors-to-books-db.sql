DROP TABLE IF EXISTS author_books;

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

INSERT INTO author_books(author_id, book_id) VALUES (1, 1);
INSERT INTO author_books(author_id, book_id) VALUES (2, 2);
INSERT INTO author_books(author_id, book_id) VALUES (3, 2);
INSERT INTO author_books(author_id, book_id) VALUES (4, 3);
INSERT INTO author_books(author_id, book_id) VALUES (5, 4);
INSERT INTO author_books(author_id, book_id) VALUES (5, 6);
INSERT INTO author_books(author_id, book_id) VALUES (6, 5);