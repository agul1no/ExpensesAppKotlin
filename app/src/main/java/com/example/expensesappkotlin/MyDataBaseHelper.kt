package com.example.expensesappkotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MyDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VERSION) {

    companion object{ //creating constants
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Expenses.db"
        private const val TABLE_NAME = "expenses"

        private const val KEY_ID = "_id"
        private const val EXPENSE_NAME = "expense_name"
        private const val EXPENSE_AMOUNT = "expense_amount"
        private const val EXPENSE_COMMENT = "expense_comment"
        private const val EXPENSE_DATE = "expense_date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query: String = "CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY, $EXPENSE_NAME TEXT, $EXPENSE_AMOUNT REAL, $EXPENSE_COMMENT TEXT, $EXPENSE_DATE TEXT);"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addExpense(expense: ExpenseModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(EXPENSE_NAME, expense.name)
        contentValues.put(EXPENSE_AMOUNT, expense.amount)
        contentValues.put(EXPENSE_COMMENT, expense.comment)
        contentValues.put(EXPENSE_DATE, expense.date)

        val success = db.insert(TABLE_NAME,null,contentValues)
        db.close()
        return success
    }

    fun readDataFromDatabase(itemSelectedOnSpinner : String): ArrayList<ExpenseModel> {
        val expenseList : ArrayList<ExpenseModel> = ArrayList()
        val db = this.readableDatabase
        //val itemSelectedOnSpinner = readingDataFromSpinnerForDatabase()
        val query = "SELECT * FROM $TABLE_NAME WHERE $EXPENSE_DATE $itemSelectedOnSpinner"
        var cursor : Cursor? = null

        try {
            cursor = db.rawQuery(query,null)
        }catch (e: SQLException){
            db.execSQL(query)
            return ArrayList()
        }

        var id: Int
        var expenseName : String
        var expenseAmount : Double
        var expenseComment : String
        var expenseDate : String

        while (cursor.moveToNext()){
            id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            expenseName = cursor.getString(cursor.getColumnIndex(EXPENSE_NAME))
            expenseAmount = cursor.getDouble(cursor.getColumnIndex(EXPENSE_AMOUNT))
            expenseComment = cursor.getString(cursor.getColumnIndex(EXPENSE_COMMENT))
            expenseDate = cursor.getString(cursor.getColumnIndex(EXPENSE_DATE))

            val expense = ExpenseModel(id,expenseName,expenseAmount,expenseComment,expenseDate)
            expenseList.add(expense)
        }
        return expenseList
    }
}

