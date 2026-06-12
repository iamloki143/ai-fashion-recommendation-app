package com.example.dressbrand.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dressbrand.ui.theme.RichCharcoal
import com.valentinilk.shimmer.shimmer

@Composable
fun ProductCardSkeleton() {

    Column(
        modifier = Modifier
            .padding(4.dp)
            .background(
                RichCharcoal,
                RoundedCornerShape(12.dp)
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .shimmer()
                .background(
                    Color.Gray.copy(alpha = 0.3f)
                )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(0.8f)
                .height(18.dp)
                .shimmer()
                .background(
                    Color.Gray.copy(alpha = 0.3f)
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .width(80.dp)
                .height(16.dp)
                .shimmer()
                .background(
                    Color.Gray.copy(alpha = 0.3f)
                )
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}