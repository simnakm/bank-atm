package com.example.atmbank

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.atmbank.databinding.ActivityReadPinNoBinding
import com.google.gson.Gson

class ReadPinNo : AppCompatActivity() {

    private lateinit var binding: ActivityReadPinNoBinding
    lateinit var arrayList: ArrayList<Account>
    var currentAccount = ""
    var savingsAccount = ""
    var digits: Int = 0
    var flag: Int = 0
    var accountNumber: String = ""
    var password: String = ""
    var json: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadPinNoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //if(NetworkHelper.isNetworkConnected(this)) {
        currentAccount = intent.getStringExtra("type").toString()
        savingsAccount = intent.getStringExtra("type").toString()
        binding.pinNoText.visibility = View.GONE
        binding.submitButton.visibility = View.GONE
        setArray()

        binding.submitButton.setOnClickListener {
            if (NetworkHelper.isNetworkConnected(this)) {
                submit()
            } else {
                Toast.makeText(
                    this,
                    "Sorry! There Is No Internet Connection.Please Try Again Later",
                    Toast.LENGTH_LONG
                ).show()
            }

        }//submit ends here

        binding.accountNoText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (binding.accountNoText.text.toString() == "") {

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Alert!")
                    builder.setMessage("Please Enter Your Account Number")
                    builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> })
                    builder.show()
                    binding.pinNoText.visibility = View.GONE
                    binding.submitButton.visibility = View.GONE
                    // binding.messageView.text = "Please Enter Your Account Number"
                } else {
                    digits = binding.accountNoText.text.toString().length
                    if (digits != 16) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Alert!")
                        builder.setMessage("Please Check Your Account Number")
                        builder.setPositiveButton("OK",
                            { dialogInterface: DialogInterface, i: Int -> })
                        builder.show()
                    }
                }
                if ((digits == 16) && (binding.accountNoText.text.toString() != "")) {
                    binding.pinNoText.visibility = View.VISIBLE
                    binding.pinNoText.isEnabled=true
                }
                true
            } else {
                false
            }
        }
        binding.pinNoText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (NetworkHelper.isNetworkConnected(this)) {
                    if (binding.pinNoText.text.toString() == "") {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Alert!")
                        builder.setMessage("Please Enter Your Pin Number")
                        builder.setPositiveButton("OK",
                            { dialogInterface: DialogInterface, i: Int -> })
                        builder.show()
                    } else {
                        password = binding.pinNoText.text.toString()
                        if (password.length != 4) {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Alert!")
                            builder.setMessage("Please Check Your Pin Number")
                            builder.setPositiveButton("OK",
                                { dialogInterface: DialogInterface, i: Int -> })
                            builder.show()
                        }
                    }
                    submit()
                } else {
                    Toast.makeText(
                        this,
                        "Sorry! There Is No Internet Connection.Please Try Again Later",
                        Toast.LENGTH_LONG
                    ).show()
                }

                if ((binding.pinNoText.text.toString() == "") && (password.length != 4)) {
                    binding.submitButton.visibility = View.VISIBLE
                }
                true
            } else {
                false
            }
        }
    }

    //function to give account details as arraylist using Account class
    private fun setArray() {
        arrayList = ArrayList<Account>()//initializing arraylist
        //assigning values to arraylist using constructor in Account class
        val paul = Account("4035300539804083", "1290", "Alexandra Paul", 10000)
        val john = Account("4742127848216186", "2349", "Samuel John", 50000)
        val hussain = Account("4494329568491763", "3578", "Zakir Hussain", 2000)
        //adding values to arraylist using add()
        arrayList.add(paul)
        arrayList.add(john)
        arrayList.add(hussain)

        val sharedPreferences = getSharedPreferences("sharedPre", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson: Gson = Gson()
        json = gson.toJson(arrayList)
        println(json)
        editor.putString("arraylist", json)
        editor.apply()
    }

    //function to check user input account number and pin number matches with
    // account number and pin number in arraylist
    private fun submit() {
        accountNumber = binding.accountNoText.text.toString()
        //checks user input with values in arraylist
        for (i in 0 until arrayList.size) {
            if (accountNumber.equals(arrayList.get(i).accountNumber) && (password.equals(
                    arrayList.get(i).password
                ))
            ) {
                flag = 1
                //goes to next activity if user give correct account number
                // and pin number
                if (currentAccount.equals("current")) {
                    intent = Intent(this, DisplayOPtions::class.java)
                    intent.putExtra("type", currentAccount)
                    intent.putExtra("list", arrayList)
                    // intent.putExtra("list", json)
                    intent.putExtra("account_number", accountNumber)
                    startActivity(intent)
                    binding.pinNoText.text.clear()
                }
                if (savingsAccount.equals("savings")) {
                    intent = Intent(this, DisplayOPtions::class.java)
                    intent.putExtra("type", savingsAccount)
                    intent.putExtra("list", arrayList)
                    //intent.putExtra("list", json)
                    intent.putExtra("account_number", accountNumber)
                    startActivity(intent)

                    binding.pinNoText.text.clear()

                }
            }
        }
        if (flag != 1) {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Alert!")
            builder.setMessage("Your account number and pin number does not match")
            builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> })
            builder.show()
            binding.pinNoText.visibility = View.GONE
            binding.submitButton.visibility = View.GONE
        }
    }


}

