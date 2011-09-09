package org.freaknowledge.trip;

import java.util.Collection;

public class ExpenseReport {

    private Collection<ExpenseReceipt> receipts;

    public Collection<ExpenseReceipt> getReceipts() {

        return receipts;
    }

    public void setReceipts(Collection<ExpenseReceipt> receipts) {

        this.receipts = receipts;
    }
}
