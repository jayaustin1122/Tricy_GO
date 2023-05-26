package com.example.tricygo_final.admin.tabs

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tricygo_final.databinding.FragmentBottomSheetDialogAddBinding
import com.example.tricygo_final.models.TradeInModel
import com.example.tricygo_final.models.TricycleModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddTradeIn: BottomSheetDialogFragment() {
    private lateinit var progressDialog : ProgressDialog
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseDatabase
    private lateinit var selectedImage : Uri
    private lateinit var binding: FragmentBottomSheetDialogAddBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBottomSheetDialogAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()


        progressDialog = ProgressDialog(this.requireContext())
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)
        binding.title.setText("Add New Trade-In")
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.imageContainer.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }

        binding.btnSave.setOnClickListener {
            validateData()

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            if (data.data != null){
                selectedImage = data.data!!
                binding.imageContainer.setImageURI(selectedImage)
            }
        }
    }
    var name = ""
    var price = ""
    var description = ""
    private fun validateData() {
        val title = binding.etName.text.toString().trim()
        val price = binding.etPrice.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        when {
            title.isEmpty() -> {
                Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
            price.isEmpty() -> {
                Toast.makeText(requireContext(), "Price cannot be empty", Toast.LENGTH_SHORT).show()
            }
            description.isEmpty() -> {
                Toast.makeText(requireContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else -> {
                uploadImage()
            }
        }
    }
    private fun uploadImage() {
        progressDialog.setMessage("Uploading Image...")
        progressDialog.show()

        val reference = storage.reference.child("TradeInProfile")
            .child(Date().time.toString())
        reference.putFile(selectedImage).addOnCompleteListener{
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener {task->
                    uploadInfo(task.toString())
                }
            }
        }

    }
    private fun uploadInfo(imgUrl: String) {
        progressDialog.setMessage("Saving...")
        progressDialog.show()
        name = binding.etName.text.toString().trim()
        price = binding.etPrice.text.toString().trim()
        description = binding.etDescription.text.toString().trim()
        val currentDate = getCurrentDate()
        val timestamp = System.currentTimeMillis()
        val currentTime = getCurrentTime()
        val uid = auth.uid

        val tradeIn = TradeInModel(
            uid = uid,
            name = name,
            price = price,
            description = description,
            image = imgUrl,
            currentDate = currentDate,
            currentTime = currentTime,
            id = "$timestamp"

        )

        database.getReference("Trade-ins")
            .child(timestamp.toString())
            .setValue(tradeIn)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.dismiss()
                    dismiss()
                    Toast.makeText(this.requireContext(), "Product Added", Toast.LENGTH_SHORT).show()
                    binding.etName.text = null
                    binding.etDescription.text = null
                    binding.etPrice.text = null
//                    // Send the notification after successful upload
//                    val notification = PushNotification(
//                        NotificationData(title, description),
//                        TOPIC
//                    )
//                    sendNotification(notification)
                } else {
                    Toast.makeText(this.requireContext(), it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }




    private fun getCurrentTime(): String {
        val tz = TimeZone.getTimeZone("GMT+08:00")
        val c = Calendar.getInstance(tz)
        val hours = String.format("%02d", c.get(Calendar.HOUR))
        val minutes = String.format("%02d", c.get(Calendar.MINUTE))
        return "$hours:$minutes"
    }


    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val currentDateObject = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        return formatter.format(currentDateObject)
    }

}