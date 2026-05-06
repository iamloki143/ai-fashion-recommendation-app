package com.example.dressbrand.ui.screen.homescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.room.Database
import com.example.dressbrand.data.Product
import com.example.dressbrand.data.room.DatabaseProvider
import com.example.dressbrand.network.RetrofitInstance
import com.example.dressbrand.data.room.entity.SearchHistory
import com.example.dressbrand.ui.theme.BabyPink
import com.example.dressbrand.ui.theme.BelligoesFont
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCartClick:() -> Unit,
    onProfileClick:() -> Unit,
    onProductClick:(Product) -> Unit,
    onSearchClick:(String)->Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedTab by remember {
        mutableStateOf("popular")
    }
    val categories = listOf(
        "Shirts",
        "Pants",
        "Shorts",
        "Tshirts",
        "Dresses",
        "Hoodies"
    )
    val context= LocalContext.current

    val scope= rememberCoroutineScope ()
    val searchDao = DatabaseProvider.getDatabase(context).SearchHistoryDao()
    var popularProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var recommendedProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    LaunchedEffect(searchText) {
        if (searchText.isNotBlank()){
            recommendedProducts= RetrofitInstance.api.getRecommendedProducts(searchText)
        }
    }
    LaunchedEffect(Unit) {
        try {
            popularProducts= RetrofitInstance.api.getPopularProducts()
            val frequentQuery = searchDao.getMostFrequentSearch()
            if (frequentQuery!=null){
                recommendedProducts= RetrofitInstance.api.getRecommendedProducts(frequentQuery)
            }

        }
        catch (e: Exception){
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
                    onClick = { },
                    label = {
                        Text("Home")
                    },
                    icon = {Icon(Icons.Default.Home, contentDescription = "Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onCartClick()
                    },
                    label = {
                        Text("Cart")
                    },
                    icon = {Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onProfileClick()
                    },
                    label = {
                        Text("Profile")
                    },
                    icon = {Icon(Icons.Default.Person, contentDescription = "Profile")}
                )
            }
        }
    ) {padding ->
        Column (
            modifier = Modifier.padding(padding).fillMaxSize()
        ){
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {searchText=it},
                    placeholder = {
                        Text("Search Product")
                    },
                    shape = RoundedCornerShape(28.dp),
                    singleLine = true,
                    modifier = Modifier.weight(1f).height(48.dp)
                )
                IconButton(
                    onClick = {
                        if (searchText.isNotBlank()){
                            scope.launch {
                                searchDao.insertSearch(
                                    SearchHistory(
                                        searchText=searchText
                                    )
                                )
                            }
                            onSearchClick(
                                searchText)
                        }
                    }
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical=8.dp),

                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories){category ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = BabyPink
                        )
                    ) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(vertical=10.dp, horizontal = 14.dp),


                            )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if(selectedTab=="popular")
                                BabyPink
                            else
                                Color.DarkGray
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            selectedTab="popular"
                        }
                ){
                    Text(
                        "Popular",
                        modifier = Modifier.padding(
                            horizontal = 18.dp,
                            vertical = 8.dp
                        )
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if(selectedTab=="recommended")
                                BabyPink
                            else
                                Color.DarkGray
                    ),
                    modifier = Modifier
                        .clickable {
                            selectedTab="recommended"
                        }
                ){
                    Text(
                        "Recommended",
                        modifier = Modifier.padding(
                            horizontal = 18.dp,
                            vertical = 8.dp
                        )
                    )
                }
            }
            val productsToShow =
                if(selectedTab=="popular")
                    popularProducts
                else
                    recommendedProducts
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productsToShow.size){index ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = BabyPink
                        ),
                        modifier = Modifier.padding(8.dp).clickable{
                            onProductClick(
                                productsToShow[index]
                            )
                        },

                    ) {
                        ProductCard(
                            name = productsToShow[index].productName,
                            price = "₹${productsToShow[index].price}",
                            rating = productsToShow[index].rating.toString(),
                            imageUrl = productsToShow[index].imageUrl
                        )
                    }
                }
            }
        }
    }
}