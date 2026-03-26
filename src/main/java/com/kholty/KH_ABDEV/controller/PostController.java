package com.kholty.KH_ABDEV.controller;

import com.kholty.KH_ABDEV.entity.post.Post;
import com.kholty.KH_ABDEV.entity.user.User;
import com.kholty.KH_ABDEV.exception.PostException;
import com.kholty.KH_ABDEV.repository.PostRepository;
import com.kholty.KH_ABDEV.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class PostController{

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;


    // ==================== QUERIES ====================


    // Récupérer tous les posts

    @QueryMapping
    public List<Post> posts(){
        return postRepository.findAll();
    }

    //Récupérer un post par son ID
    @QueryMapping
    public Optional<Post> post(@Argument Long id){
    return postRepository.findById(id);
    }
    //Récupérer les posts d'un utilisateur

    @QueryMapping
    public List<Post> postByUser(@Argument Long userId) {
      return postRepository.findByAuthorId(userId);

    }

    // ==================== MUTATIONS ====================


    //Créer un post
    @MutationMapping
    @Transactional
    public Post createPost(@Argument String title, @Argument String content, @Argument Long authorId) throws PostException{
        User user=userRepository.findById(authorId).orElseThrow(()->new PostException("user not exist!!"));
       Post post =new Post(title,content,user);
       return postRepository.save(post);
    }

   // Mettre à jour un post
    @MutationMapping
    @Transactional
    public Post updatePost(@Argument Long id, @Argument  String title,@Argument  String content)throws PostException{
        Post post =postRepository.findById(id).orElseThrow(()->new PostException("post not exist!!") );

        if(title!=null){
            post.setTitle(title);
        }
        if(content!=null){
            post.setContent(content);
        }

        return postRepository.save(post);

    }


     // Supprimer un post
    @MutationMapping
     public boolean deletePost(@Argument Long id){
        if(!postRepository.existsById(id)){
           System.out.println("post not exist");
            return FALSE;
        }
        postRepository.deleteById(id);
        return TRUE;
    }









}