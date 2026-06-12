package com.example.dressbrand.ui.screen.homescreen

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.dressbrand.data.CartItem
import com.example.dressbrand.data.Product
import com.example.dressbrand.data.room.DatabaseProvider
import com.example.dressbrand.data.room.entity.CartProduct
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
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    cartItems: MutableList<CartItem>,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val cartDao = DatabaseProvider.getDatabase(context).CartDao()
    var showCartDialog by remember { mutableStateOf(false) }
    var selectedSize by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(1) }

    val sizeStock = mapOf(
        "XS" to product.sizeXS,
        "S" to product.sizeS,
        "M" to product.sizeM,
        "L" to product.sizeL,
        "XL" to product.sizeXL
    )
    var imageLoading by remember {
        mutableStateOf(true)
    }

    Scaffold(
        containerColor = DeepObsidian,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepObsidian,
                    titleContentColor = IvoryWhite,
                    navigationIconContentColor = IvoryWhite
                ),
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = IvoryWhite
                        )
                    }
                },
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(DeepObsidian)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .background(RichCharcoal)
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build()
                )

                imageLoading =
                    painter.state is AsyncImagePainter.State.Loading

                Box {

                    if (imageLoading) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmer()
                                .background(
                                    ElevatedSurface
                                )
                        )

                    }

                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.productName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, DeepObsidian)
                            )
                        )
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.productName,
                    color = IvoryWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "₹${product.price}",
                        color = LuxeGold,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = LuxeGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = product.rating.toString(),
                            color = SilverMist,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = SubtleBorder, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "AVAILABLE SIZES",
                    color = DimSteel,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(sizeStock.toList()) { (size, stock) ->
                        SizeButton(size = size, stock = stock)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LuxeGold, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { showCartDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = DeepObsidian
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = null
                    ) {
                        Text(
                            "ADD TO BAG",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 3.sp,
                            color = DeepObsidian
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (showCartDialog) {
            AlertDialog(
                onDismissRequest = { showCartDialog = false },
                containerColor = RichCharcoal,
                shape = RoundedCornerShape(16.dp),
                title = {
                    Column {
                        Text(
                            "ADD TO BAG",
                            color = IvoryWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Divider(color = LuxeGold, thickness = 0.8.dp)
                    }
                },
                text = {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(ElevatedSurface, RoundedCornerShape(10.dp))
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = product.productName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(product.productName, color = IvoryWhite, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("₹${product.price}", color = LuxeGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = LuxeGold, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(product.rating.toString(), color = SilverMist, fontSize = 12.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("SELECT SIZE", color = DimSteel, fontSize = 9.sp, letterSpacing = 2.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(sizeStock.keys.toList()) { size ->
                                val stock = sizeStock[size] ?: 0
                                val isSelected = selectedSize == size
                                Box(
                                    modifier = Modifier
                                        .background(
                                            when {
                                                stock == 0 -> ElevatedSurface
                                                isSelected -> LuxeGold
                                                else -> ElevatedSurface
                                            },
                                            RoundedCornerShape(6.dp)
                                        )
                                        .border(
                                            width = if (isSelected) 0.dp else 0.8.dp,
                                            color = if (stock == 0) SubtleBorder else if (isSelected) Color.Transparent else SubtleBorder,
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .clip(RoundedCornerShape(6.dp))
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Button(
                                        enabled = stock > 0,
                                        onClick = {
                                            selectedSize = size
                                            if (quantity > stock) quantity = stock
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent,
                                            contentColor = if (isSelected) DeepObsidian else IvoryWhite,
                                            disabledContainerColor = Color.Transparent,
                                            disabledContentColor = DimSteel
                                        ),
                                        elevation = null,
                                        modifier = Modifier.size(width = 50.dp, height = 36.dp),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            if (stock == 0) "$size✕" else size,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                }
                            }
                        }
                        if (selectedSize.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Selected: $selectedSize  ·  ${sizeStock[selectedSize] ?: 0} left",
                                color = MutedGold,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("QUANTITY", color = DimSteel, fontSize = 9.sp, letterSpacing = 2.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(ElevatedSurface, RoundedCornerShape(6.dp))
                                    .border(0.5.dp, SubtleBorder, RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = { if (quantity > 1) quantity-- },
                                    modifier = Modifier.size(36.dp)
                                ) { Text("−", color = IvoryWhite, fontSize = 18.sp) }
                            }
                            Text(
                                text = quantity.toString(),
                                color = IvoryWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(ElevatedSurface, RoundedCornerShape(6.dp))
                                    .border(0.5.dp, SubtleBorder, RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = {
                                        if (quantity < (sizeStock[selectedSize] ?: 1)) quantity++
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) { Text("+", color = IvoryWhite, fontSize = 18.sp) }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (selectedSize.isNotEmpty()) {
                                val existingItem = cartItems.find {
                                    it.productId == product.productId && it.selectedSize == selectedSize
                                }
                                if (existingItem != null) {
                                    existingItem.quantity += quantity
                                } else {
                                    cartItems.add(
                                        CartItem(
                                            productId = product.productId,
                                            productName = product.productName,
                                            imageUrl = product.imageUrl,
                                            price = product.price,
                                            quantity = quantity,
                                            selectedSize = selectedSize
                                        )
                                    )
                                    scope.launch {
                                        cartDao.insertCartItem(
                                            CartProduct(
                                                productId = product.productId,
                                                productName = product.productName,
                                                imageUrl = product.imageUrl,
                                                price = product.price,
                                                quantity = quantity,
                                                selectedSize = selectedSize
                                            )
                                        )
                                    }
                                }
                                showCartDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LuxeGold,
                            contentColor = DeepObsidian
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedSize.isNotEmpty()
                    ) {
                        Text(
                            "CONFIRM",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SizeButton(size: String, stock: Int) {
    val available = stock > 0
    Box(
        modifier = Modifier
            .background(
                if (available) ElevatedSurface else RichCharcoal,
                RoundedCornerShape(8.dp)
            )
            .border(
                width = 0.8.dp,
                color = if (available) SubtleBorder else SubtleBorder.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = size,
                color = if (available) IvoryWhite else DimSteel,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            if (!available) {
                Text(
                    "OUT",
                    color = SoftCrimson,
                    fontSize = 8.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    "$stock left",
                    color = DimSteel,
                    fontSize = 9.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}