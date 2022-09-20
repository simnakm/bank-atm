package com.example.atmbank

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.atmbank.databinding.ActivityTransactionsBinding

class Transactions : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionsBinding
    lateinit var arrayList: ArrayList<Account>
    var accountNumber: String = ""
    var balance: Int = 0
    var value: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        value = intent.getStringExtra("value").toString()
        arrayList = (intent.getSerializableExtra("list") as ArrayList<Account>?)!!
        accountNumber = intent.getStringExtra("account_number").toString()
        if (NetworkHelper.isNetworkConnected(this)) {


            //checking for user selection
            when (value) {
                "deposit" -> {
                    depositAmount()
                }
                "withdraw" -> {
                    withdrawAmount()
                }
                "balance" -> {
                    balanceEnquiry()
                }
                "exit" -> {
                    exitTransaction()
                }
            }
        } else {
            Toast.makeText(
                this,
                "Sorry! There Is No Internet Connection.Please Try Again Later",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //function to display message
    private fun exitTransaction() {
        binding.depositLayout.visibility = View.GONE
        binding.withdrawLayout.visibility = View.GONE
        binding.thanksLayout.visibility = View.VISIBLE
    }

    //function to display balance amount in arraylist
    private fun balanceEnquiry() {
        binding.depositLayout.visibility = View.VISIBLE
        binding.withdrawLayout.visibility = View.GONE
        binding.depositAmtView.visibility = View.GONE
        binding.depositAmtText.visibility = View.GONE
        binding.creditMsgView.visibility = View.GONE
        binding.submitButton.visibility = View.GONE
        binding.thanksLayout.visibility = View.GONE
        if (arrayList != null) {
            for (i in 0 until arrayList.size) {
                if (accountNumber == arrayList[i].accountNumber) {
                    binding.depositBalanceText.text = arrayList[i].balance.toString()
                }
            }
        }
    }

    //function to read withdraw amount and update arraylist
    private fun withdrawAmount() {
        binding.depositLayout.visibility = View.GONE
        binding.thanksLayout.visibility = View.GONE
        binding.withdrawBalanceView.visibility = View.GONE
        binding.withdrawBalanceText.visibility = View.GONE
        binding.withdrawSubmitButton.setOnClickListener {
            withdrawSubmit()
            binding.withdrawAmtText.setOnEditorActionListener { _, i, _ ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    withdrawSubmit()//function call to to update arraylist
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun withdrawSubmit() {
        var withdrawAmount: Int = binding.withdrawAmtText.text.toString().toInt()
        if (withdrawAmount % 100 != 0) {
            binding.debitMsgView.text = "Amount Must Be Multiple Of 100"
        } else {

            if (arrayList != null) {
                for (i in 0 until arrayList.size) {
                    if (accountNumber == arrayList[i].accountNumber) {
                        arrayList[i].balance -= binding.withdrawAmtText.text.toString().toInt()
                        balance = arrayList[i].balance
                    }
                }
            }
            binding.debitMsgView.visibility = View.VISIBLE
            binding.debitMsgView.text = "Amount debited successfully "
            binding.withdrawBalanceView.visibility = View.VISIBLE
            binding.withdrawBalanceText.visibility = View.VISIBLE
            binding.withdrawBalanceText.text = balance.toString()
        }
    }

    //function to read deposit amount and update arraylist
    private fun depositAmount() {
        binding.withdrawLayout.visibility = View.GONE
        binding.thanksLayout.visibility = View.GONE
        binding.depositBalanceView.visibility = View.GONE
        binding.depositBalanceText.visibility = View.GONE
        binding.submitButton.setOnClickListener {
            depositSubmit()//function call to update arraylist
            binding.depositAmtText.setOnEditorActionListener { _, i, _ ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    depositAmount()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun depositSubmit() {
        var depositAmount: Int = binding.depositAmtText.text.toString().toInt()
        if (depositAmount % 100 != 0) {
            binding.creditMsgView.text = "Amount Must Be Multiple Of 100"
        } else {
            if (arrayList != null) {
                for (i in 0 until arrayList.size) {
                    if (accountNumber == arrayList[i].accountNumber) {
                        arrayList[i].balance += binding.depositAmtText.text.toString()
                            .toInt()
                        balance = arrayList[i].balance
                    }
                }
            }
            binding.creditMsgView.visibility = View.VISIBLE
            binding.creditMsgView.text = "Amount credited successfully "
            binding.depositBalanceView.visibility = View.VISIBLE
            binding.depositBalanceText.visibility = View.VISIBLE
            binding.depositBalanceText.text = balance.toString()
        }
    }


}
