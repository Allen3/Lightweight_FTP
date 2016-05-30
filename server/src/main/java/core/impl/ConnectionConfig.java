package core.impl;

import core.StateContext;

/**
 * Created by allen on 4/23/16.
 */
public class ConnectionConfig {
    private StateContext stateContext;

    private boolean passiveMode;
    private boolean binaryMode;
    private String charsetName;

    public ConnectionConfig(StateContext stateContext, boolean binaryMode, String charsetName, boolean passiveMode) {
        this.stateContext = stateContext;
        this.binaryMode = binaryMode;
        this.charsetName = charsetName;
        this.passiveMode = passiveMode;
    }

    public boolean isBinaryMode() {
        return binaryMode;
    }

    public void setBinaryMode(boolean binaryMode) {
        this.binaryMode = binaryMode;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }
}
