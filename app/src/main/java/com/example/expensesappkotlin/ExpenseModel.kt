package com.example.expensesappkotlin

data class ExpenseModel (
    val id: Int,
    var name: String,
    var amount: Double,
    var comment: String,
    var date: String
        )