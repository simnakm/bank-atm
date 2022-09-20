package com.example.atmbank

import java.io.Serializable

//class to set account details from arraylist using its primary constructor
public class Account : Serializable {
    public var accountNumber: String = ""
    public var password: String = ""
    public var name: String = ""
    public var balance: Int = 0

    constructor(accountNumber: String, password: String, name: String, balance: Int) {
        this.accountNumber = accountNumber
        this.password = password
        this.name = name
        this.balance = balance
    }


}



