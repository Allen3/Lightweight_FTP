package core.impl;

import core.StateContext;

/**
 * Created by Allen on 4/30/2016.
 */
public class UserState {
    private StateContext stateContext;
    private String username;
    private String password;
    private String currentPath;

    public UserState(StateContext stateContext) {
        this.stateContext = stateContext;
        currentPath = System.getProperty("user.home");
    }

    public boolean isAuthenticated() {
        //TODO
        // authentication codes.
        return true;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
