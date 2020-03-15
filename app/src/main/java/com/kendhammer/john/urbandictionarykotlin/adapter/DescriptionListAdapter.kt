package com.kendhammer.john.urbandictionarykotlin.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kendhammer.john.urbandictionarykotlin.R
import com.kendhammer.john.urbandictionarykotlin.model.Definition
import kotlinx.android.synthetic.main.word_layout.view.*
import java.text.SimpleDateFormat


class DescriptionListAdapter(var definitions: ArrayList<Definition>) :
    RecyclerView.Adapter<DescriptionListAdapter.WordListViewHolder>() {

    fun updateWordList(newDefinitionList: List<Definition>) {
        definitions.clear()
        definitions.addAll(newDefinitionList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WordListViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.word_layout, parent, false)
    )

    override fun getItemCount() = definitions.size

    override fun onBindViewHolder(holder: WordListViewHolder, position: Int) {
        val definition = definitions[position]
        holder.bind(definition)

        holder.itemView.btn_liked.setOnClickListener {
            definition.thumbsUp = definition.thumbsUp!!.inc()
            holder.itemView.btn_liked.text = definition.thumbsUp.toString()

        }

        holder.itemView.btn_dislike.setOnClickListener {
            definition.thumbsDown = definition.thumbsDown!!.inc()
            holder.itemView.btn_dislike.text = definition.thumbsDown.toString()

        }

        holder.itemView.sort_likes.setOnClickListener {
            sortLikes(definitions)
        }

        holder.itemView.sort_dislikes.setOnClickListener {
            sortDislikes(definitions)
        }
    }

    private fun sortLikes(definitions: List<Definition>){
        val sortedList = definitions.sortedByDescending { definition -> definition.thumbsUp }
        updateWordList(sortedList)
    }

    private fun sortDislikes(definitions: List<Definition>){
        val sortedList = definitions.sortedByDescending { definition -> definition.thumbsDown }
        updateWordList(sortedList)
    }

    class WordListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val wordView = view.tv_word
        private val exampleView = view.tv_example
        private val definitionView = view.tv_definition
        private val authorView = view.tv_author
        private val buttonLikes = view.btn_liked
        private val buttonDislike = view.btn_dislike
        private val dateView = view.tv_date


        fun bind(definition: Definition) {
            wordView.text = definition.word
            definitionView.text = definition.definition
            authorView.text = definition.author
            exampleView.text = definition.example
            buttonDislike.text = definition.thumbsDown.toString()
            buttonLikes.text = definition.thumbsUp.toString()
            setDate(definition.writtenOn)
        }

        @SuppressLint("SimpleDateFormat")
        fun setDate(writtenOn: String?) {
            val parser = SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("MM/dd/yyyy")
            dateView.text = formatter.format(parser.parse(writtenOn))
        }
    }
}