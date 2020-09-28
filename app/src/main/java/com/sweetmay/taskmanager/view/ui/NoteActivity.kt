package com.sweetmay.taskmanager.view.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.sweetmay.taskmanager.view.ui.viewmodel.NoteViewModel
import com.sweetmay.taskmanager.R
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.view.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.appbar_layout.*

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    override val layoutRes: Int?
        get() = R.layout.activity_note

    override val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.note)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        editNote()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        MenuInflater(this).inflate(R.menu.note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.delete -> {
                viewModel.deleteNote()
                finish()
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }

    private fun editNote() {
        if (intent.extras?.get("note") != null) {
            viewModel.loadNote(intent.extras?.get("note") as String)
        }
    }

    override fun renderData(data: Note?) {
        data?.let { note_title.setText(it.title); note_body.setText(it.text)}
    }


    override fun onPause() {
        super.onPause()
        viewModel.save(note_title.text.toString(), note_body.text.toString())
    }
}