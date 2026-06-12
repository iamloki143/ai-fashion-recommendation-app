package com.example.dressbrand.ui.screen.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dressbrand.data.Product
import com.example.dressbrand.data.room.DatabaseProvider
import com.example.dressbrand.data.room.entity.SearchHistory
import com.example.dressbrand.network.RetrofitInstance
import com.example.dressbrand.ui.theme.BelligoesFont
import com.example.dressbrand.ui.theme.DeepObsidian
import com.example.dressbrand.ui.theme.DimSteel
import com.example.dressbrand.ui.theme.ElevatedSurface
import com.example.dressbrand.ui.theme.IvoryWhite
import com.example.dressbrand.ui.theme.LuxeGold
import com.example.dressbrand.ui.theme.MutedGold
import com.example.dressbrand.ui.theme.RichCharcoal
import com.example.dressbrand.ui.theme.SilverMist
import com.example.dressbrand.ui.theme.SubtleBorder
import com.example.dressbrand.utils.ProductCardSkeleton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,
    onProductClick: (Product) -> Unit,
    onSearchClick: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("popular") }

    val categories = listOf("Shirts", "Pants", "Shorts", "T-Shirts", "Dresses", "Hoodies")
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val searchDao = DatabaseProvider.getDatabase(context).SearchHistoryDao()

    var popularProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var recommendedProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(searchText) {
        if (searchText.isNotBlank()) {
            recommendedProducts = RetrofitInstance.api.getRecommendedProducts(searchText)
        }
    }
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            popularProducts =
                RetrofitInstance.api
                    .getPopularProducts()
            val frequentQuery =
                searchDao.getMostFrequentSearch()
            if (frequentQuery != null) {
                recommendedProducts =
                    RetrofitInstance.api
                        .getRecommendedProducts(frequentQuery)
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, LuxeGold, Color.Transparent)
                    ),
                    shape = RoundedCornerShape(0.dp)
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = RichCharcoal,
                tonalElevation = 0.dp,
                modifier = Modifier.border(
                    width = 0.5.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, LuxeGold, Color.Transparent)
                    ),
                    shape = RoundedCornerShape(0.dp)
                )
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    label = { Text("Home", fontSize = 10.sp, letterSpacing = 1.sp) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(20.dp)) },
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
                    onClick = { onCartClick() },
                    label = { Text("Cart", fontSize = 10.sp, letterSpacing = 1.sp) },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", modifier = Modifier.size(20.dp)) },
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
                    onClick = { onProfileClick() },
                    label = { Text("Profile", fontSize = 10.sp, letterSpacing = 1.sp) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(20.dp)) },
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
                .padding(padding)
                .fillMaxSize()
                .background(DeepObsidian)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = {
                        Text(
                            "Search products…",
                            color = DimSteel,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LuxeGold,
                        unfocusedBorderColor = SubtleBorder,
                        focusedTextColor = IvoryWhite,
                        unfocusedTextColor = IvoryWhite,
                        cursorColor = LuxeGold,
                        focusedContainerColor = ElevatedSurface,
                        unfocusedContainerColor = RichCharcoal
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(LuxeGold, RoundedCornerShape(8.dp))
                        .clickable {
                            if (searchText.isNotBlank()) {
                                scope.launch {
                                    searchDao.insertSearch(SearchHistory(searchText = searchText))
                                }
                                onSearchClick(searchText)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = DeepObsidian,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    Box(
                        modifier = Modifier
                            .border(
                                width = 0.8.dp,
                                color = SubtleBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .background(RichCharcoal, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category.uppercase(),
                            color = SilverMist,
                            fontSize = 10.sp,
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("popular" to "POPULAR", "recommended" to "FOR YOU").forEach { (key, label) ->
                    val isSelected = selectedTab == key
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (isSelected) LuxeGold else RichCharcoal,
                                RoundedCornerShape(6.dp)
                            )
                            .border(
                                width = if (isSelected) 0.dp else 0.8.dp,
                                color = if (isSelected) Color.Transparent else SubtleBorder,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { selectedTab = key }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) DeepObsidian else SilverMist,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val productsToShow = if (selectedTab == "popular") popularProducts else recommendedProducts

            if(isLoading){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(6){
                        ProductCardSkeleton()
                    }
                }
            }else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(productsToShow.size) { index ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onProductClick(productsToShow[index]) }
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
}