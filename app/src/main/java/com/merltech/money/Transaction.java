package com.merltech.money;

import java.io.Serializable;

public class Transaction implements Serializable {
    public int id;
    public String category;
    public int amount;
    public String description;

    public Transaction(String category, int amount, String description)
    {
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public Transaction()
    {
        this.category = "other";
        this.amount = 0;
        this.description = "";
    }
}
