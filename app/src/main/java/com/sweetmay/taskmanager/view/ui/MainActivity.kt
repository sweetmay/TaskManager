package com.sweetmay.taskmanager.view.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sweetmay.taskmanager.R
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.view.adapter.TaskAdapterRV
import com.sweetmay.taskmanager.view.callback.OnItemRVClick
import com.sweetmay.taskmanager.view.ui.base.BaseActivity
import com.sweetmay.taskmanager.view.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<List<Note>?>(), OnItemRVClick, OnItemRVLongClick {

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override val layoutRes: Int
        get() = R.layout.activity_main

    override val viewModel: MainViewModel by viewModel()

    lateinit var adapterRV: TaskAdapterRV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notes_rv.layoutManager = LinearLayoutManager(this)
        adapterRV = TaskAdapterRV(this, this)
        notes_rv.adapter = adapterRV

        val intent = Intent(this, NoteActivity::class.java)
        btn.setOnClickListener { startActivity(intent) }
    }

    override fun onItemClick(note: Note) {
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra("note", note)
        startActivity(intent)
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapterRV.notes = it }
    }

    override fun onItemLongClick(note: Note) {
        
    }

}