package com.codelipi.ecomm.startup;

/**
 * Created by Manish on 8/21/2017.
 */

public class Contact {

    public static abstract class NewUserInfo
    {
        public static final String User_NAME="user_firstname";
        public static final String User_LASTNAME="user_lastname";
        public static final String User_MOB="user_mob";
        public static final String User_EMAIL="user_email";
        public static final String TABLE_NAME="user_info";
        public static final String User_Pass="user_pass";
    }
    public static abstract class Address
    {
        public static final String User_EMAIL="user_email";
        public static final String User_Address="user_address";
        public static final String User_Street="user_street";
        public static final String User_City="user_city";
        public static final String TABLE_NAME="user_address";
        public static final String User_State="user_state";
        public static final String User_Pincode="user_pincode";
    }

}
