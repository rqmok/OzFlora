/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fit5042.ozflora.controllers;

import com.fit5042.ozflora.auth.AuthenticationUtils;
import com.fit5042.ozflora.auth.entities.User;
import com.fit5042.ozflora.mbeans.UserManagedBean;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;

/**
 *
 * @author zeeshan
 */
@Named(value = "editUserController")
@ViewScoped
public class EditUserController implements Serializable {

    private static final Logger logger = Logger.getLogger(EditUserController.class.getName());

    @ManagedProperty(value = "#{ userManagedBean }")
    private final UserManagedBean userManagedBean;

    private User user;

    private String password;
    private String confirmPassword;

    /**
     * Creates a new instance of EditUserController
     */
    public EditUserController() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        this.userManagedBean = (UserManagedBean) FacesContext.getCurrentInstance().getApplication()
                .getELResolver().getValue(elContext, null, "userManagedBean");

        String userId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("userId");
        this.user = this.userManagedBean.getUser(userId);
    }

    private void addFacesErrorMessage(FacesContext context, String clientId, String errorMessage) {
        FacesMessage message = new FacesMessage(errorMessage);
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage(clientId, message);
        context.renderResponse();
    }

    public void validateForm(ComponentSystemEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        UIComponent components = event.getComponent();

        // Get the password.
        UIInput passwordInput = (UIInput) components.findComponent("password");
        String password = passwordInput.getLocalValue() == null ? "" : passwordInput.getLocalValue().toString();
        String passwordId = passwordInput.getClientId();

        // Get the confirm password.
        UIInput confirmPasswordInput = (UIInput) components.findComponent("confirm_password");
        String confirmPassword = confirmPasswordInput.getLocalValue() == null ? "" : confirmPasswordInput.getLocalValue().toString();
        String confirmPasswordId = confirmPasswordInput.getClientId();
        
        if (!password.isEmpty() && confirmPassword.isEmpty()) {
            this.addFacesErrorMessage(context, confirmPasswordId, "Please confirm your password");
        } else if (password.isEmpty() && !confirmPassword.isEmpty()) {
            this.addFacesErrorMessage(context, passwordId, "Please enter a password");
        }

        // Return if unable to retrieve passwords.
        if (!password.isEmpty() && !confirmPassword.isEmpty()) {
            if (!confirmPassword.equals(password)) {
                this.addFacesErrorMessage(context, passwordId, "Password and Confirm Password do not match");
            }
        }
    }

    public String saveUser() {
        if (user != null) {
            if (password != null && !password.isEmpty() && password.length() > 0) {
                try {
                    this.user.setPassword(AuthenticationUtils.encodeSHA256(password));
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                    logger.log(Level.SEVERE, null, e);
                }
            }

            this.userManagedBean.saveUser(user);
        }
        return "manageusers?redirect-faces=true";
    }

    public String cancel() {
        return "manageusers?redirect-faces=true";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}