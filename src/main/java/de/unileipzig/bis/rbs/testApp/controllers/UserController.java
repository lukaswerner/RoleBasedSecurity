package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.model.Role;
import de.unileipzig.bis.rbs.testApp.model.User;
import de.unileipzig.bis.rbs.testApp.service.RoleRepository;
import de.unileipzig.bis.rbs.testApp.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The user controller to manage users in this application.
 *
 * @author Lukas Werner
 */
@Controller
@RequestMapping("/manage/user")
public class UserController extends AbstractController {

    /**
     * The user repository to persist changes
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * The role repository
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get all users
     *
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String users(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user/all-users";
    };

    /**
     * Get user by id
     *
     * @param userid the id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/{userid}", method = RequestMethod.GET)
    public String user(@PathVariable String userid, Model model) {
        User user = userRepository.findOne(Long.valueOf(userid));
        model.addAttribute("user", user);
        return "user/user";
    }

    /**
     * Create new user (show mask)
     *
     * @param model the ui model
     * @return the user creation mask
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        Iterable<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "user/create";
    }

    /**
     * Create new user (action)
     *
     * @param username the username
     * @param password the password
     * @param name the name
     * @param roleIds the selected roles
     * @return the view (redirect)
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String doCreate(@RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "roles[]", required = false) Long[] roleIds) {
        User user = new User(username, password, name);
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                roles.add(roleRepository.findOne(roleId));
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
        return "redirect:/manage/user";
    }

    /**
     * Edit existing user (show mask)
     *
     * @param userid the user id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/edit/{userid}", method = RequestMethod.GET)
    public String edit(@PathVariable String userid, Model model) {
        User user = userRepository.findOne(Long.valueOf(userid));
        Iterable<Role> roles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "user/edit";
    }

    /**
     * Edit existing user (action)
     *
     * @param userid the user id
     * @param username the new username
     * @param password the new password
     * @param name the new name
     * @param roleIds the selected roles
     * @return the view (redirect)
     */
    @RequestMapping(value = "/edit/{userid}", method = RequestMethod.POST)
    public String doEdit(@PathVariable String userid,
                         @RequestParam(value = "username") String username,
                         @RequestParam(value = "password") String password,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "roles[]", required = false) Long[] roleIds) {
        User user = userRepository.findOne(Long.valueOf(userid));
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                roles.add(roleRepository.findOne(roleId));
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
        return "redirect:/manage/user/" + userid;
    }

    /**
     * Delete existing user
     *
     * @param userid the user id
     * @return the view (redirect)
     */
    @RequestMapping(value = "/delete/{userid}", method = RequestMethod.GET)
    public String delete(@PathVariable String userid) {
        userRepository.delete(Long.valueOf(userid));
        return "redirect:/manage/user";
    }

}