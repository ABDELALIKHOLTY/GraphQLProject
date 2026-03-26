package com.kholty.KH_ABDEV.controller;

import com.kholty.KH_ABDEV.entity.user.User;
import com.kholty.KH_ABDEV.exception.UserException;
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
public class UserController {
    @Autowired
    private UserRepository userRepository;


    // ==================== QUERIES ====================
@QueryMapping
public List<User> users(){
    return userRepository.findAll();
}
@QueryMapping
    public Optional<User> user(@Argument Long id){

    return userRepository.findById(id);
}

    // ==================== MUTATIONS ====================
@MutationMapping
@Transactional
    public  User createUser(@Argument String name, @Argument String email)throws UserException{

    if(userRepository.existsByEmail(email)){
        throw new UserException("user already exist!!");

    }

    User user= new User();
    user.setName(name);
    user.setEmail(email);
    return userRepository.save(user);

}

@MutationMapping
@Transactional
    public User updateUser(@Argument Long id, @Argument String name, @Argument String email) throws UserException{
    User user = userRepository.findById(id).orElseThrow(() -> new UserException("User not exist"));

    if(name!=null){
        user.setName(name);
    }else{
        throw new UserException("name should  be not null!");
    }

    if(email!=null && !email.equals(user.getEmail())){
        if(userRepository.existsByEmail(email)){
            throw new UserException("email already exist!!");
        }

        user.setEmail(email);
    }

return userRepository.save(user);
}

@MutationMapping
    public boolean deleteUser(@Argument Long id){
    if(!userRepository.existsById(id)){
        System.out.println("User not exist");
        return FALSE;
    }
    userRepository.deleteById(id);
    System.out.println("user are deleted !!");
    return TRUE;
}

}
