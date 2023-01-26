package com.lay.fastipcdemo.binder;

import androidx.annotation.NonNull;

import com.lay.fastipc.annatation.ServiceId;

/**
 * author: qinlei
 * create by: 2023/1/26 20:24
 * description:
 */
@ServiceId(name = "UserService")
public class UserManager implements IUserManager {

    private static UserManager userManager = new UserManager();

    public static UserManager getDefault() {
        return userManager;
    }

    private User user;


    @Override
    public void setUserInfo(@NonNull User user) {
        this.user = user;
    }

    @NonNull
    @Override
    public User getUserInfo() {
        return user;
    }
}
