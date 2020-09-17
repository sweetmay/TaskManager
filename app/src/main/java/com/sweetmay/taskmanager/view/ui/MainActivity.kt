package com.sweetmay.taskmanager.view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sweetmay.taskmanager.*
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.view.adapter.TaskAdapterRV
import com.sweetmay.taskmanager.view.callback.OnItemRVClick
import com.sweetmay.taskmanager.viewmodel.MainViewModel
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
        viewModel.getNotes().observe(this, { noteArr ->
            Log.d("aa", noteArr.toString())
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