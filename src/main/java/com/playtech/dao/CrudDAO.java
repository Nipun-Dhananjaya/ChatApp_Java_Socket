package com.playtech.dao;

import com.playtech.entity.User;

public interface CrudDAO extends SuperDAO{
    public boolean search(User user);
}
