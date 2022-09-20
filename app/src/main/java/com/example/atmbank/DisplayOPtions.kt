package com.example.atmbank

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.atmbank.databinding.ActivityDisplayOptionsBinding

class DisplayOPtions : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayOptionsBinding
    lateinit var arrayList: ArrayList<Account>
    var type: String = ""
    var value: Int = 0
    var accountNumber: String = ""
    var transaction: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        type = intent.getStringExtra("type").toString()
        accountNumber = intent.getStringExtra("account_number").toString()
        arrayList = ArrayList<Account>()
        arrayList =
            (intent.getSerializableExtra("list") as ArrayList<Account>?)!!  //fetching arraylist values

        showDetails()
        binding.depositButton.setOnClickListener {
            if (NetworkHelper.isNetworkConnected(this)) {

                transaction = "deposit"
                intent = Intent(this, Transactions::class.java)
                intent.putExtra("list", arrayList)
                intent.putExtra("account_number", accountNumber)
                intent.putExtra("value", transaction)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Sorry! There is no internet connection.Please try again later!",
                    Toast.LENGTH_LONG
                ).show()
            }


        }
        binding.withdrawButton.setOnClickListener {
            if (NetworkHelper.isNetworkConnected(this)) {

                transaction = "withdraw"
                intent = Intent(this, Transactions::class.java)
                intent.putExtra("list", arrayList)
                // intent.putExtra("list",)
                intent.putExtra("account_number", accountNumber)
                intent.putExtra("value", transaction)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Sorry! There is no internet connection.Please try again later!",
                    Toast.LENGTH_LONG
                ).show()
            }


        }
        binding.checkBalanceButton.setOnClickListener {

            if (NetworkHelper.isNetworkConnected(this)) {

                transaction = "balance"
                intent = Intent(this, Transactions::class.java)
                intent.putExtra("list", arrayList)
                intent.putExtra("account_number", accountNumber)
                intent.putExtra("value", transaction)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Sorry! There Is No Internet Connection.Please Try Again Later",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.exitButton.setOnClickListener {
            if (NetworkHelper.isNetworkConnected(this)) {


                val alertdialog: AlertDialog = AlertDialog.Builder(this).create()
                alertdialog.setTitle("Alert!")
                alertdialog.setMessage("Do you want to exit?")
                alertdialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes")
                { _: DialogInterface?, _: Int ->

                    exitDialog()
                }

                alertdialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE,
                    "No"
                ) { _: DialogInterface?, _: Int ->


                }


                alertdialog.show()
            } else {
                Toast.makeText(
                    this,
                    "Sorry! There Is No Internet Connection.Please Try Again Later",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }

    //function to display name and account type of account holder
    private fun showDetails() {
        if (arrayList != null) {
            for (i in 0 until arrayList.size) {
                if (accountNumber.equals(arrayList[i].accountNumber)) {
                    binding.nameText.text = arrayList[i].name
                }
            }
        }
        if (type.equals("current")) {
            binding.accountTypeText.text = "Current Account"
        } else if (type.equals("savings")) {
            binding.accountTypeText.text = "Savings Account"
        }
    }

    private fun exitDialog() {
        transaction = "exit"
        intent = Intent(this, Transactions::class.java)
        intent.putExtra("list", arrayList)
        intent.putExtra("account_number", accountNumber)
        intent.putExtra("value", transaction)
        startActivity(intent)
        finish()
    }
}