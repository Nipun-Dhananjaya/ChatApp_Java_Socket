package com.playtech.dao.custom.impl;

import com.playtech.dao.CrudDAO;
import com.playtech.dao.custom.UserDAO;
import com.playtech.entity.User;
import com.playtech.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
    @Override
    public boolean search(User user) {
        ResultSet result = null;
        User isUser;
        try {
            result = CrudUtil.execute("SELECT user_name FROM user WHERE user_name=?;", user.getUserName());
            if (result.next()) {
                isUser = new User(result.getString(1));
            }else {
                return false;
            }
            if (user.getUserName().equals(isUser.getUserName())) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
