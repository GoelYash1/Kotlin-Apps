package com.example.expensetracker.data.models

import android.os.IBinder.DeathRecipient
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey
    val timestamp: Long,
    val title: String,
    val otherPartyName: String,
    val amount: Double,
    val type: String,
    @ColumnInfo(name = "category_name")
    val categoryName: String?,
    @ColumnInfo(name = "account_id")
    val accountId: String?
)

