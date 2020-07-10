package com.ismail.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.fragment_detail.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {
    private var notificationOnOrOff  = false
    var c = Calendar.getInstance()

    private lateinit var toggle_notifcation : TextView
    private lateinit var spinner: Spinner
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var now: Calendar
    private lateinit var calender: TextView
    private lateinit var timePicker: TextView
    private val dateFormat = SimpleDateFormat("EEEE MMM dd", Locale.US)
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.US)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        now = Calendar.getInstance()
        calender = view.findViewById(R.id.tvCalender)
        timePicker = view.findViewById(R.id.tv_time_picker)
        calender.setOnClickListener {
            datePickerDialogue()
        }
        timePicker.setOnClickListener {
            timePickerDialogue()
        }
        return view
    }

    private fun datePickerDialogue() {
        val calender = Calendar.getInstance()
        var datePickerDialog = DatePickerDialog(
            requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                now.set(Calendar.YEAR, year)
                now.set(Calendar.MONTH, month)
                now.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date = dateFormat.format(now.time)
                tvCalender.text = date

                Toast.makeText(requireContext(), "Click", Toast.LENGTH_SHORT).show()
            },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()

    }

    private fun timePickerDialogue() {
        val timepicker = TimePickerDialog(
            requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                now.set(Calendar.HOUR_OF_DAY, hourOfDay)
                now.set(Calendar.MINUTE, minute)
                val time = timeFormat.format(now.time)
                time
                if(now.timeInMillis > c.timeInMillis) {
                    tv_time_picker.text = time
                }
                else{
                    tv_time_picker.text = null
                    Toast.makeText(requireContext(), "Time chosen is in the past", Toast.LENGTH_SHORT).show()
                    Log.i("past_time", "Time chosen is in past  ")
                }

            },

            now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false
        )


        timepicker.show()

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_item, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val title = edtTaskName.text.toString()
        when (item.itemId) {
            R.id.add_item -> {
                saveTodoItem()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveTodoItem() {
        val taskName = edtTaskName.text.toString()
        val dueDate = tvCalender.text.toString()
        if (taskName.isBlank()) {
            Toast.makeText(requireContext(), "Empty task field", Toast.LENGTH_SHORT).show()
            return
        }
        val toDOItem = TodoItem(0, taskName, dueDate)
        todoViewModel.insertItem(toDOItem)
        Log.i("item_inserted", "Item is inserted -> $toDOItem ")
        Toast.makeText(requireContext(), "Item inserted successfully", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_detailFragment_to_mainFragment)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.notification_toggle) as Spinner
        toggle_notifcation = view.findViewById(R.id.tv_notification_status)
        val notification_type = arrayOf("Off", "On")
         spinner.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            notification_type
        )
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
       // spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                toggle_notifcation.text = notification_type[position]
               var selectedItem : String = notification_type[position].toString()
                var item = selectedItem.toUpperCase()
                notificationOnOrOff = selectedItem != "OFF"


            }


        }


    }
}