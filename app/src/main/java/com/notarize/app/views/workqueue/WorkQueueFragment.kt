package com.notarize.app.views.workqueue

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.notarize.app.R
import com.notarize.app.db.entities.WorkStatus
import kotlinx.android.synthetic.main.fragment_work_queue.*
import org.koin.android.ext.android.inject
import timber.log.Timber

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
        adapter = WorkQueueAdapter(LayoutInflater.from(view.context)) { submission ->
            if (submission.status == WorkStatus.SUCCESS) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    Timber.d("Image URI ${submission.uri}")
                    putExtra(Intent.EXTRA_STREAM, submission.uri)
                    type = "image/jpg"
                }

                view.context.startActivity(
                    Intent.createChooser(
                        intent,
                        getString(R.string.send_file)
                    )
                )
            } else {
                Snackbar
                    .make(view, getString(R.string.msg_share_only_signed), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        rv_work_queue.layoutManager =
            LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        rv_work_queue.adapter = adapter

        queueViewModel.queueLiveData.observe(viewLifecycleOwner,
            Observer {
                adapter.queue = it
            })
    }
}
