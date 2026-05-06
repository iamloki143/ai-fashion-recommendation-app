package com.example.dressbrand.ui.screen.searchscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.dressbrand.data.Product
import com.example.dressbrand.network.RetrofitInstance
import com.example.dressbrand.ui.screen.homescreen.ProductCard
import com.example.dressbrand.ui.theme.BelligoesFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    onHomeClick:() -> Unit,
    onCartClick:() -> Unit,
    onProfileClick:() -> Unit,

    query:String,

    onBack:()->Unit,

    onProductClick:(Product)->Unit

) {
    BackHandler {
        onBack()
    }

    var searchResults by remember {

        mutableStateOf<List<Product>>(
            emptyList()
        )

    }

    LaunchedEffect(query) {

        try {

            searchResults =
                RetrofitInstance.api
                    .getRecommendedProducts(
                        query
                    )

        } catch (e: Exception) {

            e.printStackTrace()

        }

    }
    Scaffold(
        topBar = {

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Dress brand",
                            fontFamily = BelligoesFont,
                            fontSize = 32.sp
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        onHomeClick()
                    },
                    label = {
                        Text("Home")
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onCartClick()
                    },
                    label = {
                        Text("Cart")
                    },
                    icon = {Icon(Icons.Default.ShoppingCart,"cart") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onProfileClick()
                    },
                    label = {
                        Text("Profile")
                    },
                    icon = {Icon(Icons.Default.Person, contentDescription = "profile")}
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {

            Text("Searched Product:$query")
            Spacer(modifier = Modifier.height(6.dp))
            Text("Suggestions")
            Spacer(modifier = Modifier.height(6.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                items(searchResults) { product ->

                    Card(
                        modifier =
                            Modifier
                                .padding(8.dp)
                                .clickable {

                                    onProductClick(
                                        product
                                    )

                                }
                    ) {

                        ProductCard(
                            name = product.productName,
                            price = "₹${product.price}",
                            rating = product.rating.toString(),
                            imageUrl = product.imageUrl
                        )

                    }

                }

            }

        }
    }
}

