package com.hgm.googlesignin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hgm.googlesignin.ui.theme.GoogleSignInTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object SignInRoute

@Serializable
data object DetailRoute

class MainActivity : ComponentActivity() {
      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                  GoogleSignInTheme {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                              val context = LocalContext.current
                              val scope = rememberCoroutineScope()
                              val navController = rememberNavController()
                              val googleSignInManager = remember {
                                    GoogleSignInManager(context as ComponentActivity)
                              }
                              NavHost(
                                    navController = navController,
                                    startDestination = SignInRoute,
                                    modifier = Modifier
                                          .fillMaxSize()
                                          .padding(innerPadding)
                              ) {
                                    composable<SignInRoute> {
                                          Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                          ) {
                                                OutlinedButton(
                                                      onClick = {
                                                            scope.launch {
                                                                  val result = googleSignInManager.signIn()
                                                                  val text = when (result) {
                                                                        is SignInResult.Cancelled -> "Cancelled"
                                                                        is SignInResult.ErrorTypeCredentials -> "ErrorTypeCredentials"
                                                                        is SignInResult.Failure -> "Failure"
                                                                        is SignInResult.NoCredentials -> "NoCredentials"
                                                                        is SignInResult.Success -> {
                                                                              navController.navigate(DetailRoute){
                                                                                    popUpTo<SignInRoute> {
                                                                                          inclusive = true
                                                                                    }
                                                                              }
                                                                              "Success --> ${result.id} - ${result.username} - ${result.avatarUrl}"
                                                                        }
                                                                  }
                                                                  Log.e("GoogleSignIn", text)
                                                            }
                                                      }
                                                ) {
                                                      Text(text = "Google Sign In")
                                                }
                                          }
                                    }

                                    composable<DetailRoute> {
                                          Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                          ) {
                                                Text(text = "Welcome ~")
                                          }
                                    }
                              }
                        }
                  }
            }
      }
}
