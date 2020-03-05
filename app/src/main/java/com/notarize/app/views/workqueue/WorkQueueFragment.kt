package com.notarize.app.views.workqueue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.notarize.app.R
import kotlinx.android.synthetic.main.fragment_work_queue.*
import org.koin.android.ext.android.inject

class WorkQueueFragment : Fragment() {

    private val queueViewModel: WorkQueueViewModel by inject()
    private lateinit var adapter: WorkQueueAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_work_queue,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = WorkQueueAdapter(LayoutInflater.from(view.context))
        rv_work_queue.layoutManager =
            LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        rv_work_queue.adapter = adapter

        queueViewModel.queueLiveData.observe(viewLifecycleOwner,
            Observer {
                adapter.queue = it
            })
    }
}
