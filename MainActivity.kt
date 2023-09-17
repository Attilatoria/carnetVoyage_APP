import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreetingsPreview()
        }
    }
}

data class Trip(val destination: String, val date: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripScreen(onAddTrip: (Trip) -> Unit) {
    var destination by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf(false) }
    var destinationError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = destination,
            onValueChange = {
                destination = it
                destinationError = it.isBlank()
            },
            label = { Text("Destination") },
            isError = destinationError,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = date,
            onValueChange = {
                date = it
                dateError = !isValidDate(it)
            },
            label = { Text("Date (JJ/MM/AAAA)") },
            isError = dateError,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                descriptionError = it.isBlank()
            },
            label = { Text("Description") },
            isError = descriptionError,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                if (!destinationError && !dateError && !descriptionError) {
                    val trip = Trip(destination, date, description)
                    onAddTrip(trip)
                    destination = ""
                    date = ""
                    description = ""
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Ajouter")
        }

        if (destinationError || dateError || descriptionError) {
            Text(
                text = "Veuillez remplir tous les champs correctement.",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun TripApp() {
    var trips by remember { mutableStateOf(emptyList<Trip>()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AddTripScreen { trip ->
            trips = trips + trip
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Afficher les voyages enregistrés
        trips.forEach { trip ->
            Text(text = "Destination: ${trip.destination}")
            Text(text = "Date: ${trip.date}")
            Text(text = "Description: ${trip.description}")
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun MyApp() {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        TripApp()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingsPreview() {
    MaterialTheme {
        MyApp()
    }
}

//@OptIn(ExperimentalMaterialApi::class)
//fun main() {
//    setContent {
//        MyApp()
//    }
//}

private fun isValidDate(date: String): Boolean {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    dateFormat.isLenient = false

    return try {
        dateFormat.parse(date)
        true
    } catch (e: Exception) {
        false
    }
}
