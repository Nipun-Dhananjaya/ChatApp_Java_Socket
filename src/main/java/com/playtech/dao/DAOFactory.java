package com.playtech.dao;

import com.playtech.dao.custom.impl.UserDAOImpl;

public class DAOFactory {
    private DAOFactory(){}
    private static DAOFactory daoFactory;

    public static DAOFactory getDaoFactory() {
        return (daoFactory==null)?daoFactory=new DAOFactory():daoFactory;
    }
    public enum DAOTypes{
        USER
    }
    public static SuperDAO getDAO(DAOTypes daoTypes){
        switch (daoTypes){
            case USER:return new UserDAOImpl();
            default: return null;
        }
    }
}
