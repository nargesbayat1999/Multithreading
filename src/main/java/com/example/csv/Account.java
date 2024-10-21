package com.example.csv;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private int accountType;

    @Column(nullable = false)
    private Long customerID;

    @Column(nullable = false)
    private double accountLimit;

    @Column(nullable = false)
    private LocalDate openDate;

    @Column(nullable = false)
    private String balance;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public double getAccountLimit() {
        return accountLimit;
    }

    public void setAccountLimit(double accountLimit) {
        this.accountLimit = accountLimit;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }


    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) throws Exception {
        this.balance = EncryptionUtil.encrypt(String.valueOf(balance));
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) throws Exception {
        this.accountNumber = EncryptionUtil.encrypt(accountNumber);
    }

    public double getDecryptedBalance() throws Exception {
        return Double.parseDouble(EncryptionUtil.decrypt(this.balance));
    }

}
