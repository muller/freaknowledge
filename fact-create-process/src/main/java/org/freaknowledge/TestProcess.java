package org.freaknowledge;

public class TestProcess extends Process {

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
