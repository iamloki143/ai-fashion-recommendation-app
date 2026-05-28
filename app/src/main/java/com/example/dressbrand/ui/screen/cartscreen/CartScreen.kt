package com.example.dressbrand.ui.screen.cartscreen

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dressbrand.data.CartItem
import com.example.dressbrand.data.room.DatabaseProvider
import com.example.dressbrand.data.room.entity.PurchaseHistory
import com.example.dressbrand.ui.theme.BelligoesFont
import com.example.dressbrand.ui.theme.DeepObsidian
import com.example.dressbrand.ui.theme.DimSteel
import com.example.dressbrand.ui.theme.ElevatedSurface
import com.example.dressbrand.ui.theme.IvoryWhite
import com.example.dressbrand.ui.theme.LuxeGold
import com.example.dressbrand.ui.theme.MutedGold
import com.example.dressbrand.ui.theme.RichCharcoal
import com.example.dressbrand.ui.theme.SilverMist
import com.example.dressbrand.ui.theme.SoftCrimson
import com.example.dressbrand.ui.theme.SubtleBorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onProductClick: () -> Unit,
    cartItems: MutableList<CartItem>,
    paymentLauncher: ActivityResultLauncher<Intent>
) {
    val purchaseGroupId = System.currentTimeMillis()
    val context = LocalContext.current
    var showCheckoutDialog by remember { mutableStateOf(false) }
    val totalAmount = cartItems.sumOf { it.price * it.quantity }

    Scaffold(
        containerColor = DeepObsidian,
        floatingActionButton = {
            if (cartItems.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { showCheckoutDialog = true },
                    containerColor = LuxeGold,
                    contentColor = DeepObsidian,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Checkout", modifier = Modifier.size(22.dp))
                }
            }
        },
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
                    selected = false,
                    onClick = { onHomeClick() },
                    label = { Text("Home", fontSize = 10.sp, letterSpacing = 1.sp) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(20.dp)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LuxeGold, selectedTextColor = LuxeGold,
                        indicatorColor = ElevatedSurface,
                        unselectedIconColor = DimSteel, unselectedTextColor = DimSteel
                    )
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    label = { Text("Cart", fontSize = 10.sp, letterSpacing = 1.sp) },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", modifier = Modifier.size(20.dp)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LuxeGold, selectedTextColor = LuxeGold,
                        indicatorColor = ElevatedSurface,
                        unselectedIconColor = DimSteel, unselectedTextColor = DimSteel
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onProfileClick() },
                    label = { Text("Profile", fontSize = 10.sp, letterSpacing = 1.sp) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(20.dp)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LuxeGold, selectedTextColor = LuxeGold,
                        indicatorColor = ElevatedSurface,
                        unselectedIconColor = DimSteel, unselectedTextColor = DimSteel
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
            // Header section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "MY BAG",
                    color = IvoryWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp
                )
                Text(
                    text = "${cartItems.size} item${if (cartItems.size != 1) "s" else ""}",
                    color = SilverMist,
                    fontSize = 12.sp
                )
            }

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = SubtleBorder,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Your bag is empty", color = DimSteel, fontSize = 14.sp, letterSpacing = 1.sp)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    cartItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(RichCharcoal, RoundedCornerShape(12.dp))
                                .border(0.5.dp, SubtleBorder, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Product image
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = item.productName,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ElevatedSurface),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.productName,
                                    color = IvoryWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "SIZE  ${item.selectedSize}",
                                    color = DimSteel,
                                    fontSize = 10.sp,
                                    letterSpacing = 1.5.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "₹${item.price}",
                                    color = LuxeGold,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                // Quantity row
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(ElevatedSurface, RoundedCornerShape(4.dp))
                                            .border(0.5.dp, SubtleBorder, RoundedCornerShape(4.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        IconButton(
                                            onClick = { if (item.quantity > 1) item.quantity-- },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Text("−", color = IvoryWhite, fontSize = 16.sp)
                                        }
                                    }
                                    Text(
                                        text = item.quantity.toString(),
                                        color = IvoryWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 14.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(ElevatedSurface, RoundedCornerShape(4.dp))
                                            .border(0.5.dp, SubtleBorder, RoundedCornerShape(4.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        IconButton(
                                            onClick = { item.quantity++ },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Text("+", color = IvoryWhite, fontSize = 16.sp)
                                        }
                                    }
                                }
                            }
                            // Delete button
                            IconButton(
                                onClick = { cartItems.remove(item) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .align(Alignment.Top)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Remove",
                                    tint = SoftCrimson,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(80.dp)) // space for FAB
                }
            }

            // Order total bar
            if (cartItems.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(RichCharcoal)
                        .border(
                            width = 0.5.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, LuxeGold, Color.Transparent)
                            ),
                            shape = RoundedCornerShape(0.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("TOTAL", color = SilverMist, fontSize = 9.sp, letterSpacing = 2.sp)
                            Text("₹$totalAmount", color = LuxeGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .background(LuxeGold, RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                                .padding(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text(
                                "CHECKOUT",
                                color = DeepObsidian,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )
                        }
                    }
                }
            }
        }

        // Checkout Dialog
        if (showCheckoutDialog) {
            AlertDialog(
                onDismissRequest = { showCheckoutDialog = false },
                containerColor = RichCharcoal,
                shape = RoundedCornerShape(16.dp),
                title = {
                    Column {
                        Text(
                            "ORDER SUMMARY",
                            color = IvoryWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Divider(color = LuxeGold, thickness = 0.8.dp)
                    }
                },
                text = {
                    Column {
                        cartItems.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.productName, color = IvoryWhite, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text("Qty ${item.quantity}  ·  Size ${item.selectedSize}", color = DimSteel, fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("₹${item.price * item.quantity}", color = LuxeGold, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                IconButton(
                                    onClick = { cartItems.remove(item) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "delete", tint = SoftCrimson, modifier = Modifier.size(16.dp))
                                }
                            }
                            Divider(color = SubtleBorder, thickness = 0.5.dp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("TOTAL", color = SilverMist, fontSize = 11.sp, letterSpacing = 2.sp)
                            Text("₹$totalAmount", color = LuxeGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                confirmButton = {
                    val ctx = LocalContext.current
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LuxeGold, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "PLACE ORDER",
                            color = DeepObsidian,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).padding(0.dp)
                        )
                    }
                    // Invisible button over the box to handle click
                    FilledTonalButton(
                        onClick = {
                            val itemToSave = cartItems.toList()
                            CoroutineScope(Dispatchers.IO).launch {
                                val purchaseDao = DatabaseProvider.getDatabase(ctx).PurchaseHistoryDao()
                                val cartDao = DatabaseProvider.getDatabase(ctx).CartDao()
                                itemToSave.forEach { item ->
                                    purchaseDao.insertPurchase(
                                        PurchaseHistory(
                                            productId = item.productId,
                                            purchaseGroupId = purchaseGroupId,
                                            productName = item.productName,
                                            quantity = item.quantity,
                                            size = item.selectedSize,
                                            totalPaid = item.quantity * item.price
                                        )
                                    )
                                }
                                cartDao.clearCart()
                            }
                            cartItems.clear()
                            showCheckoutDialog = false
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {}
                }
            )
        }
    }
}