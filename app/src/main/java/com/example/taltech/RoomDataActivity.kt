package com.example.taltechapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taltech.R
import com.example.taltechapp.DataParser.DataParserListener

class RoomDataActivity : AppCompatActivity() {
    private lateinit var dataParser: DataParser
    lateinit var roomNumber: String
    lateinit var ruuminumber: TextView
    lateinit var ruumiTemperatuur: TextView
    lateinit var ruumiCO2Tase: TextView
    lateinit var ruumiNiiskuseTase: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_activity)
        ruumiTemperatuur = findViewById(R.id.ruumiTemperatuur)
        ruuminumber = findViewById(R.id.ruuminumber)
        ruumiCO2Tase = findViewById(R.id.ruumiCO2Tase)
        ruumiNiiskuseTase = findViewById(R.id.ruumiNiiskuseTase)
        dataParser = DataParser()
        roomNumber = intent.getStringExtra("roomNr").toString()
        fetchRoomDataList(roomNumber)
    }

    private fun fetchRoomDataList(roomNr: String?) {
        dataParser!!.fetchRoomDataList(roomNr!!, object : DataParserListener {
            override fun onRoomNrListFetched(roomNrList: List<String>?) {}
            override fun onRoomDataListFetched(roomDataList: List<String>?) {
                runOnUiThread {
                    val room = roomDataList!![0]
                    val temperature = roomDataList[1]
                    val CO2 = roomDataList[2]
                    val humidity = roomDataList[3]
                    ruuminumber!!.text = room
                    ruumiTemperatuur!!.text = temperature
                    ruumiCO2Tase!!.text = CO2
                    ruumiNiiskuseTase!!.text = humidity
                }
            }
        })
    }
}