package com.example.tricygo_final.admin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.tricygo_final.databinding.ActivityTricyDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TricyDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTricyDetailsBinding
    private var eventId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTricyDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        eventId = intent.getStringExtra("id")!!
        loadDetails()
    }
    private fun loadDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("Tricycles")
        ref.child(eventId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get data

                    val name = "${snapshot.child("name").value}"
                    val price = "${snapshot.child("price").value}"
                    val description = "${snapshot.child("description").value}"
                    val image = "${snapshot.child("image").value}"
                    val uid = "${snapshot.child("uid").value}"

                    //set data

                    binding.eventTitle.text = eventsTitle
                    binding.newsDes.text = eventsDescription

                    Glide.with(this@EventDetailActivity)
                        .load(image)
                        .into(binding.eventCover)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}