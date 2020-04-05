package com.notarize.app.views.signed_doc


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.notarize.app.R
import com.notarize.app.db.entities.SignedDoc
import kotlinx.android.synthetic.main.item_signed_doc.view.*

class MySignedDocRecyclerViewAdapter() :
    RecyclerView.Adapter<MySignedDocRecyclerViewAdapter.ViewHolder>() {

    private val _signedDocs = mutableListOf<SignedDoc>()
    var signedDocs: List<SignedDoc>
        get() = _signedDocs
        set(value) {
            _signedDocs.clear()
            _signedDocs.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_signed_doc, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = _signedDocs[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = _signedDocs.size

    inner class ViewHolder(val contentView: View) : RecyclerView.ViewHolder(contentView) {
        val docHashTv: TextView = contentView.tv_doc_hash

        fun bind(signedDoc: SignedDoc) {
            docHashTv.text = signedDoc.hash
        }
    }
}
