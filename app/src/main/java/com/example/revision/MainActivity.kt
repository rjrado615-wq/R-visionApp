package com.example.revision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Card(val question: String, val answer: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { Surface(Modifier.fillMaxSize()) { RevisionScreen() } } }
    }
}

@Composable
fun RevisionScreen() {
    val cards = remember {
        mutableStateListOf(
            Card("Capitale de la France ?", "Paris"),
            Card("Formule de l'eau ?", "H₂O"),
            Card("Année de la Révolution française ?", "1789"),
            Card("Racine carrée de 144 ?", "12")
        )
    }
    var index by remember { mutableIntStateOf(0) }
    var flipped by remember { mutableStateOf(false) }
    var known by remember { mutableIntStateOf(0) }
    var seen by remember { mutableIntStateOf(0) }
    var showAdd by remember { mutableStateOf(false) }

    fun next(ok: Boolean) {
        if (ok) known++
        seen++
        flipped = false
        index = (index + 1) % cards.size
    }

    Column(
        Modifier.fillMaxSize().padding(24.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Révisions", fontSize = 28.sp)
        Text("Carte ${index + 1}/${cards.size}  •  Score : $known/$seen")
        Spacer(Modifier.height(24.dp))

        ElevatedCard(
            Modifier.fillMaxWidth().weight(1f).clickable { flipped = !flipped }
        ) {
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(
                    if (flipped) cards[index].answer else cards[index].question,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Text(
            if (flipped) "Réponse" else "Touche la carte pour voir la réponse",
            Modifier.padding(top = 8.dp)
        )
        Spacer(Modifier.height(16.dp))

        if (flipped) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { next(false) }, modifier = Modifier.weight(1f)) { Text("À revoir") }
                Button(onClick = { next(true) }, modifier = Modifier.weight(1f)) { Text("Je savais") }
            }
        }
        OutlinedButton(onClick = { showAdd = true }, modifier = Modifier.fillMaxWidth()) {
            Text("+ Ajouter une carte")
        }
    }

    if (showAdd) {
        var q by remember { mutableStateOf("") }
        var a by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAdd = false },
            title = { Text("Nouvelle carte") },
            text = {
                Column {
                    OutlinedTextField(q, { q = it }, label = { Text("Question") })
                    OutlinedTextField(a, { a = it }, label = { Text("Réponse") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (q.isNotBlank() && a.isNotBlank()) cards.add(Card(q.trim(), a.trim()))
                    showAdd = false
                }) { Text("Ajouter") }
            },
            dismissButton = { TextButton(onClick = { showAdd = false }) { Text("Annuler") } }
        )
    }
}
