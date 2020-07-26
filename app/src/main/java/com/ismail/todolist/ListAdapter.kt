package com.ismail.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.item_list.view.*


class ListAdapter(
    private val callback: AdapterCallBack

) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var todoList: List<TodoItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_list, parent, false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = todoList[position]
        holder.bind(currentItem)
    }

    fun setList(todoItem: List<TodoItem>) {
    val oldList = todoList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            TodoItemDiffCal(oldList, todoItem)
        )
        todoList = todoItem
        diffResult.dispatchUpdatesTo(this)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(todoItem: TodoItem) {
            val position = adapterPosition
            itemView.tvTitle.text = todoItem.title
            itemView.dueDate.text = todoItem.dueDate
            itemView.doneCheckBox.isChecked = false
            itemView.setOnClickListener() {
                callback.onItemClick(todoItem, position)
            }
            itemView.doneCheckBox.setOnClickListener() {
                callback.onCheckBoxClick(todoItem, position)
            }
        }
    }

    class TodoItemDiffCal(private var oldList: List<TodoItem>, private var newList: List<TodoItem>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]

        }
    }
        fun removeItem(position: Int): TodoItem {
            return todoList[position]
        }
    }


