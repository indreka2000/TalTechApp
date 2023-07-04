package com.example.taltechapp

import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DataParser {
    private val host = "http://192.168.0.18"
    private val api = "/hostName/testserver-master/api/arooms/"
    private val rooms = "main.Json/"
    private val roomData = "roomData/"

    interface DataParserListener {
        fun onRoomNrListFetched(roomNrList: List<String>?)
        fun onRoomDataListFetched(roomDataList: List<String>?)
    }

    fun fetchRoomNrList(listener: DataParserListener) {
        RoomNrListTask(listener).execute()
    }

    fun fetchRoomDataList(roomNr: String, listener: DataParserListener) {
        RoomDataListTask(roomNr, listener).execute()
    }

    private inner class RoomNrListTask(private val listener: DataParserListener) :
        AsyncTask<Void?, Void?, List<String>>() {
        override fun doInBackground(vararg params: Void?): List<String>? {
            val roomNrList: MutableList<String> = ArrayList()
            try {
                val url = URL(host + api + rooms)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                    val jsonResponse = StringBuilder()
                    var line: String?
                    while (bufferedReader.readLine().also { line = it } != null) {
                        jsonResponse.append(line)
                    }
                    val jsonData = jsonResponse.toString()
                    try {
                        val jsonArray = JSONArray(jsonData)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val roomNr = jsonObject.getString("roomNr")
                            roomNrList.add(roomNr)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    bufferedReader.close()
                    inputStream.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return roomNrList
        }

        override fun onPostExecute(roomNrList: List<String>) {
            listener.onRoomNrListFetched(roomNrList)
        }
    }

    private inner class RoomDataListTask(
        private val roomNr: String,
        private val listener: DataParserListener
    ) :
        AsyncTask<Void?, Void?, List<String>>() {
        override fun doInBackground(vararg params: Void?): List<String>? {
            val roomDataList: MutableList<String> = ArrayList()
            try {
                val url = URL("$host$api$roomData$roomNr/")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                    val jsonResponse = StringBuilder()
                    var line: String?
                    while (bufferedReader.readLine().also { line = it } != null) {
                        jsonResponse.append(line)
                    }
                    val jsonData = jsonResponse.toString()
                    try {
                        val jsonObject = JSONObject(jsonData)
                        val temperature = jsonObject.getString("temperature")
                        val CO2 = jsonObject.getString("CO2")
                        val humidity = jsonObject.getString("humidity")
                        val roomNumber = jsonObject.getString("roomNr")
                        roomDataList.add(roomNumber)
                        roomDataList.add(temperature)
                        roomDataList.add(CO2)
                        roomDataList.add(humidity)
                        Log.d(TAG, "RoomDataList: $roomDataList")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    bufferedReader.close()
                    inputStream.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return roomDataList
        }

        override fun onPostExecute(roomDataList: List<String>) {
            listener.onRoomDataListFetched(roomDataList)
        }
    }

    companion object {
        private const val TAG = "DataParser"
    }
}