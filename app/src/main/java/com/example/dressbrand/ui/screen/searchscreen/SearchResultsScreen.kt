package com.example.dressbrand.ui.screen.searchscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dressbrand.data.Product
import com.example.dressbrand.network.RetrofitInstance
import com.example.dressbrand.ui.screen.homescreen.ProductCard
import com.example.dressbrand.ui.theme.BelligoesFont
import com.example.dressbrand.ui.theme.DeepObsidian
import com.example.dressbrand.ui.theme.DimSteel
import com.example.dressbrand.ui.theme.ElevatedSurface
import com.example.dressbrand.ui.theme.IvoryWhite
import com.example.dressbrand.ui.theme.LuxeGold
import com.example.dressbrand.ui.theme.RichCharcoal
import com.example.dressbrand.ui.theme.SilverMist
import com.example.dressbrand.ui.theme.SubtleBorder
import com.example.dressbrand.utils.ProductCardSkeleton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(

    onHomeClick: () -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,

    query: String,

    onBack: () -> Unit,

    onProductClick: (Product) -> Unit

) {

    BackHandler {
        onBack()
    }

    var searchResults by remember {

        mutableStateOf<List<Product>>(
            emptyList()
        )

    }
    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(query) {

        isLoading = true

        try {

            searchResults =
                RetrofitInstance.api
                    .getRecommendedProducts(query)

        } finally {

            isLoading = false

        }
    }

    Scaffold(

        containerColor = DeepObsidian,

        topBar = {

            TopAppBar(

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepObsidian,
                    titleContentColor = IvoryWhite
                ),

                title = {

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "DRESS BRAND",
                            fontFamily = BelligoesFont,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Light,
                            color = IvoryWhite,
                            letterSpacing = 6.sp
                        )

                    }

                },

                modifier = Modifier.border(
                    width = 0.5.dp,
                    color = LuxeGold.copy(alpha = 0.4f)
                )

            )

        },

        bottomBar = {

            NavigationBar(

                containerColor = RichCharcoal,

                tonalElevation = 0.dp,

                modifier = Modifier.border(
                    width = 0.5.dp,
                    color = LuxeGold.copy(alpha = 0.4f)
                )

            ) {

                NavigationBarItem(

                    selected = true,

                    onClick = {
                        onHomeClick()
                    },

                    label = {

                        Text(
                            "Home",
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )

                    },

                    icon = {

                        Icon(
                            Icons.Default.Home,
                            contentDescription = "home",
                            modifier = Modifier.size(20.dp)
                        )

                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LuxeGold,
                        selectedTextColor = LuxeGold,
                        indicatorColor = ElevatedSurface,
                        unselectedIconColor = DimSteel,
                        unselectedTextColor = DimSteel
                    )

                )

                NavigationBarItem(

                    selected = false,

                    onClick = {
                        onCartClick()
                    },

                    label = {

                        Text(
                            "Cart",
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )

                    },

                    icon = {

                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "cart",
                            modifier = Modifier.size(20.dp)
                        )

                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LuxeGold,
                        selectedTextColor = LuxeGold,
                        indicatorColor = ElevatedSurface,
                        unselectedIconColor = DimSteel,
                        unselectedTextColor = DimSteel
                    )

                )

                NavigationBarItem(

                    selected = false,

                    onClick = {
                        onProfileClick()
                    },

                    label = {

                        Text(
                            "Profile",
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )

                    },

                    icon = {

                        Icon(
                            Icons.Default.Person,
                            contentDescription = "profile",
                            modifier = Modifier.size(20.dp)
                        )

                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LuxeGold,
                        selectedTextColor = LuxeGold,
                        indicatorColor = ElevatedSurface,
                        unselectedIconColor = DimSteel,
                        unselectedTextColor = DimSteel
                    )

                )

            }

        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(DeepObsidian)

        ) {

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Column(
                modifier = Modifier.padding(horizontal = 18.dp)
            ) {

                Text(
                    text = "SEARCH RESULTS",
                    color = IvoryWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = query,
                    color = LuxeGold,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Box(

                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            RichCharcoal,
                            RoundedCornerShape(12.dp)
                        )
                        .border(
                            0.5.dp,
                            SubtleBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(
                            horizontal = 14.dp,
                            vertical = 12.dp
                        )

                ) {

                    Text(
                        text = "${searchResults.size} products found",
                        color = SilverMist,
                        fontSize = 12.sp
                    )

                }

            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )
            if (isLoading){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),

                    horizontalArrangement = Arrangement.spacedBy(10.dp),

                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(6){
                        ProductCardSkeleton()
                    }
                }
            }

            else if (searchResults.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "No Products Found",
                            color = IvoryWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Try searching something else",
                            color = SilverMist,
                            fontSize = 13.sp
                        )

                    }

                }

            } else {

                LazyVerticalGrid(

                    columns = GridCells.Fixed(2),

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),

                    horizontalArrangement = Arrangement.spacedBy(10.dp),

                    verticalArrangement = Arrangement.spacedBy(10.dp)

                ) {

                    items(searchResults) { product ->

                        Box(

                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
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

}