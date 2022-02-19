package com.example.expensesappkotlin

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesappkotlin.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

private lateinit var binding: ActivityMainBinding
var itemSelectedOnSpinner = ""

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) /** for the animation **/

        /** setting the initial value of the views when coming back from the add activity **/
        binding.root.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.fab.isVisible = true
        binding.progressBar.isVisible = true
        binding.tvZero.isVisible = true
        binding.tvMaxAmount.isVisible = true
        binding.spinner.isVisible = true
        binding.rvExpenses.isVisible = true


        /** Defining the views **/
        //val fab: FloatingActionButton = findViewById(R.id.fab)
        val spinner: Spinner = findViewById(R.id.spinner)

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

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                itemSelectedOnSpinner = adapterView?.getItemAtPosition(position).toString()
                Toast.makeText(binding.spinner.context, "You have selected $itemSelectedOnSpinner", Toast.LENGTH_SHORT).show()
                binding.rvExpenses.adapter = null
                setupListOfDataIntoRecyclerView()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

    }
}

fun creatingDataForTheSpinner(): MutableList<String?>{
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

private fun setupListOfDataIntoRecyclerView(){
    if(getItemsList().size > 0){
        /** set the layout manager which this recycler view will use **/
        binding.rvExpenses.layoutManager = LinearLayoutManager(binding.rvExpenses.context)
        /** adapter is initialized and list is passed to the parameters **/
        val adapter = ItemAdapter(binding.rvExpenses.context, getItemsList())
        /** adapter instance is set to the recycler view to inflate the item **/
        binding.rvExpenses.adapter = adapter
    }else{

    }
}

fun getItemsList(): ArrayList<ExpenseModel> {
    val db = MyDataBaseHelper(binding.rvExpenses.context)
    var itemSelectedOnSpinnerr = readingDataFromSpinnerForDatabase()
    return db.readDataFromDatabase(itemSelectedOnSpinnerr)
}


fun readingDataFromSpinnerForDatabase(): String{
    /** Reading the input of the spinner **/
    binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
            itemSelectedOnSpinner = adapterView?.getItemAtPosition(position).toString()
            //Toast.makeText(binding.spinner.context, "You have selected $itemSelectedOnSpinner", Toast.LENGTH_SHORT).show()
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

    }
    return "LIKE '%$itemSelectedOnSpinner%'"
}