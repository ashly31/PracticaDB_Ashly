package com.example.practicadb_ashly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.practicadb_ashly.databinding.ActivityAddBinding
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddBinding
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getSerializableExtra("Data") as User

        if (user == null) binding.btnAddOrUpdateUser.text = "Add User"
        else { binding.btnAddOrUpdateUser.text = "Update"
            binding.edFirstName.setText(user?.firstName.toString())
            binding.edLastName.setText(user?.lastName.toString())
        }

        binding.btnAddOrUpdateUser.setOnClickListener{ addUser()}
    }

    private fun addUser(){
        val firstName = binding.edFirstName.text.toString()
        val lastName = binding.edLastName.text.toString()

       lifecycleScope.launch {
           if (user == null) {
               val user = User(firstName = firstName, lastName = lastName)
               AppDatabase(this@AddActivity).getUserDao().addUser(user)
               finish()
           } else {
               val u = User(firstName, lastName)
               u.id = user?.id ?: 0
               AppDatabase(this@AddActivity).getUserDao().updateUser(u)
               finish()
           }

       }
    }
}