package dao;

import model.User;

import java.util.List;

/**
 * Created by arsalan.khalid on 9/20/2016.
 */
public interface UserDao {
    User findByName(String name);

    List<User> findAll();
}
