package org.cloudfoundry.samples.music.domain;

public class SessionInfo {

    public int getModifyCount() {
        return modifyCount;
    }

    public int incModifyCount() {
        return ++modifyCount;
    }

    private int modifyCount = 0;

}
