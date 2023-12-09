package main.com.ngrewards.Models;

import java.io.Serializable;

public class SplitList implements Serializable {
    String Id, Date, IsPaid, Amount, Invoice;

    public SplitList(String id, String date, String isPaid, String amount, String invoice) {
        Id = id;
        Date = date;
        IsPaid = isPaid;
        Amount = amount;
        Invoice = invoice;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getIsPaid() {
        return IsPaid;
    }

    public void setIsPaid(String isPaid) {
        IsPaid = isPaid;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getInvoice() {
        return Invoice;
    }

    public void setInvoice(String invoice) {
        Invoice = invoice;
    }
}
