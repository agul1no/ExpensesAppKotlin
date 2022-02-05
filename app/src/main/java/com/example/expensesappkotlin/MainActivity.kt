package com.example.expensesappkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.spinner)
        val rvExpenses: RecyclerView = findViewById(R.id.rvExpenses)

        var spinnerList = creatingDataForTheSpinner()
        var arrayAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,spinnerList)
        spinner.adapter = arrayAdapter

        val expenses = ArrayList<ExpenseModel>()
        var expense1 = ExpenseModel(1,"Netto", 8.3,"Comment","Feb/22")
        expenses.add(expense1)

        val adapter = ItemAdapter(this, expenses)
        rvExpenses.adapter = adapter
        rvExpenses.layoutManager = LinearLayoutManager(this)

    }
}

private fun creatingDataForTheSpinner(): MutableList<String?>{
    /** Creating the mutableListOf Month/Year **/
    val calendar = Calendar.getInstance()
    var month = (calendar.get(Calendar.MONTH)) /** January = 0, February = 1 etc... **/
    var year = calendar.get(Calendar.YEAR)

    var spinnerlist = mutableListOf<String?>()
    for (i in 0..5){
        if(month == 0){
            spinnerlist.add(transformingDateFromIntToString(month,year))
            month = 12
            year -= 1
        }else{
            spinnerlist.add(transformingDateFromIntToString(month,year))
        }
        month -= 1
    }
    return spinnerlist
}

private fun transformingDateFromIntToString(month: Int, year: Int): String? {
    var monthString: String? = null
    var yearString: String? = null
    when(month){
        0 -> monthString = "Jan"
        1 -> monthString = "Feb"
        2 -> monthString = "Mar"
        3 -> monthString = "Apr"
        4 -> monthString = "Mai"
        5 -> monthString = "Jun"
        6 -> monthString = "Jul"
        7 -> monthString = "Aug"
        8 -> monthString = "Sep"
        9 -> monthString = "Oct"
        10 -> monthString = "Nov"
        11 -> monthString = "Dec"
    }
    when(year){
        2021 -> yearString = "21"
        2022 -> yearString = "22"
        2023 -> yearString = "23"
        2024 -> yearString = "24"
        2025 -> yearString = "25"
    }
    return "$monthString / $yearString"
}

private fun readingDataFromSpinnerForDatabase(spinnerInput: String): String{
    val delimitator = "/"
    var listSpinnerDataSeparated = spinnerInput.split(delimitator)
    return "$listSpinnerDataSeparated[0] $listSpinnerDataSeparated[1]"
}