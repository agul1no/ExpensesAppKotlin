package com.example.expensesappkotlin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MyDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VERSION) {

    companion object{ //creating constants
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Expenses.db"
        private const val TABLE_NAME = "Expenses"

        private const val KEY_ID = "_id"
        private const val EXPENSE_NAME = "expense_name"
        private const val EXPENSE_AMOUNT = "expense_amount"
        private const val EXPENSE_COMMENT = "expense_comment"
        private const val EXPENSE_DATE = "expense_date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query: String = ("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID +
                "INTEGER PRIMARY KEY," + EXPENSE_NAME + "TEXT," +
                EXPENSE_AMOUNT + "REAL," + EXPENSE_COMMENT + "TEXT," + EXPENSE_DATE + "DATE"+")")
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
}

fun addExpense(){

}