package com.example.examen_moviles_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var etNombreJugador: EditText
    private lateinit var dpFechaNacimiento: DatePicker
    private lateinit var etFondoApuestas: EditText
    private lateinit var spCantidadDados: Spinner
    private lateinit var btnIngresar: Button

    private val montoMinimo: Double = 2000000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar los elementos de la interfaz
        etNombreJugador = findViewById(R.id.etNombreJugador)
        dpFechaNacimiento = findViewById(R.id.dpFechaNacimiento)
        etFondoApuestas = findViewById(R.id.etFondoApuestas)
        spCantidadDados = findViewById(R.id.spCantidadDados)
        btnIngresar = findViewById(R.id.btnIngresar)


        val dadosOptions = arrayOf("2", "3")
        spCantidadDados.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dadosOptions)


        btnIngresar.setOnClickListener {
            validarDatos()
        }
    }

    private fun validarDatos() {
        val nombreJugador = etNombreJugador.text.toString()
        val fondoApuestas = etFondoApuestas.text.toString().toDoubleOrNull()
        val cantidadDados = spCantidadDados.selectedItem.toString().toInt()


        val year = dpFechaNacimiento.year
        val month = dpFechaNacimiento.month
        val day = dpFechaNacimiento.dayOfMonth
        val fechaNacimiento = Calendar.getInstance().apply {
            set(year, month, day)
        }

        // Calcular la edad del jugador
        val edadJugador = calcularEdad(fechaNacimiento.timeInMillis)

        if (nombreJugador.isEmpty()) {
            mostrarMensaje("Por favor ingrese su nombre.")
            return
        }

        if (edadJugador < 21) {
            mostrarMensaje("Lo sentimos, debes tener al menos 21 años para jugar.")
            return
        }

        if (fondoApuestas == null || fondoApuestas < montoMinimo) {
            mostrarMensaje("El fondo de apuestas debe ser al menos de 2 millones de colones.")
            return
        }

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

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun cargarVentanaJuego(nombre: String, dados: Int, apuesta: Double) {
        val intent = Intent(this, VentanaJuegoActivity::class.java).apply {
            putExtra("NOMBRE_JUGADOR", nombre)
            putExtra("CANTIDAD_DADOS", dados)
            putExtra("APUESTA_INICIAL", apuesta)
        }
        startActivity(intent)
    }

}
