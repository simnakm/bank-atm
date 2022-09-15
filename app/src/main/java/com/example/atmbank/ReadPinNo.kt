package com.example.atmbank

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.atmbank.databinding.ActivityReadPinNoBinding

class ReadPinNo : AppCompatActivity() {

    private lateinit var binding: ActivityReadPinNoBinding
    lateinit var arrayList: ArrayList<Account>
    var currentAccount = ""
    var savingsAccount = ""
    var digits: Int = 0
    var flag: Int = 0
    var accountNumber: String = ""
    var password: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadPinNoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(NetworkHelper.isNetworkConnected(this)) {
        currentAccount = intent.getStringExtra("type").toString()
        savingsAccount = intent.getStringExtra("type").toString()
        setArray()

            binding.submitButton.setOnClickListener {
                submit()
                binding.accountNoText.setOnEditorActionListener { _, i, _ ->
                    if (i == EditorInfo.IME_ACTION_DONE) {
                        binding.messageView.text = "Please Enter Your PIN Number"
                        true
                    } else {
                        false
                    }
                }
                binding.pinNoText.setOnEditorActionListener { _, i, _ ->
                    if (i == EditorInfo.IME_ACTION_DONE) {
                        submit()
                        true
                    } else {
                        false
                    }
                }
            }
        }
        else{
            Toast.makeText(
                this,
                "Sorry! There Is No Internet Connection.Please Try Again Later",
                Toast.LENGTH_LONG
            ).show()
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
    }
    //function to check user input account number and pin number matches with
    // account number and pin number in arraylist
    private fun submit() {
        binding.messageView.text = ""
        if (binding.accountNoText.text.toString() == "") {
            binding.messageView.text = "Please Enter Your Account Number"
        } else {
            digits = binding.accountNoText.text.toString().length
            if (digits != 16) {
                binding.messageView.text = "Please Check Your Account Number "
            }
        }
        accountNumber = binding.accountNoText.text.toString()
        if (binding.pinNoText.text.toString() == "") {
            binding.messageView.text = "Please Enter Your PIN Number"
        } else {
            password = binding.pinNoText.text.toString()
            if (password.length != 4) {
                binding.messageView.text = "Please Check Your Pin Number"
            }
        }
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
                    intent.putExtra("account_number", accountNumber)
                    startActivity(intent)
                }
                if (savingsAccount.equals("savings")) {
                    intent = Intent(this, DisplayOPtions::class.java)
                    intent.putExtra("type", savingsAccount)
                    intent.putExtra("list", arrayList)
                    intent.putExtra("account_number", accountNumber)
                    startActivity(intent)
                }
            }
        }
        if (flag == 0) {
            binding.messageView.text = "Please Enter Valid Account Number And PIN Number"
        }
    }
}

