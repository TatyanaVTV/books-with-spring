package ru.vtvhw.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "authors")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SoftDelete
public class Author {

    @Id
    @SequenceGenerator(name = "authorIdSeq", sequenceName = "authors_author_id_seq", initialValue = 6, allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "authorIdSeq")
    @Column(name = "author_id")
    private long id;

    @Column(name = "author_name", nullable = false)
    private String name;

    @Column(nullable = false, insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private boolean deleted;

    public Author(String authorName) {
        this.name = authorName;
    }

    @ManyToMany(mappedBy = "authors",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
            targetEntity = Book.class)
//    @JoinTable(name = "author_books",
//            joinColumns = @JoinColumn(name = "author_id", referencedColumnName = "author_id", nullable = false),
//            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "book_id"))//,
////            uniqueConstraints = { @UniqueConstraint(name = "UniqueAuthorAndBook", columnNames = { "author_id", "book_id" } )})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Book> books = new HashSet<>();

    public Author(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    @PreRemove
    public void removeBookAssociations() {
        books.forEach(book -> book.removeAuthor(this));
    }
}