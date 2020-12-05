package com.sweetmay.taskmanager.view.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.sweetmay.taskmanager.R
import com.sweetmay.taskmanager.extensions.getColorInt
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.view.ui.base.BaseActivity
import com.sweetmay.taskmanager.view.ui.customviews.colorpicker.OnColorClickListener
import com.sweetmay.taskmanager.view.ui.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.appbar_layout.*
import org.koin.android.viewmodel.ext.android.viewModel

class NoteActivity : BaseActivity<NoteData>() {

    override val layoutRes: Int?
        get() = R.layout.activity_note

    override val viewModel: NoteViewModel by viewModel()

    var currentColor: Int = Color.BLACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        initCirclesContainerView()
        supportActionBar?.title = getString(R.string.note)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        editNote()
    }

    private fun initCirclesContainerView() {
        val colors: ArrayList<Int> = ArrayList()
        App.Color.values().forEach { color ->
            colors.add(color.getColorInt(this))
        }
        circle_container.addCircles(colors, object : OnColorClickListener {
            override fun onClick(color: Int) {
                toolbar.setTitleTextColor(color)
                currentColor = color
                viewModel.save(note_title.text.toString(), note_body.text.toString(), currentColor)
            }
        })
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
            val note: Note = intent.extras?.get("note") as Note
            viewModel.loadNote(note.id)
            toolbar.setTitleTextColor(note.color)
            currentColor = note.color
        }
    }

    override fun renderData(data: NoteData) {
        data.note.let { note_title.setText(it?.title); note_body.setText(it?.text)}
    }

    override fun onPause() {
        super.onPause()
        viewModel.save(note_title.text.toString(), note_body.text.toString(), currentColor )
    }
}