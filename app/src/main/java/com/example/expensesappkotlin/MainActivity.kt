package com.example.expensesappkotlin

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesappkotlin.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.roundToInt
import androidx.fragment.app.activityViewModels

private lateinit var binding: ActivityMainBinding
var itemSelectedOnSpinner = ""
var maxAmount : Int = 100

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) /** for the animation **/

        /** Setting the shared Preferences **/
        var sharedPreferences = getSharedPreferences("maxAmount", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        maxAmount = sharedPreferences.getInt("maxAmount",100)
        binding.tvMaxAmount.text = "Limit: $maxAmount €"

        /** setting the initial value of the views when coming back from the add activity **/
        binding.root.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.fab.isVisible = true
        binding.progressBar.isVisible = true
        binding.tvPercent.isVisible = true
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

        /** reading the info from spinner **/
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                itemSelectedOnSpinner = adapterView?.getItemAtPosition(position).toString()
                //Toast.makeText(binding.spinner.context, "You have selected $itemSelectedOnSpinner", Toast.LENGTH_SHORT).show()
                binding.rvExpenses.adapter = null
                setupListOfDataIntoRecyclerView(itemSelectedOnSpinner)
                settingMaxAmountAndPercent()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        /** Setting the data to the views and onClickListener for the Limit Amount **/
        settingMaxAmountAndPercent()
        binding.tvMaxAmount.setOnClickListener {
            val maxAmountDialog = Dialog(this,R.style.Theme_Dialog)
            maxAmountDialog.setCancelable(false)
            maxAmountDialog.setContentView(R.layout.dialog_maxamount)
            val etAmount = maxAmountDialog.findViewById<EditText>(R.id.etAmount)
            val updateButton = maxAmountDialog.findViewById<Button>(R.id.updateButton)
            val cancelButton = maxAmountDialog.findViewById<Button>(R.id.cancelButton)
            maxAmountDialog.show()
            cancelButton.setOnClickListener {
                maxAmountDialog.dismiss()
            }
            updateButton.setOnClickListener {
                maxAmount = etAmount.text.toString().toInt()
                binding.tvMaxAmount.text = "Limit: $maxAmount €"
                etAmount.text.clear()
                maxAmountDialog.dismiss()
                settingMaxAmountAndPercent()
                editor.apply {
                    putInt("maxAmount", maxAmount)
                    apply()
                }
            }


        }


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
                binding.tvPercent.isVisible = false
                binding.tvMaxAmount.isVisible = false
                binding.spinner.isVisible = false
                binding.rvExpenses.isVisible = false
                binding.tvTotal.isVisible = false
                binding.constraintLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.purple_500))
                intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
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

private fun setupListOfDataIntoRecyclerView(spinnerItem: String){
    if(getItemsList(spinnerItem).size > 0){
        /** set the layout manager which this recycler view will use **/
        binding.rvExpenses.layoutManager = LinearLayoutManager(binding.rvExpenses.context)
        /** adapter is initialized and list is passed to the parameters **/
        val adapter = ItemAdapter(binding.rvExpenses.context, getItemsList(spinnerItem))
        /** adapter instance is set to the recycler view to inflate the item **/
        binding.rvExpenses.adapter = adapter
    }else{

    }
}

fun getItemsList(spinnerItem:String): ArrayList<ExpenseModel> {
    val db = MyDataBaseHelper(binding.rvExpenses.context)
    //var itemSelectedOnSpinnerr = readingDataFromSpinnerForDatabase()
    return db.readDataFromDatabase(spinnerItem)
}

fun getTotalAmountExpenses(spinnerItem: String) : Double{
    var expensesList = getItemsList(spinnerItem)
    var totalExpenses : Double = 0.0
    if (getItemsList(spinnerItem).size > 0){
        for (item in expensesList){
            totalExpenses += item.amount
        }
    }
    return totalExpenses
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

fun settingMaxAmountAndPercent(){
    var totalAmountExpenses = getTotalAmountExpenses(itemSelectedOnSpinner)
    binding.tvTotal.text = "Total: $totalAmountExpenses €"
    val percentage = Math.round((totalAmountExpenses/maxAmount)* 100)
    binding.tvPercent.text = "${percentage} %"
    binding.progressBar.setProgress(percentage.toInt())
}
