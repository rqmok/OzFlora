/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fit5042.ozflora.repository;

import com.fit5042.ozflora.auth.entities.User;
import com.fit5042.ozflora.repository.entities.Plant;
import javax.ejb.Remote;

/**
 *
 * @author Zeeshan
 */
@Remote
public interface UserRepository {

    /**
     * Create a given {@link User} in the database.
     *
     * @param user The {@link User} to create.
     * @return The created {@link User}.
     * @throws java.lang.Exception
     */
    User createUser(User user) throws Exception;

    /**
     * Find a {@link User} given their ID.
     *
     * @param id The ID of the {@link User} to find.
     * @return The found {@link User}, and null if not found.
     * @throws java.lang.Exception
     */
    User findUserById(String id) throws Exception;
    
    /**
     * Save a given {@link Plant} to the given {@link User}.
     * 
     * @param user The {@link User} to add the {@link Plant} to.
     * @param plant The {@link Plant} to save.
     * @throws java.lang.Exception
     */
    void savePlantToUser(User user, Plant plant) throws Exception;
    
    /**
     * Remove a given {@link Plant} from the given {@link User}.
     * 
     * @param user The {@link User} to remove the {@link Plant} from.
     * @param plant The {@link Plant} to remove.
     * @throws java.lang.Exception
     */
    void removePlantFromUser(User user, Plant plant) throws Exception;
}
