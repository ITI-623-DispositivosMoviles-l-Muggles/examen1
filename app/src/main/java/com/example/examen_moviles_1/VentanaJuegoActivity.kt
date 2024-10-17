package com.example.examen_moviles_1

import android.content.Intent
import android.os.Bundle
import android.widget.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class VentanaJuegoActivity : AppCompatActivity() {

    private lateinit var nombreJugador: TextView
    private lateinit var montoDisponible: TextView
    private lateinit var montoApostado: EditText
    private lateinit var dado1: ImageView
    private lateinit var dado2: ImageView
    private lateinit var dado3: ImageView
    private lateinit var gridApuestas: GridLayout
    private lateinit var estadoJuego: ImageView
    private var montoActual: Double = 0.0
    private var montoInicial: Double = 0.0
    private var cantidadDados: Int = 2
    private var radioButtonSeleccionado: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventana_juego_vertical)

        // Inicialización de las vistas
        nombreJugador = findViewById(R.id.etNombreJugador)
        montoDisponible = findViewById(R.id.tvMontoDisponible)
        montoApostado = findViewById(R.id.etMontoApostado)
        dado1 = findViewById(R.id.dado1)
        dado2 = findViewById(R.id.dado2)
        dado3 = findViewById(R.id.dado3)
        gridApuestas = findViewById(R.id.gridApuestas)
        estadoJuego = findViewById(R.id.ivEstadoJuego)

        val nombre = intent.getStringExtra("NOMBRE_JUGADOR") ?: "Jugador"
        montoInicial = intent.getDoubleExtra("APUESTA_INICIAL", 100.0)
        val apuestaInicial = montoInicial
        cantidadDados = intent.getIntExtra("CANTIDAD_DADOS", 2)

        nombreJugador.setText(nombre)
        montoActual = apuestaInicial
        montoDisponible.text = "Monto disponible: ₡%.2f".format(montoActual)

        configurarOpcionesDeApuesta()


        if (cantidadDados == 3) {
            dado3.visibility = View.VISIBLE
        } else {
            dado3.visibility = View.GONE
        }

        findViewById<Button>(R.id.btnRetirarse).setOnClickListener {
            retirarseDelJuego()
        }

        findViewById<Button>(R.id.btnLanzarDados).setOnClickListener {
            lanzarDados()
        }
    }

    private fun configurarOpcionesDeApuesta() {
        gridApuestas.removeAllViews()

        // Establecer el rango de apuestas según la cantidad de dados
        val rangoApuestas = if (cantidadDados == 3) 3..18 else 2..12

        gridApuestas.columnCount = 4  // 4 columnas fijas

        for (i in rangoApuestas) {
            val radioButton = RadioButton(this).apply {
                text = i.toString()
                id = View.generateViewId()  // Generar un ID único para cada RadioButton
                layoutParams = GridLayout.LayoutParams().apply {
                    width = GridLayout.LayoutParams.WRAP_CONTENT
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                }

                // Configurar un listener para gestionar la selección
                setOnClickListener {
                    seleccionarRadioButton(this)
                }
            }
            gridApuestas.addView(radioButton)
        }

        // Seleccionar el primer RadioButton por defecto
        if (gridApuestas.childCount > 0) {
            val firstRadioButton = gridApuestas.getChildAt(0) as RadioButton
            firstRadioButton.isChecked = true
            radioButtonSeleccionado = firstRadioButton
        }
    }

    private fun seleccionarRadioButton(radioButton: RadioButton) {
        // Desmarcar el anterior RadioButton si existe
        radioButtonSeleccionado?.isChecked = false

        // Marcar el nuevo RadioButton
        radioButtonSeleccionado = radioButton
        radioButtonSeleccionado?.isChecked = true
    }

    private fun lanzarDados() {
        // Obtener el monto apostado y validar
        val monto = montoApostado.text.toString().toDoubleOrNull()
        if (monto == null || monto <= 0 || monto > montoActual) {
            Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show()
            return
        }

        // Generar los valores de los dados
        val valorDado1 = (1..6).random()
        val valorDado2 = (1..6).random()
        var valorDado3 = 0
        if (cantidadDados == 3) {
            valorDado3 = (1..6).random()
        }

        // Mostrar los resultados de los dados
        mostrarResultadoDados(valorDado1, dado1)
        mostrarResultadoDados(valorDado2, dado2)
        if (cantidadDados == 3) {
            mostrarResultadoDados(valorDado3, dado3)
        }

        // Calcular el resultado del juego
        val totalDados = valorDado1 + valorDado2 + if (cantidadDados == 3) valorDado3 else 0
        verificarResultado(totalDados, monto)
    }

    private fun mostrarResultadoDados(valor: Int, dado: ImageView) {
        val resId = when (valor) {
            1 -> R.drawable.dado1
            2 -> R.drawable.dado2
            3 -> R.drawable.dado3
            4 -> R.drawable.dado4
            5 -> R.drawable.dado5
            6 -> R.drawable.dado6
            else -> R.drawable.ic_question_mark
        }
        dado.setImageResource(resId)
    }

    private fun verificarResultado(totalDados: Int, montoApuesta: Double) {
        if (radioButtonSeleccionado == null) {
            Toast.makeText(this, "Selecciona un número para apostar", Toast.LENGTH_SHORT).show()
            return
        }

        val numeroApuesta = radioButtonSeleccionado?.text.toString().toInt()

        // Comprobar si el jugador ganó o perdió
        if (totalDados == numeroApuesta) {
            // El jugador ganó
            montoActual += montoApuesta * 2
            estadoJuego.setImageResource(R.drawable.ic_happy_face)

            Toast.makeText(this, "¡Felicidades! Eres un ganador", Toast.LENGTH_LONG).show()
        } else {
            // El jugador perdió
            montoActual -= montoApuesta
            estadoJuego.setImageResource(R.drawable.ic_sad_face)

            Toast.makeText(this, "Has perdido esta ronda", Toast.LENGTH_LONG).show()
        }

        // Actualizar el monto disponible
        montoDisponible.text = "Monto disponible: ₡%.2f".format(montoActual)

    }

    private fun retirarseDelJuego() {
        val intent = Intent(this, VentanaFinal::class.java).apply {
            putExtra("montoInicial", montoInicial)
            putExtra("montoFinal", montoActual)
        }
        startActivity(intent)
    }
}
