package com.dut.sweetchatapp.constant;

import java.io.File;

public class DefaultPath {

    public static final String ROOT_FOLDER = System.getProperty("user.home");

    public static final String ACCOUNT_PATH = "api/accounts";

    public static final String AUTHENTICATION_PATH = "api/auth";

    public static final String ACTIVATION_PATH = "api/public/active-email";

    public static final String FORGOT_PASSWORD_PATH = "api/public/forgot-password";

    public static final String RESET_PASSWORD_PATH = "api/public/reset-password";

    public static final String SAVE_PASSWORD_PATH = "api/public/save-password";

    public static final String PUBLIC_IMAGE_PATH = "api/public/images";

    public static String createRootFolder(String storagePath) {
        return ROOT_FOLDER + storagePath + "/images/";
    }

    public static String createAvatarImageFolder(String storagePath) {
        return createRootFolder(storagePath) + File.separator + "avatars";
    }

}
