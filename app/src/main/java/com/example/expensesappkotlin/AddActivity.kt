package com.example.expensesappkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val textEditName: EditText = findViewById(R.id.textEditName)
        val textEditAmount: EditText = findViewById(R.id.textEditAmount)
        val textEditComment: EditText = findViewById(R.id.textEditComment)
        val addButton : Button = findViewById(R.id.addButton)

        addButton.setOnClickListener {
            if (textEditName.toString().trim() == "" || textEditName.toString().trim() == " "){
                Toast.makeText(this, "Expense Name can not be empty", Toast.LENGTH_SHORT).show()
            } else if (textEditAmount.toString().startsWith(".") || textEditAmount.toString().contains("..") || textEditAmount.toString().endsWith(".")){
                Toast.makeText(this, "Expense Amount can not be empty", Toast.LENGTH_SHORT).show()
            }else{
                //addExpense()
                textEditName.text.clear()
                textEditAmount.text.clear()
                textEditComment.text.clear()
            }

        }
    }
}