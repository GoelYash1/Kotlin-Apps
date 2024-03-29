package com.example.expensetracker.data.repo

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.expensetracker.api.SMSReadAPI
import com.example.expensetracker.data.db.ExpenseTrackerDatabase
import com.example.expensetracker.data.dtos.SMSMessageDTO
import com.example.expensetracker.data.models.Transaction
import com.example.expensetracker.data.models.TransactionCategories
import com.example.expensetracker.helper.TransactionSMSFilter
import kotlinx.coroutines.flow.Flow
import java.time.Month
import java.time.Year

class ExpenseTrackerRepository(
    private val db: ExpenseTrackerDatabase,
    private val context: Context
){
    private val transactionDao = db.transactionDao()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun readAndStoreSMS(year: Int = Year.now().value, month: Month? = null, date: Int? = null) {
        val smsMessages = readSMSMessages(year, month, date)
        val transactions = processSMSMessages(smsMessages)
        storeTransactions(transactions)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun readSMSMessages(year: Int?, month: Month?, date: Int?): Map<String, List<SMSMessageDTO>> {
        val smsReadAPI = SMSReadAPI(context.contentResolver)
        return smsReadAPI.getGroupedSMSMessagesByDateMonthYear(year, month, date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun processSMSMessages(smsMessages: Map<String, List<SMSMessageDTO>>): List<Transaction> {
        val transactions = mutableListOf<Transaction>()

        smsMessages.flatMap { (_, messages) ->
            messages.map { sms ->
                val transactionFilter = TransactionSMSFilter()
                val amountSpent = transactionFilter.getAmountSpent(sms.body) ?: 0.0
                val isExpense = transactionFilter.isExpense(sms.body)
                val extractedAccount = transactionFilter.extractAccount(sms.body)

                val defaultTitle: String?
                val defaultCategoryName: String?
                val otherPartyName: String?

                val account = extractedAccount?.let { db.accountDao().getAccountByAccountId(it) }
                if (account == null) {
                    // Account is not present, implement the prompt logic here

                    // Set default values
                    otherPartyName = if (isExpense) "To Whom?" else "From Whom?"
                    defaultTitle = "What was the payment for?"
                    defaultCategoryName = TransactionCategories.OTHERS
                } else {
                    defaultTitle = account.defaultTitle
                    defaultCategoryName = account.defaultCategoryName
                    otherPartyName = account.name
                }

                val transaction = Transaction(
                    title = defaultTitle,
                    otherPartyName = otherPartyName,
                    amount = amountSpent,
                    timestamp = sms.time,
                    type = if (isExpense) "Expense" else "Income",
                    categoryName = defaultCategoryName,
                    accountId = extractedAccount
                )

                transactions.add(transaction)
            }
        }

        return transactions
    }

    private fun storeTransactions(transactions: List<Transaction>) {
        transactions.forEach { transaction ->
            transactionDao.insertTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction){
        transactionDao.deleteTransaction(transaction)
    }
    fun editTransaction(transaction: Transaction){
        transactionDao.editTransaction(transaction)
    }

    fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions()
    }

    fun getTransactionsForTimePeriod(startTimestamp: Long, endTimestamp: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsForTimePeriod(startTimestamp, endTimestamp)
    }

    fun getTransactionsForCategory(categoryName: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsForCategory(categoryName)
    }
}



