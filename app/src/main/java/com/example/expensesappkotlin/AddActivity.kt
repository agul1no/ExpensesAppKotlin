package com.example.expensesappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.expensesappkotlin.databinding.ActivityAddBinding

private lateinit var binding: ActivityAddBinding

var itemSelectedOnSpinner2 = ""

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addButton : Button = findViewById(R.id.addButton)
        val spinner2 : Spinner = findViewById(R.id.spinner2)

        /** Creating and setting Data for the spinner and spinnerAdapter **/
        var spinnerList = creatingDataForTheSpinner()
        var arrayAdapter = ArrayAdapter(this,R.layout.spinner_text,spinnerList)
        spinner2.adapter = arrayAdapter

        /** Reading the input of the spinner **/
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                itemSelectedOnSpinner2 = adapterView?.getItemAtPosition(position).toString()
                //Toast.makeText(binding.spinner2.context, "You have selected $itemSelectedOnSpinner2", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        nameFocusListener()
        amountFocusListener()

        addButton.setOnClickListener {
            addForm()
        }

    }

    private fun addForm() {

        binding.textFieldName.helperText = validName()
        binding.textFieldAmount.helperText = validAmount()

        val validName = binding.textFieldName.helperText == "required"
        val validAmount = binding.textFieldAmount.helperText == "required"
        val db = MyDataBaseHelper(this)

        if(validName && validAmount){
            val status = db.addExpense(ExpenseModel(0, binding.textEditName.text.toString().trim(), binding.textEditAmount.text.toString().toDouble(),binding.textEditComment.text.toString().trim(),
                itemSelectedOnSpinner2))
            if(status > -1){
                Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show()
                binding.textEditName.text?.clear()
                binding.textEditAmount.text?.clear()
                binding.textEditComment.text?.clear()
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this, "Error inserting data", Toast.LENGTH_SHORT).show()
                }
        }else{
            invalidForm()
        }
    }

    private fun invalidForm() {
        var message = ""
        if(binding.textFieldName.helperText != "required")
            message += "\n\nName: " + binding.textFieldName.helperText
        if(binding.textFieldAmount.helperText != "required")
            message += "\n\nAmount: " + binding.textFieldAmount.helperText

        AlertDialog.Builder(this)
            .setTitle("Invalid Values")
            .setMessage(message)
            .setPositiveButton("Okay"){_,_->
                //do nothing
            }
            .show()
    }

    private fun amountFocusListener() {
        binding.textEditAmount.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.textFieldAmount.helperText = validAmount()
            }
        }
    }

    private fun validAmount(): String? {
        if(binding.textEditAmount.text.toString().startsWith(".") || binding.textEditAmount.text.toString().endsWith(".") || binding.textEditAmount.text.toString().contains("..") || binding.textEditAmount.text.toString() == ""){
            return "Invalid Amount"
        }
        return "required"
    }

    private fun nameFocusListener() {
        binding.textEditName.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.textFieldName.helperText = validName()
            }
        }
    }

    private fun validName(): String? {
        if(binding.textEditName.text.toString().length < 3){
            return "Invalid Name"
        }
        return "required"
    }

    override fun onBackPressed() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
}