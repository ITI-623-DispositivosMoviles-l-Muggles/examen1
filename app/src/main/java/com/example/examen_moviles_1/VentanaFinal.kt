package com.example.examen_moviles_1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VentanaFinal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventana_final)

        val montoInicial = intent.getDoubleExtra("montoInicial", 0.0)
        val montoFinal = intent.getDoubleExtra("montoFinal", 100.0)

        // Mostrar el monto final y un mensaje adecuado
        val tvMensajeFinal = findViewById<TextView>(R.id.tvMensajeFinal)
        val tvMontoFinal = findViewById<TextView>(R.id.tvMontoFinal)

        tvMontoFinal.text = "Monto final: $$montoFinal"

        // Ajustar la lógica para los mensajes finales
        when {
            montoFinal == 0.0 -> tvMensajeFinal.text = "Lo perdiste todo….No vuelvas a jugar!"
            montoFinal > montoInicial -> tvMensajeFinal.text = "¡Eres un ganador!"
            montoFinal < montoInicial -> tvMensajeFinal.text = "No deberías de jugar…Retírate"
            montoFinal == montoInicial -> tvMensajeFinal.text = "Te salvaste..."
        }
    }
}
