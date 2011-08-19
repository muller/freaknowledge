package org.freaknowledge;

import java.io.Serializable;

public abstract class Fact implements Serializable {

    private static final long serialVersionUID = 7859104395882440128L;

    private String factHandle;

    public String getFactHandle() {

        return factHandle;
    }

    public void setFactHandle(String factHandle) {

        this.factHandle = factHandle;
    }
}
