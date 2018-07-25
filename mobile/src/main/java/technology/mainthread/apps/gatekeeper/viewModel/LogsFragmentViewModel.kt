package technology.mainthread.apps.gatekeeper.viewModel

import androidx.databinding.BaseObservable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import technology.mainthread.apps.gatekeeper.R
import technology.mainthread.apps.gatekeeper.model.firebase.EventLog
import technology.mainthread.apps.gatekeeper.view.adapter.LogsAdapter
import technology.mainthread.apps.gatekeeper.view.adapter.LogsViewHolder
import javax.inject.Inject

class LogsFragmentViewModel @Inject
internal constructor(database: FirebaseDatabase) : BaseObservable() {

    private val events: DatabaseReference = database.reference.child("events")

    fun initialize(logsRecyclerView: RecyclerView) {
        logsRecyclerView.layoutManager = LinearLayoutManager(logsRecyclerView.context)
        logsRecyclerView.setHasFixedSize(true)
        val query = events.orderByChild("sort").limitToFirst(100)
        val logsAdapter = LogsAdapter(EventLog::class.java, R.layout.item_log, LogsViewHolder::class.java, query)
        logsRecyclerView.adapter = logsAdapter
    }

}
