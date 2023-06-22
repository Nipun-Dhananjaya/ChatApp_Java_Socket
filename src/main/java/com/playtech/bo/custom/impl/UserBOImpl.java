package com.playtech.bo.custom.impl;

import com.playtech.bo.BOFactory;
import com.playtech.bo.SuperBO;
import com.playtech.bo.custom.UserBO;
import com.playtech.dao.DAOFactory;
import com.playtech.dao.custom.UserDAO;
import com.playtech.dto.UserDTO;
import com.playtech.entity.User;

public class UserBOImpl implements UserBO {
    UserDAO userDAO= (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.USER);
    @Override
    public boolean searchUser(UserDTO userDTO) {
        return userDAO.search(new User(userDTO.getUserName()));
    }
}
