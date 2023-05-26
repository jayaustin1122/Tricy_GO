package com.example.tricygo_final

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.tricygo_final.databinding.FragmentSignUpBinding
import com.example.tricygo_final.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private lateinit var progressDialog : ProgressDialog
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseDatabase
    private lateinit var selectedImage : Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
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

        binding.img.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.btnLogin.setOnClickListener {
            validateData()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            if (data.data != null){
                selectedImage = data.data!!
                binding.img.setImageURI(selectedImage)
            }
        }
    }
    private var email = ""
    private var pass = ""
    private var fullname = ""
    private var address = ""
    private var userType = "member"

    private fun validateData() {
        val email = binding.etEmailSignUp.text.toString().trim()
        val pass = binding.etPasswordSignUp.text.toString().trim()
        val fullname = binding.etFullname.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        when {
            email.isEmpty() -> Toast.makeText(this.requireContext(), "Enter Your Email...", Toast.LENGTH_SHORT).show()
            pass.isEmpty() -> Toast.makeText(this.requireContext(), "Enter Your Password...", Toast.LENGTH_SHORT).show()
            fullname.isEmpty() -> Toast.makeText(this.requireContext(), "Enter Your Fullname...", Toast.LENGTH_SHORT).show()
            address.isEmpty() -> Toast.makeText(this.requireContext(), "Enter Your Address...", Toast.LENGTH_SHORT).show()
            else -> createUserAccount()
        }
    }
    private fun createUserAccount() {
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        auth.createUserWithEmailAndPassword(binding.etEmailSignUp.text.toString().trim(),binding.etPasswordSignUp.text.toString().trim())

            .addOnSuccessListener {
                // if user successfully created ()
                uploadImage()
            }
            .addOnFailureListener { e ->
                //if the user fialef creating account
                progressDialog.dismiss()
                Toast.makeText(this.requireContext(),"Failed Creating Account or ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }
    private fun uploadImage() {
        progressDialog.setMessage("Uploading Image...")
        progressDialog.show()
        val uid = auth.uid


        val reference = storage.reference.child("profile")
            .child(uid!!)
        reference.putFile(selectedImage).addOnCompleteListener{
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener {task->
                    uploadInfo(task.toString())
                }
            }
        }



    }
    private fun uploadInfo(imageUrl: String) {

        progressDialog.setMessage("Saving Account...")
        progressDialog.show()
        email = binding.etEmailSignUp.text.toString().trim()
        pass = binding.etPasswordSignUp.text.toString().trim()
        fullname = binding.etFullname.text.toString().trim()
        address = binding.etAddress.text.toString().trim()
        val currentDate = getCurrentDate()
        val currentTime = getCurrentTime()
        val timestamp = System.currentTimeMillis()
        val uid = auth.uid

        val user = UserModel(
            uid = uid,
            email = email,
            password = pass,
            fullName = fullname,
            address = address,
            image = imageUrl,
            currentDate = currentDate,
            currentTime = currentTime,
            id = "$timestamp",
            userType = "member"
        )


        database.getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    progressDialog.dismiss()
                    findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                    Toast.makeText(this.requireContext(),"Account Created", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this.requireContext(),it.exception!!.message, Toast.LENGTH_SHORT).show()
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