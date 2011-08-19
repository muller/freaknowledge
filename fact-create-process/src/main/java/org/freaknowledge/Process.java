package org.freaknowledge;

public abstract class Process extends Fact {

    private static final long serialVersionUID = -6212132833116045895L;

    private long processInstanceId;

    public long getProcessInstanceId() {

        return processInstanceId;
    }

    public void setProcessInstanceId(long processInstanceId) {

        this.processInstanceId = processInstanceId;
    }
}
