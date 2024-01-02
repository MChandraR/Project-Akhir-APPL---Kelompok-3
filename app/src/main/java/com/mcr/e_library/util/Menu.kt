package com.mcr.e_library.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Menu(var route:String, val title:String?, val icon: ImageVector?){
    object  Home : Menu("home","Home", Icons.Default.Home)
    object  Scan : Menu("scan","Scan", Icons.Default.AccountBox)
    object  Profile : Menu("profile","Profile", Icons.Default.Person)
    object  Rak : Menu("rak","Rak",Icons.Default.ShoppingCart)
    object Setting : Menu("settings","Settings",Icons.Default.Settings)
    object  Empty : Menu("empty", "Empty",null)
}
