package com.suci0008.laundry

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.suci0008.laundry.navigation.Screen
import com.suci0008.laundry.navigation.SetupNavGraph
import com.suci0008.laundry.ui.theme.LaundryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaundryTheme {
                SetupNavGraph()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var nama by rememberSaveable { mutableStateOf(TextFieldValue()) }
    var layanan by rememberSaveable { mutableStateOf("Reguler") }
    var berat by rememberSaveable { mutableStateOf(TextFieldValue()) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val layananOptions = listOf("Reguler", "Express", "Kilat")
    val hargaPerKg = when (layanan) {
        "Reguler" -> 7000
        "Express" -> 10000
        "Kilat" -> 12000
        else -> 0
    }
    val beratCucian = berat.text.toDoubleOrNull() ?: 0.0
    val totalHarga = hargaPerKg * beratCucian
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner_laundry),
            contentDescription = stringResource(R.string.banner),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text(stringResource(id = R.string.customer_name))},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded},
        ) {
            OutlinedTextField(
            value = layanan,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(id = R.string.service_type))},
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                layananOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            layanan = option
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = berat,
            onValueChange = { berat = it},
            label = { Text(text = stringResource(id = R.string.laundry_weight))},
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.submit))
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {showDialog = false },
                title = { Text(text = "Total Biaya Laundry")},
                text = {
                    Column {
                        Text(text = "Nama: ${nama.text}")
                        Text(text = "Layanan: $layanan")
                        Text(text = "Berat: $beratCucian kg")
                        Text(text = "Harga per kg: Rp$hargaPerKg")
                        Text(text = "Total: Rp${String.format("%,.0f", totalHarga)}")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDialog = false}) {
                        Text("OK")
                    }
                }
            )
        }
    }
}



@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    LaundryTheme {
        MainScreen(rememberNavController())
    }
}