package com.kholty.KH_ABDEV.repository;

import com.kholty.KH_ABDEV.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    // Récupérer tous les posts d'un utilisateur par son ID
    List<Post> findByAuthorId(Long authorId);

    // Compter le nombre de posts d'un utilisateur
    long countByAuthorId(Long authorId);



}
