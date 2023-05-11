package com.example.practicadb_ashly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicadb_ashly.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var madapter: UserAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAdapter(list: List<User>) {
        madapter?.setData(list)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
        val userList = AppDatabase(this@MainActivity).getUserDao().getAllUser()

            madapter = UserAdapter()
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = madapter
                setAdapter(userList)

                    madapter?.setOnActionEditListener {
                        val intent = Intent(this@MainActivity, AddActivity::class.java)
                        intent.putExtra("Data", it)
                        startActivity(intent)
                    }
                    madapter?.setOnActionDeleteListener {
                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setMessage("Estas seguro que deseas eliminarlo?")
                        builder.setPositiveButton("SI") { p0, p1 ->
                            lifecycleScope.launch{
                                AppDatabase(this@MainActivity).getUserDao().deleteUser(it)
                                val list = AppDatabase(this@MainActivity).getUserDao().getAllUser()
                                setAdapter(list)
                            }
                            p0.dismiss()
                        }

                        builder.setNegativeButton("NO"){ p0, p1 ->
                            p0.dismiss()
                        }

                        val dialog = builder.create()
                        dialog.show()

                }
            }
        }
    }
}