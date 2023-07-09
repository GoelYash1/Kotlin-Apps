package com.example.expensetracker

interface Destinations {
    val route: String
    val icon: Int
    val title: String
}

object Home: Destinations{
    override val route = "Home"
    override val icon = R.drawable.ic_home
    override val title = "Home"
}
object Main: Destinations{
    override val route = "Main"
    override val icon = R.drawable.ic_home
    override val title = "Main"
}
object OnBoarding: Destinations{
    override val route = "OnBoarding"
    override val icon = R.drawable.ic_home
    override val title = "OnBoarding"
}

object Transactions: Destinations{
    override val route = "Transactions"
    override val icon = R.drawable.ic_transactions
    override val title = "Transactions"
}

object Notification: Destinations{
    override val route = "Notification"
    override val icon = R.drawable.ic_notifications
    override val title = "Notification"
}
