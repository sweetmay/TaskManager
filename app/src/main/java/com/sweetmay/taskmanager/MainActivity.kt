package com.sweetmay.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnItemRVClick {

    lateinit var viewModel: MainViewModel
    lateinit var adapterRV: TaskAdapterRV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        notes_rv.layoutManager = LinearLayoutManager(this)
        adapterRV = TaskAdapterRV(this)
        notes_rv.adapter = adapterRV

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getNotes().observe(this, {noteArr ->
            adapterRV.notes = noteArr
        })

        val intent = Intent(this, NoteActivity::class.java)
        btn.setOnClickListener { startActivity(intent) }


    }

    override fun onItemClick(note: Note) {
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra("note", note)
        startActivity(intent)
    }
}