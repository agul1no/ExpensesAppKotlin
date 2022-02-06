package com.example.expensesappkotlin

/** Data class. Contains the properties of each expense (item) **/

data class ExpenseModel (
    val id: Int,
    var name: String,
    var amount: Double,
    var comment: String,
    var date: String
        )