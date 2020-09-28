package com.sweetmay.taskmanager.view.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.sweetmay.taskmanager.R
import com.sweetmay.taskmanager.extensions.getColorRes
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.view.ui.base.BaseActivity
import com.sweetmay.taskmanager.view.ui.customviews.colorpicker.OnColorClickListener
import com.sweetmay.taskmanager.view.ui.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.appbar_layout.*
import org.koin.android.viewmodel.ext.android.viewModel

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    override val layoutRes: Int?
        get() = R.layout.activity_note

    override val viewModel: NoteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        val colors: ArrayList<Int> = ArrayList()
        Note.Color.values().forEach { color ->
            colors.add(color.getColorRes())
        }
        circle_container.addCircles(colors, object: OnColorClickListener{
            override fun onClick(color: Int) {
                toolbar.setBackgroundColor(getColor(color))
                viewModel.save(note_title.text.toString(), note_body.text.toString())
            }
        })

        supportActionBar?.title = getString(R.string.note)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        editNote()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.note_menu, menu)
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
            }
            R.id.expand_colors -> {
                toggleColors(item)
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleColors(item: MenuItem){
        if(circle_container.isOpen){
            circle_container.close()
            item.setIcon(R.drawable.ic_expand_more_24px)
        }else{
            circle_container.open()
            item.setIcon(R.drawable.ic_expand_less_24px)
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