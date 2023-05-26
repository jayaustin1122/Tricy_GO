package com.example.tricygo_final.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tricygo_final.R
import com.example.tricygo_final.admin.activity.TricyDetailsActivity
import com.example.tricygo_final.databinding.ItemTricycleRowBinding
import com.example.tricygo_final.models.TricycleModel
import com.google.firebase.database.FirebaseDatabase

class TricycleAdapter:RecyclerView.Adapter<TricycleAdapter.ViewHolderTricycle> {

    private lateinit var binding : ItemTricycleRowBinding
    private val context : Context
    public var listArrayList : ArrayList<TricycleModel>

    constructor(
        context: Context,
        eventArrayList: ArrayList<TricycleModel>
    ) : super() {
        this.context = context
        this.listArrayList = eventArrayList
    }
    inner class ViewHolderTricycle(itemView: View):RecyclerView.ViewHolder(itemView){
        var name : TextView = binding.tvTitle
        var price : TextView = binding.tvPrice
        var moreBtn : ImageButton = binding.btnMore
        var image : ImageView = binding.img

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTricycle {
        //binding ui row item event
        binding = ItemTricycleRowBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolderTricycle(binding.root)
    }
    override fun onBindViewHolder(holder: ViewHolderTricycle, position: Int) {
        //get data
        val model = listArrayList[position]
        val id = model.id
        val name = model.name
        val price = model.price
        val imageselected = model.image



        //set data's
        holder.name.text = name
        holder.price.text = price

        Glide.with(this@TricycleAdapter.context)
            .load(imageselected)
            .into(holder.image)


        holder.moreBtn.setOnClickListener {
            moreOptions(model,holder)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TricyDetailsActivity::class.java)
            intent.putExtra("id",id)//reference to load the other details
            context.startActivity(intent)
        }

    }

    private fun moreOptions(model: TricycleModel, holder: TricycleAdapter.ViewHolderTricycle) {
        //get id title
        val id = model.id
        val name = model.name
        val price = model.price
        val description = model.description
        val image = model.image
        // show options
        val options = arrayOf("Edit","Delete")
        // show alert dialog
        val  builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option")
            .setItems(options){dialog,position ->
                //handle item clicked
                if (position == 0 ){
                    //edit btn
                    val intent = Intent(context, TricyDetailsActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("name", name)
                    intent.putExtra("price", name)
                    intent.putExtra("description", description)
                    intent.putExtra("image", image)
                    //id as the reference to edit events
                    context.startActivity(intent)

                }
                else if (position == 1){
                    //delete btn
                    //dialog
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Confirm"){a,d->
                            Toast.makeText(context,"Deleting...", Toast.LENGTH_SHORT).show()

                            deleteEvent(model,holder)
                        }
                        .setNegativeButton("Cancel"){a,d->
                            a.dismiss()
                        }
                        .show()
                }
            }
            .show()

    }

    private fun deleteEvent(model: TricycleModel, holder: ViewHolderTricycle) {
        //id as the reference to delete

        val id = model.id

        val dbRef = FirebaseDatabase.getInstance().getReference("Tricycles")
        dbRef.child(id.toString())
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(context,"Unable to delete due to ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }

    override fun getItemCount(): Int {
        return listArrayList.size
    }
}