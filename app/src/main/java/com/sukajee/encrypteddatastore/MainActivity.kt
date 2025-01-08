package com.sukajee.encrypteddatastore

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import com.sukajee.encrypteddatastore.ui.theme.EncryptedDataStoreTheme
import kotlinx.coroutines.launch

private val Context.dataStore by dataStore(
    fileName = "user-preferences",
    serializer = UserPreferencesSerializer
)

private val SECRET_TOKEN = (1..100)
    .map {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString("")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EncryptedDataStoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val scope = rememberCoroutineScope()
                        var text by remember {
                            mutableStateOf("")
                        }
                        Text(text = SECRET_TOKEN)
                        Button(
                            onClick = {
                                scope.launch {
                                    dataStore.updateData {
                                        UserPreferences(
                                            token = SECRET_TOKEN
                                        )
                                    }
                                }
                            }
                        ) {
                            Text("Save token")
                        }

                        Button(
                            onClick = {
                                scope.launch {
                                    dataStore.data.collect {
                                       it.token?.let { token ->
                                           text = token
                                       }
                                    }
                                }
                            }
                        ) {
                            Text("Retrieve token")
                        }

                        Text("Retrieved Token: $text")
                    }
                }
            }
        }
    }
}