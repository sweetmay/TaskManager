package com.sweetmay.taskmanager.view.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sweetmay.taskmanager.*
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.model.Repository
import com.sweetmay.taskmanager.view.adapter.TaskAdapterRV
import com.sweetmay.taskmanager.view.callback.OnItemRVClick
import com.sweetmay.taskmanager.view.ui.base.BaseActivity
import com.sweetmay.taskmanager.view.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<List<Note>?, MainViewState>(), OnItemRVClick {

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override val layoutRes: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    lateinit var adapterRV: TaskAdapterRV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notes_rv.layoutManager = LinearLayoutManager(this)
        adapterRV = TaskAdapterRV(this)
        notes_rv.adapter = adapterRV

        val intent = Intent(this, NoteActivity::class.java)
        btn.setOnClickListener { startActivity(intent) }
    }

    override fun onItemClick(note: Note) {
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra("note", note.id)
        startActivity(intent)
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapterRV.notes = it }
    }

}