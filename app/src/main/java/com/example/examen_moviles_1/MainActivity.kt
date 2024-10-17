package com.example.examen_moviles_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import java.util.Calendar  // Asegúrate de importar esto

class MainActivity : AppCompatActivity() {

    private lateinit var etNombreJugador: EditText
    private lateinit var dpFechaNacimiento: DatePicker
    private lateinit var etFondoApuestas: EditText
    private lateinit var spCantidadDados: Spinner
    private lateinit var btnIngresar: Button

    // Monto mínimo de apuesta
    private val montoMinimo: Double = 2000000.0 // 2 millones de colones

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar los elementos de la interfaz
        etNombreJugador = findViewById(R.id.etNombreJugador)
        dpFechaNacimiento = findViewById(R.id.dpFechaNacimiento)
        etFondoApuestas = findViewById(R.id.etFondoApuestas)
        spCantidadDados = findViewById(R.id.spCantidadDados)
        btnIngresar = findViewById(R.id.btnIngresar)

        // Configurar el Spinner para seleccionar entre 2 y 3 dados
        val dadosOptions = arrayOf("2", "3")
        spCantidadDados.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dadosOptions)

        // Configurar el botón para validar los datos
        btnIngresar.setOnClickListener {
            validarDatos()
        }
    }

    private fun validarDatos() {
        val nombreJugador = etNombreJugador.text.toString()
        val fondoApuestas = etFondoApuestas.text.toString().toDoubleOrNull()
        val cantidadDados = spCantidadDados.selectedItem.toString().toInt()

        // Obtener la fecha seleccionada
        val year = dpFechaNacimiento.year
        val month = dpFechaNacimiento.month
        val day = dpFechaNacimiento.dayOfMonth
        val fechaNacimiento = Calendar.getInstance().apply {
            set(year, month, day)
        }

        // Calcular la edad del jugador
        val edadJugador = calcularEdad(fechaNacimiento.timeInMillis)

        // Validar que el nombre no esté vacío
        if (nombreJugador.isEmpty()) {
            mostrarMensaje("Por favor ingrese su nombre.")
            return
        }

        // Validar la edad mínima de 18 años
        if (edadJugador < 18) {
            mostrarMensaje("Lo sentimos, debes tener al menos 18 años para jugar.")
            return
        }

        // Validar el fondo de apuestas
        if (fondoApuestas == null || fondoApuestas < montoMinimo) {
            mostrarMensaje("El fondo de apuestas debe ser al menos de 2 millones de colones.")
            return
        }

        // Si todo es válido, cargar la ventana de juego
        cargarVentanaJuego(nombreJugador, cantidadDados, fondoApuestas)
    }

    // Función para calcular la edad en base a la fecha de nacimiento
    private fun calcularEdad(fechaNacimientoMillis: Long): Int {
        val hoy = Calendar.getInstance()
        val nacimiento = Calendar.getInstance().apply {
            timeInMillis = fechaNacimientoMillis
        }

        var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)

        // Si no ha pasado su cumpleaños este año, restar 1 año
        if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
            edad--
        }

        return edad
    }

    // Función para mostrar un mensaje emergente (Toast)
    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    // Función para cargar la ventana de juego si los datos son válidos
    private fun cargarVentanaJuego(nombre: String, dados: Int, apuesta: Double) {
        try {
            val intent = Intent(this, VentanaJuegoActivity::class.java).apply {
                putExtra("NOMBRE_JUGADOR", nombre)
                putExtra("CANTIDAD_DADOS", dados)
                putExtra("APUESTA_INICIAL", apuesta)
            }
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al iniciar el juego: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

}
