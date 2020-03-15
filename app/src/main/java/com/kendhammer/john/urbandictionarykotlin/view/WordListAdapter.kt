package com.kendhammer.john.urbandictionarykotlin.view

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kendhammer.john.urbandictionarykotlin.R
import com.kendhammer.john.urbandictionarykotlin.model.Definition
import kotlinx.android.synthetic.main.word_layout.view.*
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat


class WordListAdapter(var definitions: ArrayList<Definition>) :
    RecyclerView.Adapter<WordListAdapter.WordListViewHolder>() {

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
            var thumbsUpCount = definition.thumbsUp
            if (thumbsUpCount != null) {
                definition.thumbsUp = thumbsUpCount++
                holder.itemView.btn_liked.text = thumbsUpCount.toString()
            }
        }

        holder.itemView.btn_dislike.setOnClickListener {
            var thumbsDownCount = definition.thumbsDown
            if (thumbsDownCount != null) {
                definition.thumbsDown = thumbsDownCount++
                holder.itemView.btn_dislike.text = "$thumbsDownCount"
            }
        }

        holder.itemView.btn_play.setOnClickListener {
            val uri = definition.soundUrls!![0]
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(uri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                mediaPlayer.setOnPreparedListener(MediaPlayer.OnPreparedListener {
                    mediaPlayer.prepareAsync()
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                })

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
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