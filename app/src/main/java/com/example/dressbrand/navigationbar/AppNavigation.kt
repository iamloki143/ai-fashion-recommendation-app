package com.example.dressbrand.navigationbar

import android.R.id.home
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.dressbrand.data.CartItem
import com.example.dressbrand.data.Product
import com.example.dressbrand.ui.screen.cartscreen.CartScreen
import com.example.dressbrand.ui.screen.homescreen.HomeScreen
import com.example.dressbrand.ui.screen.homescreen.ProductDetailScreen
import com.example.dressbrand.ui.screen.profilescreen.ProfileScreen
import com.example.dressbrand.ui.screen.searchscreen.SearchResultsScreen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect

import com.example.dressbrand.data.room.DatabaseProvider
import com.example.dressbrand.data.room.dao.CartDao

@Composable
fun AppNavigation(
    paymentLauncher:
    ActivityResultLauncher<Intent>
) {
    var currentSearchQuery by remember {
        mutableStateOf("")
    }
    var currentScreen by remember { mutableStateOf("home") }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val cartItems = remember { mutableStateListOf<CartItem>() }
    val context = LocalContext.current

    val cartDao =
        DatabaseProvider
            .getDatabase(context)
            .CartDao()
    if (selectedProduct != null){
        ProductDetailScreen(
            product = selectedProduct!!,
            cartItems=cartItems,
            onBack ={
                selectedProduct=null
            }
        )
        return
    }
    LaunchedEffect(Unit) {
        val savedCart = cartDao.getCartItems()
        cartItems.clear()
        savedCart.forEach {
            cartItems.add(
                CartItem(
                    productId =
                        it.productId,

                    productName =
                        it.productName,

                    imageUrl =
                        it.imageUrl,

                    price =
                        it.price,

                    quantity =
                        it.quantity,

                    selectedSize =
                        it.selectedSize
                )
            )
        }
    }
    when(currentScreen){
        "home" -> {
            HomeScreen(
                onCartClick ={
                    currentScreen="cart"
                },
                onProfileClick ={
                    currentScreen="profile"
                },
                onProductClick ={ productName ->
                    selectedProduct = productName
                },
                onSearchClick = {query ->
                    currentSearchQuery=query
                    currentScreen="search"
                }
            )
        }
        "cart" -> {
            CartScreen(
                onHomeClick ={
                    currentScreen="home"
                },
                onProfileClick ={
                    currentScreen="profile"
                },
                onProductClick ={
                },
                cartItems,
                paymentLauncher = paymentLauncher
                )
        }
        "profile" -> {
            ProfileScreen(
                onHomeClick = {
                    currentScreen ="home"
                },
                onCartClick = {
                    currentScreen ="cart"
                }
            )

        }
        "search" -> {

            SearchResultsScreen(

                onHomeClick = {
                    currentScreen = "home"
                },

                onCartClick = {
                    currentScreen = "cart"
                },

                onProfileClick = {
                    currentScreen = "profile"
                },

                query = currentSearchQuery,

                onBack = {
                    currentScreen = "home"
                },

                onProductClick = { product ->
                    selectedProduct = product
                }

            )

        }
    }
}