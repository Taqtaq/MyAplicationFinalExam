package com.example.myapplicationfinalexam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationfinalexam.adapter.TaskAdapter
import com.example.myapplicationfinalexam.data.Task
import com.example.myapplicationfinalexam.databinding.ActivityMainBinding
import com.example.myapplicationfinalexam.databinding.DialogAddTaskBinding
import com.example.myapplicationfinalexam.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Принудительно устанавливаем Toolbar как ActionBar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)

        setupRecyclerView()
        setupViewModel()
        setupSwipeToDelete()
        setupFabClick()
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter { task ->
            showEditTaskDialog(task)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupViewModel() {
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        taskViewModel.allTasks.observe(this) { tasks ->
            adapter.submitList(tasks)

            val isEmpty = tasks.isNullOrEmpty()
            binding.recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
            binding.textViewEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }
    }

    private fun setupSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedTask = adapter.getTaskAt(viewHolder.adapterPosition)
                taskViewModel.delete(deletedTask)
                Snackbar.make(binding.root, R.string.task_deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) { taskViewModel.insert(deletedTask) }
                    .show()
            }
        }).attachToRecyclerView(binding.recyclerView)
    }

    private fun setupFabClick() {
        binding.fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete_all -> {
                showDeleteAllConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAddTaskDialog() {
        val dialogBinding = DialogAddTaskBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.buttonSave.setOnClickListener {
            val title = dialogBinding.editTextTitle.text.toString().trim()
            val description = dialogBinding.editTextDescription.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_SHORT).show()
            } else {
                val newTask = Task(
                    title = title,
                    description = description,
                    timestamp = System.currentTimeMillis()
                )
                taskViewModel.insert(newTask)
                dialog.dismiss()
            }
        }
        dialogBinding.buttonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showEditTaskDialog(task: Task) {
        val dialogBinding = DialogAddTaskBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.editTextTitle.setText(task.title)
        dialogBinding.editTextDescription.setText(task.description)

        dialogBinding.buttonSave.setOnClickListener {
            val title = dialogBinding.editTextTitle.text.toString().trim()
            val description = dialogBinding.editTextDescription.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_SHORT).show()
            } else {
                val updatedTask = task.copy(
                    title = title,
                    description = description,
                    timestamp = System.currentTimeMillis()
                )
                taskViewModel.update(updatedTask)
                dialog.dismiss()
            }
        }
        dialogBinding.buttonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showDeleteAllConfirmation() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_all)
            .setMessage("Are you sure you want to delete all tasks?")
            .setPositiveButton("Yes") { _, _ ->
                taskViewModel.deleteAll()
                Snackbar.make(binding.root, R.string.all_tasks_deleted, Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }
}