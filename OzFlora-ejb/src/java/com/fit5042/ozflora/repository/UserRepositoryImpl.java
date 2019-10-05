/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fit5042.ozflora.repository;

import com.fit5042.ozflora.auth.AuthenticationUtils;
import com.fit5042.ozflora.auth.entities.User;
import com.fit5042.ozflora.auth.entities.UserGroup;
import com.fit5042.ozflora.repository.entities.Plant;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Zeeshan
 */
@Stateless
@LocalBean
public class UserRepositoryImpl implements UserRepository {
    
    @PersistenceContext(unitName = "OzFlora-ejbPU")
    private EntityManager entityManager;
    
    @Override
    public User createUser(User user) throws Exception {
        try {
            user.setPassword(AuthenticationUtils.encodeSHA256(user.getPassword()));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage());
        }
        
        UserGroup userGroup = new UserGroup();
        userGroup.setEmail(user.getEmail());
        userGroup.setGroupName(UserGroup.USERS_GROUP);
        
        entityManager.persist(user);
        entityManager.persist(userGroup);
        
        return user;
    }
    
    @Override
    public User findUserById(String id) throws Exception {
        return entityManager.find(User.class, id);
    }

    @Override
    public void savePlantToUser(User user, Plant plant) throws Exception {
        user.addPlant(plant);
        entityManager.merge(user);
    }

    @Override
    public void removePlantFromUser(User user, Plant plant) throws Exception {
        user.removePlant(plant);
        entityManager.merge(user);
    }
    
}