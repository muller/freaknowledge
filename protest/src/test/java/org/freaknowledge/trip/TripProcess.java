package org.freaknowledge.trip;

import org.freaknowledge.Process;

public class TripProcess extends Process {

    private static final long serialVersionUID = 1L;

    private String message;

    private Boolean important;

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public Boolean getImportant() {

        return important;
    }

    public void setImportant(Boolean important) {

        this.important = important;
    }
}
