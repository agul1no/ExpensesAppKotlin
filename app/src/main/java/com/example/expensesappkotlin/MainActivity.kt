package com.example.expensesappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesappkotlin.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

private lateinit var binding: ActivityMainBinding

val expenses = ArrayList<ExpenseModel>()
var expense1 = ExpenseModel(1,"Netto", 8.3,"Comment","Feb/22")

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** Defining the views **/
        //val fab: FloatingActionButton = findViewById(R.id.fab)
        val spinner: Spinner = findViewById(R.id.spinner)
        val rvExpenses: RecyclerView = findViewById(R.id.rvExpenses)

        /** Creating and setting Data for the spinner and spinnerAdapter **/
        var spinnerList = creatingDataForTheSpinner()
        var arrayAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,spinnerList)
        spinner.adapter = arrayAdapter

        /** Creating animation für floating action button **/

        val animation = AnimationUtils.loadAnimation(this, R.anim.circle_explosion_anim).apply {
            duration = 900
            interpolator = AccelerateDecelerateInterpolator()
        }

        binding.fab.setOnClickListener {
            binding.fab.isVisible = false
            binding.circle.isVisible = true
            binding.circle.startAnimation(animation) {
                binding.root.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
                binding.circle.isVisible = false
                binding.progressBar.isVisible = false
                binding.tvZero.isVisible = false
                binding.tvMaxAmount.isVisible = false
                binding.spinner.isVisible = false
                binding.rvExpenses.isVisible = false
                intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
            }
        }

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