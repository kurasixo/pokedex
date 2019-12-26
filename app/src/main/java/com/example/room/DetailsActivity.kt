package com.example.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        getIncomingIntent()
    }

    fun getIncomingIntent() {
        if (intent.hasExtra("description")) {
            val intentDescription: String = intent.getStringExtra("description")
            setDescription(intentDescription)
        }
    }

    fun setDescription(description: String) {
        val descriptionTextView = findViewById<TextView>(R.id.tv_animal_description)
        descriptionTextView.setText(description)
    }
}
