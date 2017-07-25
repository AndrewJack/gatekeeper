package technology.mainthread.apps.gatekeeper.view.adapter

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.Query
import technology.mainthread.apps.gatekeeper.model.firebase.EventLog

class LogsAdapter(modelClass: Class<EventLog>, modelLayout: Int, viewHolderClass: Class<LogsViewHolder>, ref: Query) : FirebaseRecyclerAdapter<EventLog, LogsViewHolder>(modelClass, modelLayout, viewHolderClass, ref) {

    override fun populateViewHolder(logsViewHolder: LogsViewHolder, eventLog: EventLog, position: Int) {
        logsViewHolder.bindTo(eventLog)
    }

}
