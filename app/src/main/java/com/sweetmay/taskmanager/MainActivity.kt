package com.sweetmay.taskmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var adapterRV: TaskAdapterRV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        notes_rv.layoutManager = LinearLayoutManager(this)
        adapterRV = TaskAdapterRV()
        notes_rv.adapter = adapterRV

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getNotes().observe(this, {noteArr ->
            adapterRV.notes = noteArr
        })
        btn.setOnClickListener { viewModel.addNote(Note("aaaaaa", "aaaaa", "aaaaa")) }
    }
}