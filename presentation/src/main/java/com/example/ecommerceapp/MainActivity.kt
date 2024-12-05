package com.example.ecommerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.domain.model.Product
import com.example.ecommerceapp.model.UiProductModel
import com.example.ecommerceapp.navigation.NavRoutes
import com.example.ecommerceapp.navigation.ProductNavType
import com.example.ecommerceapp.ui.feature.home.HomeScreen
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcommerceAppTheme   {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(navController)
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        NavHost(navController = navController, startDestination = "home") {
                            composable("home") { // Use the same string route to navigate to HomeScreen
                                HomeScreen(navController)
                            }
                            composable("cart") {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(text = "Cart")
                                }
                            }
                            composable("profile") {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(text = "Profile")
                                }
                            }
                            composable("notification") {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(text = "Notification")
                                }
                            }

                            composable<NavRoutes.ProductDetails>(
                                typeMap = mapOf(typeOf<UiProductModel>()to ProductNavType)
                            ) {
                                val productRoute = it.toRoute<NavRoutes.ProductDetails>()
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(text = productRoute.product.title )
                                }
                            }
                        }

                    }

                        }
                    }
                }

            }
        }


@Composable
fun BottomNavigationBar(navController: NavController) {
    // List of bottom navigation items
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val items = listOf(
        BottomNavItems.Home,
        BottomNavItems.Cart,
        BottomNavItems.Profile,
        BottomNavItems.Notification
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->

            val isSelected = currentRoute == item.route

            val scale = animateFloatAsState(targetValue = if (isSelected) 1.2f else 1f)

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { startRoute ->
                            popUpTo(startRoute) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    // Text with color change based on selection state
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    )
                },
                icon = {
                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(24.dp * scale.value) // Apply scaling to icon size
                            .padding(4.dp),
                        colorFilter = ColorFilter.tint(
                            if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}

sealed class BottomNavItems(val route: String, val title: String, val icon: Int) {
    object Home : BottomNavItems("home", "Home", icon = R.drawable.ic_home)
    object Cart : BottomNavItems("cart", "Cart", icon = R.drawable.ic_cart)
    object Profile : BottomNavItems("profile", "Profile", icon = R.drawable.ic_profile)
    object Notification : BottomNavItems("notification", "Notification", icon = R.drawable.notification)
}
