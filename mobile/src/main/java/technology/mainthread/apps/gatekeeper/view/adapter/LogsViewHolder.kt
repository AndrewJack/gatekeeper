package technology.mainthread.apps.gatekeeper.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

import technology.mainthread.apps.gatekeeper.databinding.ItemLogBinding
import technology.mainthread.apps.gatekeeper.model.firebase.EventLog

class LogsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ItemLogBinding = ItemLogBinding.bind(view)

    fun bindTo(event: EventLog) {
        binding.log = event
    }
}
