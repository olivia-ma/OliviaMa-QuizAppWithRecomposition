package com.example.oliviama_quizappwithrecomposition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oliviama_quizappwithrecomposition.ui.theme.OliviaMaQuizAppWithRecompositionTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OliviaMaQuizAppWithRecompositionTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues())) { innerPadding ->
                    Quiz(modifier =
                    Modifier.displayCutoutPadding().padding(innerPadding))
                }
            }
        }
    }
}


data class Flashcard(
    val question: String,
    val answer: String)

@Composable
fun Quiz(modifier: Modifier = Modifier){
    val flashcards = listOf(
        Flashcard("How many letters are in the English Alphabet?", "26"),
        Flashcard("What is 2 + 6?", "8"),
        Flashcard("What is the first letter of the English Alphabet?", "A"),
        Flashcard("What is the color of the sky?", "Blue")
    )

    var currentIndex by remember { mutableStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var isComplete by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isComplete) {
                Button(onClick = {
                    scope.launch {
                        // dismiss current snackbar if active
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                    currentIndex = 0
                    userInput = ""
                    isComplete = false

                }) {
                    Text("Restart Quiz")
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .height(128.dp)
                )
                {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = flashcards[currentIndex].question,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    label = { Text("Answer") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    // checks if answer is correct
                    if (userInput.equals(flashcards[currentIndex].answer, ignoreCase = true)) {
                        userInput = ""
                        // last question
                        if (currentIndex == flashcards.size - 1) {
                            isComplete = true

                            scope.launch {
                                // dismiss current snackbar if active
                                snackbarHostState.currentSnackbarData?.dismiss()
                                // show new snackbar
                                snackbarHostState.showSnackbar(
                                    message = "Quiz Complete! Press the button to restart!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else {
                            currentIndex++
                            scope.launch {
                                // dismiss current snackbar if active
                                snackbarHostState.currentSnackbarData?.dismiss()
                                // show new snackbar
                                snackbarHostState.showSnackbar(
                                    message = "Correct answer! Next question!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    } else {
                        userInput = ""
                        scope.launch {
                            // dismiss current snackbar if active
                            snackbarHostState.currentSnackbarData?.dismiss()
                            // show new snackbar
                            snackbarHostState.showSnackbar(
                                message = "Incorrect answer! Try again!",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }) {
                    Text("Submit Answer")
                }
            }
        }


    }

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OliviaMaQuizAppWithRecompositionTheme {
        Quiz()
    }
}