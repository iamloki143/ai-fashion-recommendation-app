package com.example.dressbrand.ui.screen.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dressbrand.ui.theme.DimSteel
import com.example.dressbrand.ui.theme.IvoryWhite
import com.example.dressbrand.ui.theme.LuxeGold
import com.example.dressbrand.ui.theme.RichCharcoal
import com.example.dressbrand.ui.theme.SilverMist

@Composable
fun ProductCard(
    name: String,
    price: String,
    rating: String,
    imageUrl: String
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .background(RichCharcoal, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
    ) {
        // Image container with subtle overlay gradient
        Box {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Details
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = name,
                color = IvoryWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = price,
                color = LuxeGold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.3.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = LuxeGold,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = rating,
                    color = SilverMist,
                    fontSize = 12.sp
                )
            }
        }
    }
}