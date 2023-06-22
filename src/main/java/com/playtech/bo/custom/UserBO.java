package com.playtech.bo.custom;

import com.playtech.bo.SuperBO;
import com.playtech.dto.UserDTO;

public interface UserBO extends SuperBO {
    public boolean searchUser(UserDTO userDTO);
}
