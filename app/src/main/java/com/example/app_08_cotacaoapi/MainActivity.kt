package com.example.app_08_cotacaoapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.net.URL
import android.util.Log
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.lang.RuntimeException
import kotlinx.android.synthetic.main.bloco_cotacao.*
import kotlinx.android.synthetic.main.bloco_entrada.*
import kotlinx.android.synthetic.main.bloco_saida.*
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val apiURL = "https://www.mercadobitcoin.net/api/BTC/ticker/"

    var cotacaoBitcoin: Double = 0.0

    private val TAG = "Rafael"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.v(TAG, "Log de verbose")
        Log.d(TAG, "Log de debug")
        Log.i(TAG, "Log de info")
        Log.w(TAG, "Log de alerta")
        Log.e(TAG, "Log de erro", RuntimeException("teste de erro"))

        buscarCotacao()

        btnCalcular.setOnClickListener {
            calcular()
        }
    }

    fun buscarCotacao() {
        doAsync {
            val resposta = URL(apiURL).readText()

            cotacaoBitcoin = JSONObject(resposta).getJSONObject("ticker").getDouble("last")

            val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            val cotacaoFormatada = f.format(cotacaoBitcoin)

            uiThread {
                //alert(String.format("$cotacaoBitcoin").show()
                txtCotacao.setText("$cotacaoFormatada")
            }
        }
    }

    fun calcular(){
        if(txtValor.text.isEmpty()){
            txtValor.error = "Preencha um valor"
            return
        }

        val valorDigitado = txtValor.text.toString().replace(",", ".").toDouble()

        val resultado = if(cotacaoBitcoin>0) valorDigitado/cotacaoBitcoin
            else 0.0

        txtQtdeBitcoins.text = "%.8f".format(resultado)
    }
}