package com.example.atmbank

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.atmbank.databinding.ActivityTransactionsBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class Transactions : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionsBinding
    lateinit var arrayList: ArrayList<Account>
    lateinit var theList: ArrayList<Account> /* = java.util.ArrayList<com.example.atmbank.Account> */
    var accountNumber: String = ""
    var balance: Int = 0
    var value: String = ""
    var setbalance = ""
    var json: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        value = intent.getStringExtra("value").toString()
        arrayList = (intent.getSerializableExtra("list") as ArrayList<Account>?)!!
        accountNumber = intent.getStringExtra("account_number").toString()
        if (NetworkHelper.isNetworkConnected(this)) {

            val sharedPreferences = getSharedPreferences("sharedPre", Context.MODE_PRIVATE)
            json = sharedPreferences.getString("arraylist", null).toString()
            val gson = GsonBuilder().create()
            theList = gson.fromJson<ArrayList<Account>>(
                json,
                object : TypeToken<ArrayList<Account>>() {}.type
            )

            //checking for user selection
            when (value) {
                "deposit" -> {
                    depositAmount(theList)
                }
                "withdraw" -> {
                    withdrawAmount(theList)
                }
                "balance" -> {
                    balanceEnquiry(theList)
                }
                "exit" -> {
                    exitTransaction(theList)
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


    //function to hide keyboard
    private fun closeKeyboard(view: View) {

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //function to display message
    private fun exitTransaction(theList: ArrayList<Account>) {
        binding.depositLayout.visibility = View.GONE
        binding.withdrawLayout.visibility = View.GONE
        binding.thanksLayout.visibility = View.VISIBLE
        binding.backBtn.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    //function to read withdraw amount and update arraylist
    private fun withdrawAmount(theList: ArrayList<Account>) {
        binding.depositLayout.visibility = View.GONE
        binding.thanksLayout.visibility = View.GONE
        binding.withdrawBalanceView.visibility = View.GONE
        binding.withdrawBalanceText.visibility = View.GONE
        binding.withdrawSubmitButton.setOnClickListener {
            if (NetworkHelper.isNetworkConnected(this)) {

                if (binding.withdrawAmtText.text.toString() == "") {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Alert!")
                    builder.setMessage("Please input amount")
                    builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> })
                    builder.show()
                    //binding.debitMsgView.text = "Please Input Amount"
                } else {
                    withdrawSubmit(theList)
                }
            } else {
                Toast.makeText(
                    this,
                    "Sorry! There Is No Internet Connection.Please Try Again Later",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.withdrawAmtText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                withdrawSubmit(theList)//function call to to update arraylist
                true
            } else {
                false
            }
        }
    }

    private fun withdrawSubmit(theList: ArrayList<Account>) {
        closeKeyboard(binding.withdrawAmtText)

        var withdrawAmount: Int = binding.withdrawAmtText.text.toString().toInt()
        if (withdrawAmount % 100 != 0) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Alert!")
            builder.setMessage("Amount Must Be Multiple Of 100")
            builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> })
            builder.show()
            // binding.debitMsgView.text = "Amount Must Be Multiple Of 100"
        } else {

            if (theList != null) {
                for (i in 0 until theList.size) {
                    if (accountNumber == theList[i].accountNumber) {

                        if (withdrawAmount > theList[i].balance) {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Alert!")
                            builder.setMessage("Insufficient balance")
                            builder.setPositiveButton(
                                "OK",
                                { dialogInterface: DialogInterface, i: Int -> })
                            builder.show()
                            // binding.debitMsgView.text = "Insufficient balance"
                            break
                        } else {
                            theList.get(i).balance -= binding.withdrawAmtText.text.toString()
                                .toInt()
                            balance = theList[i].balance
                            println(balance)
                            println(theList.get(i).balance)
                            val sharedPreferences =
                                getSharedPreferences("sharedPre", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            val gson: Gson = Gson()
                            json = gson.toJson(theList)
                            println(json)
                            editor.putString("arraylist", json)
                            editor.apply()
                        }
                        binding.debitMsgView.visibility = View.VISIBLE
                        binding.debitMsgView.text = "Amount debited successfully "
                        binding.withdrawBalanceView.visibility = View.VISIBLE
                        binding.withdrawBalanceText.visibility = View.VISIBLE
                        binding.withdrawBalanceText.text = balance.toString()



                    }
                }
            }
        }
        binding.withdrawAmtText.text.clear().toString()

    }

    //function to read deposit amount and update arraylist
    private fun depositAmount(theList: ArrayList<Account>) {
        binding.withdrawLayout.visibility = View.GONE
        binding.thanksLayout.visibility = View.GONE
        binding.depositBalanceView.visibility = View.GONE
        binding.depositBalanceText.visibility = View.GONE
        binding.submitButton.setOnClickListener {
            if (NetworkHelper.isNetworkConnected(this)) {
                if (binding.depositAmtText.text.toString() == "") {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Alert!")
                    builder.setMessage("Please input amount")
                    builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> })
                    builder.show()
                } else {
                    depositSubmit(theList)//function call to update arraylist
                }
            } else {
                Toast.makeText(
                    this,
                    "Sorry! There Is No Internet Connection.Please Try Again Later",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.depositAmtText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                depositSubmit(theList)
                true
            } else {
                false
            }
        }
    }

    private fun depositSubmit(theList: ArrayList<Account>) {
        closeKeyboard(binding.depositAmtText)
        var depositAmount: Int = binding.depositAmtText.text.toString().toInt()
        if (depositAmount % 100 != 0) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Alert!")
            builder.setMessage("Amount Must Be Multiple Of 100")
            builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> })
            builder.show()
            // binding.creditMsgView.text = "Amount Must Be Multiple Of 100"
        } else {
            if (theList != null) {
                for (i in 0 until theList.size) {
                    if (accountNumber == theList[i].accountNumber) {
                        theList[i].balance += binding.depositAmtText.text.toString()
                            .toInt()
                        balance = theList[i].balance
                        theList[i].balance = balance
                    }
                }

                val sharedPreferences = getSharedPreferences("sharedPre", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val gson: Gson = Gson()
                json = gson.toJson(theList)
                println(json)
                editor.putString("arraylist", json)
                editor.apply()
            }
            binding.depositAmtText.text.clear().toString()
            binding.creditMsgView.visibility = View.VISIBLE
            binding.creditMsgView.text = "Amount credited successfully "
            binding.depositBalanceView.visibility = View.VISIBLE
            binding.depositBalanceText.visibility = View.VISIBLE
            binding.depositBalanceText.text = balance.toString()

        }
    }

    //function to display balance amount in arraylist
    private fun balanceEnquiry(theList: ArrayList<Account>) {
        binding.depositLayout.visibility = View.VISIBLE
        binding.withdrawLayout.visibility = View.GONE
        binding.depositAmtView.visibility = View.GONE
        binding.depositAmtText.visibility = View.GONE
        binding.creditMsgView.visibility = View.GONE
        binding.submitButton.visibility = View.GONE
        binding.thanksLayout.visibility = View.GONE

        if (theList != null) {
            for (i in 0 until theList.size) {
                if (accountNumber == theList[i].accountNumber) {
                    binding.depositBalanceText.text = theList.get(i).balance.toString()
                }
            }
        }
    }
}



