package com.example.examen_moviles_1

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VentanaFinal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventana_final)

        // Obtener el monto final desde el Intent
        val montoFinal = intent.getDoubleExtra("montoFinal", 100.0)

        // Mostrar el monto final y un mensaje adecuado
        val tvMensajeFinal = findViewById<TextView>(R.id.tvMensajeFinal)
        val tvMontoFinal = findViewById<TextView>(R.id.tvMontoFinal)

        tvMontoFinal.text = "Monto final: $$montoFinal"

        // Determinar el mensaje final según el monto
        when {
            montoFinal > 100 -> tvMensajeFinal.text = "Eres un ganador"
            montoFinal == 100.0 -> tvMensajeFinal.text = "Te salvaste..."
            montoFinal > 0 -> tvMensajeFinal.text = "No deberías de jugar…Retírate"
            montoFinal == 0.0 -> tvMensajeFinal.text = "Lo perdiste todo….No vuelvas a jugar!"
        }
    }
}
