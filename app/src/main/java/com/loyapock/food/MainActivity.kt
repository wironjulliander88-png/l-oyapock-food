package com.loyapock.food

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

private val Green = Color(0xFF087A3E)
private val DarkGreen = Color(0xFF005A2C)
private val Yellow = Color(0xFFFFD51B)
private val Red = Color(0xFFE91B2B)

data class Product(
    val name: String,
    val category: String,
    val price: Double
)

private val products = listOf(
    Product("Poulet braisé et riz", "Repas", 12.90),
    Product("Poisson grillé", "Repas", 13.50),
    Product("Riz sauté aux crevettes", "Repas", 14.90),
    Product("Burger Maison", "Repas", 9.50),
    Product("Salade fraîche", "Repas", 7.90),
    Product("Jus tropical", "Boissons", 3.50),
    Product("Gâteau chocolat", "Desserts", 4.50)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LoyapockApp() }
    }
}

@Composable
fun LoyapockApp() {
    var screen by remember { mutableStateOf("Accueil") }
    var cart by remember { mutableStateOf(mapOf<Product, Int>()) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Green,
            secondary = Yellow,
            error = Red
        )
    ) {
        Scaffold(
            topBar = {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Green)
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Text(
                            "L’Oyapock Food",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        Modifier.fillMaxWidth().background(Yellow).padding(8.dp)
                    ) {
                        Text(
                            "Saint-Georges-de-l’Oyapock • Livraison 5 € • 20–40 min",
                            color = DarkGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            bottomBar = {
                NavigationBar {
                    listOf("Accueil", "Menu", "Panier", "Commandes", "Compte").forEach { item ->
                        NavigationBarItem(
                            selected = screen == item,
                            onClick = { screen = item },
                            icon = { Text(item.take(1)) },
                            label = { Text(item) }
                        )
                    }
                }
            }
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                when (screen) {
                    "Accueil" -> HomeScreen(
                        onMenu = { screen = "Menu" },
                        cartCount = cart.values.sum()
                    )
                    "Menu" -> MenuScreen(
                        cart = cart,
                        onAdd = { product ->
                            cart = cart.toMutableMap().apply {
                                this[product] = (this[product] ?: 0) + 1
                            }
                        }
                    )
                    "Panier" -> CartScreen(
                        cart = cart,
                        onCheckout = { screen = "Checkout" }
                    )
                    "Checkout" -> CheckoutScreen(
                        cart = cart,
                        onDone = { screen = "Commandes" }
                    )
                    "Commandes" -> OrdersScreen()
                    else -> AccountScreen()
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(onMenu: () -> Unit, cartCount: Int) {
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            "Bienvenue chez L’Oyapock Food",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text("Commandez vos repas et produits préférés à Saint-Georges-de-l’Oyapock.")
        Card {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Livraison locale", fontWeight = FontWeight.Bold)
                Text("5 € de livraison • 20–40 min")
                Text("Paiement en ligne ou à la livraison")
            }
        }
        Button(onClick = onMenu, modifier = Modifier.fillMaxWidth()) {
            Text("Voir le menu")
        }
        if (cartCount > 0) {
            Text("Votre panier contient $cartCount article(s).")
        }
    }
}

@Composable
private fun MenuScreen(cart: Map<Product, Int>, onAdd: (Product) -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Notre menu", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        items(products) { product ->
            Card {
                Row(
                    Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(product.name, fontWeight = FontWeight.SemiBold)
                        Text(product.category)
                        Text(String.format(Locale.FRANCE, "%.2f €", product.price))
                    }
                    Button(onClick = { onAdd(product) }) {
                        Text("Ajouter")
                    }
                }
            }
        }
    }
}

@Composable
private fun CartScreen(cart: Map<Product, Int>, onCheckout: () -> Unit) {
    val subtotal = cart.entries.sumOf { it.key.price * it.value }
    val total = if (cart.isEmpty()) 0.0 else subtotal + 5.0
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Panier", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        if (cart.isEmpty()) {
            Text("Votre panier est vide.")
        } else {
            cart.forEach { (product, qty) ->
                Text("$qty × ${product.name} — ${String.format(Locale.FRANCE, "%.2f €", product.price * qty)}")
            }
            HorizontalDivider()
            Text("Sous-total : ${String.format(Locale.FRANCE, "%.2f €", subtotal)}")
            Text("Livraison : 5,00 €")
            Text("Total : ${String.format(Locale.FRANCE, "%.2f €", total)}", fontWeight = FontWeight.Bold)
            Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth()) {
                Text("Passer la commande")
            }
        }
    }
}

@Composable
private fun CheckoutScreen(cart: Map<Product, Int>, onDone: () -> Unit) {
    var payment by remember { mutableStateOf("Paiement à la livraison") }
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Finaliser la commande", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Adresse : Saint-Georges-de-l’Oyapock")
        Text("Livraison : 5 €")
        Text("20–40 min")
        Text("Mode de paiement")
        listOf("Paiement à la livraison", "Paiement en ligne").forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = payment == option, onClick = { payment = option })
                Text(option)
            }
        }
        Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) {
            Text("Confirmer la commande")
        }
    }
}

@Composable
private fun OrdersScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Mes commandes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Card {
            Column(Modifier.padding(16.dp)) {
                Text("Suivi des commandes")
                Text("Vos commandes apparaîtront ici.")
            }
        }
    }
}

@Composable
private fun AccountScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Mon compte", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Connexion et informations client")
        Text("Cette section sera reliée au compte client réel avant la mise en production.")
    }
}
