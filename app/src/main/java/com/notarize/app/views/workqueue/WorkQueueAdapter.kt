package com.notarize.app.views.workqueue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.notarize.app.R
import com.notarize.app.db.entities.WorkStatus
import com.notarize.app.db.entities.WorkSubmission
import com.notarize.app.ext.format
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_work_submission.view.*

class WorkQueueAdapter(
    private val layoutInflater: LayoutInflater,
    private val onItemClickListener: (WorkSubmission) -> Unit
) :
    RecyclerView.Adapter<WorkQueueAdapter.WorkQueueItemViewHolder>() {

    private val _queue = mutableListOf<WorkSubmission>()

    var queue: List<WorkSubmission>?
        set(value) {
            _queue.clear()
            if (value != null)
                _queue.addAll(value)
            notifyDataSetChanged()
        }
        get() = _queue

    inner class WorkQueueItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(queueItem: WorkSubmission) {
            containerView.tv_submission_hash.text = queueItem.hash
            containerView.tv_submission_date.text = queueItem.submittedOn.format()
            containerView.img_status.setImageResource(
                when (queueItem.status) {
                    WorkStatus.PENDING -> R.drawable.ic_cloud_waiting
                    WorkStatus.FAILED -> R.drawable.ic_cloud_error
                    WorkStatus.SUCCESS -> R.drawable.ic_success
                    WorkStatus.SHARED -> R.drawable.ic_success
                }
            )

            containerView
                .img_status
                .setColorFilter(
                    when (queueItem.status) {
                        WorkStatus.SHARED -> containerView.context.getColor(R.color.green_effective)
                        else -> containerView.context.getColor(R.color.black_effective)
                    }
                )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkQueueItemViewHolder {
        return WorkQueueItemViewHolder(
            layoutInflater.inflate(
                R.layout.item_work_submission,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = _queue.size

    override fun onBindViewHolder(holder: WorkQueueItemViewHolder, position: Int) {
        holder.bind(_queue[position])
        holder.containerView.setOnClickListener {
            onItemClickListener(_queue[position])
        }
    }
}