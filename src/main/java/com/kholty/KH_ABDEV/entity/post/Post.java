package com.kholty.KH_ABDEV.entity.post;

import com.kholty.KH_ABDEV.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    // Relation Many-to-One avec User (plusieurs posts peuvent avoir le même auteur)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Constructeur pour la création
    public Post(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
    

}


