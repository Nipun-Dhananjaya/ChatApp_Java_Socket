package com.playtech.bo;
import com.playtech.bo.custom.impl.*;
public class BOFactory {
    private BOFactory(){}
    private static BOFactory BoFactory;

    public static BOFactory getBoFactory() {
        return (BoFactory==null)?BoFactory=new BOFactory():BoFactory;
    }
    public enum BOTypes{
        USER
    }
    public static SuperBO getBO(BOTypes boTypes){
        switch (boTypes){
            case USER:return new UserBOImpl();
            default: return null;
        }
    }
}
