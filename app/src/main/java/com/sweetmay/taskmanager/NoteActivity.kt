package com.sweetmay.taskmanager

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.appbar_layout.*

class NoteActivity : AppCompatActivity() {

    lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        onEditNote()
    }

    private fun onEditNote() {
        if (intent.extras?.get("note") != null) {
            val note = (intent.extras?.get("note") as Note).copy()
            viewModel.setPendingNote(note)
            note_title.setText(note.title)
            note_body.setText(note.text)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId){
        android.R.id.home ->{
            onBackPressed()
            true
        }else -> super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.save(note_title.text.toString(), note_body.text.toString())
    }
}