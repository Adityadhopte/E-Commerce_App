package com.example.ecommerceapp.ui.feature.home


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.domain.model.Product
import com.example.ecommerceapp.R
import com.example.ecommerceapp.model.UiProductModel
import com.example.ecommerceapp.navigation.NavRoutes
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsState()

    val loading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf<String?>(null) }
    val features = remember { mutableStateOf(emptyList<Product>()) }
    val popularProducts = remember { mutableStateOf(emptyList<Product>()) }
    val categories = remember { mutableStateOf(emptyList<String>()) }

    when (val state = uiState.value) {
        is HomeViewModel.HomeScreenUIEvents.Loading -> {
            loading.value = true
            error.value = null
        }

        is HomeViewModel.HomeScreenUIEvents.Success -> {
            loading.value = false
            error.value = null
            features.value = state.features
            popularProducts.value = state.popularProducts
            categories.value = state.categories
        }

        is HomeViewModel.HomeScreenUIEvents.Error -> {
            loading.value = false
            error.value = state.message
        }
    }

    HomeContent(
        features = features.value,
        popularProducts = popularProducts.value,
        categories = categories.value,
        isloading = loading.value,
        error = error.value,
        onClick = { product ->

            navController.navigate(NavRoutes.ProductDetails(UiProductModel.fromProduct(product)))

        }


    )
}

@Composable
fun ProfileHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 16.dp + WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "John Doe",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.notification),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterEnd)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.3f))
                .padding(6.dp),
            contentScale = ContentScale.Inside
        )
    }
}

@Composable
fun HomeContent(
    features: List<Product>,
    popularProducts: List<Product>,
    categories: List<String>,
    isloading: Boolean = false,
    error: String? = null,
    onClick: (Product) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Profile Header
        item {
            ProfileHeader()
            Spacer(modifier = Modifier.size(16.dp))

            // Search Bar
            val searchText = remember { mutableStateOf("") }
            SearchBar(
                value = searchText.value,
                onTextChanged = { searchText.value = it }
            )
            Spacer(modifier = Modifier.size(16.dp))
        }

        // Categories Section
        if (categories.isNotEmpty()) {
            item {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.size(8.dp))

                var isCategoriesVisible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    isCategoriesVisible = true
                }

                AnimatedVisibility(
                    visible = isCategoriesVisible,
                    enter = fadeIn() + slideInHorizontally(initialOffsetX = { -300 }) +
                            scaleIn(initialScale = 0.8f)
                ) {
                    LazyRow {
                        items(categories) { category ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(MaterialTheme.colorScheme.onPrimary)
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = category.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

        // Error Message
        if (error != null) {
            item {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

        // Loading Indicator
        if (isloading) {
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Features Section
        if (features.isNotEmpty()) {
            item {
                var isFeaturesVisible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    isFeaturesVisible = true
                }

                AnimatedVisibility(
                    visible = isFeaturesVisible,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { 300 }) +
                            scaleIn(initialScale = 0.8f)
                ) {
                    HomeProductRow(products = features, title = "Featured", onClick = onClick)
                }
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

        // Popular Products Section
        if (popularProducts.isNotEmpty()) {
            item {
                var isPopularProductsVisible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    isPopularProductsVisible = true
                }

                AnimatedVisibility(
                    visible = isPopularProductsVisible,
                    enter = fadeIn() + slideInHorizontally(initialOffsetX = { 300 }) +
                            scaleIn(initialScale = 0.8f)
                ) {
                    HomeProductRow(products = popularProducts, title = "Popular Products", onClick = onClick)
                }
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun SearchBar(value: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onTextChanged,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
        ),
        placeholder = {
            Text(
                text = "Search for products",
                style = MaterialTheme.typography.bodySmall
            )
        }
    )
}

@Composable
fun HomeProductRow(products: List<Product>, title: String, onClick: (Product) -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyRow {
            items(products) { product ->
                ProductItem(product = product, onClick = onClick)  // Pass onClick here
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: (Product) -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(initialOffsetX = { 200 }, animationSpec = tween(durationMillis = 300)) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = { -200 }, animationSpec = tween(durationMillis = 300)) + fadeOut()
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .size(width = 126.dp, height = 144.dp)
                .clickable { onClick(product) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = product.image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
