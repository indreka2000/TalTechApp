package com.example.taltechapp

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.taltechapp.DataParser.DataParserListener

class MainActivity : AppCompatActivity() {

    private lateinit var roomListView: ListView
    private lateinit var roomListAdapter: ArrayAdapter<String>
    private lateinit var dataParser: DataParser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        roomListView = findViewById(R.id.RoomNrList)
        roomListAdapter = ArrayAdapter(this, R.layout.simple_list_item_1)
        roomListView.setAdapter(roomListAdapter)
        dataParser = DataParser()

        fetchRoomNrList()
    }

    private fun fetchRoomNrList() {
        dataParser.fetchRoomNrList(object : DataParserListener {
            override fun onRoomNrListFetched(roomNrList: List<String>?) {
                runOnUiThread {
                    roomListAdapter.clear()
                    roomListAdapter.addAll(roomNrList!!)
                    roomListAdapter.notifyDataSetChanged()
                }
                roomListView.onItemClickListener =
                    OnItemClickListener { adapterView, view, i, l ->
                        val roomNr = roomListAdapter.getItem(i)
                        val intent = Intent(applicationContext, RoomDataActivity::class.java)
                        intent.putExtra("roomNr", roomNr)
                        startActivity(intent)
                    }
            }

            override fun onRoomDataListFetched(roomDataList: List<String>?) {}
        })
    }
}