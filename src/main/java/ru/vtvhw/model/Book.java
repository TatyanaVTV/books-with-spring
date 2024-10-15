package ru.vtvhw.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SoftDelete
public class Book {

    @Id
    @SequenceGenerator(name = "bookIdSeq", sequenceName = "books_book_id_seq", initialValue = 6, allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "bookIdSeq")
    @Column(name = "book_id")
    private long id;

    @Column(nullable = false)
    private String title = "Unknown book";

//    @Column
//    private String author = "Unknown author";

    @Column
    private String genre = "Unknown genre";

    @Column(name = "pages")
    private int numberOfPages;

    @Column(nullable = false, insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private boolean deleted;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
            targetEntity = Author.class)
    @JoinTable(name = "author_books",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "author_id"))//,
//            uniqueConstraints = { @UniqueConstraint(name = "UniqueAuthorAndBook", columnNames = { "author_id", "book_id" } )})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Author> authors = new HashSet<>();

    public Book(String title, String genre, int numberOfPages) {
        this.title = title;
//        this.author = author;
        this.genre = genre;
        this.numberOfPages = numberOfPages;
        this.deleted = false;
    }

    public Book(long id, String title, String genre, int numberOfPages) {
        this(title, genre, numberOfPages);
        this.setId(id);
    }

    public Book(long id, String title, String genre, int numberOfPages, boolean isDeleted) {
        this(id, title, genre, numberOfPages);
        this.setDeleted(isDeleted);
    }


    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
    }

    @PreRemove
    public void removeBookAssociations() {
        authors.forEach(author -> author.removeBook(this));
    }
}