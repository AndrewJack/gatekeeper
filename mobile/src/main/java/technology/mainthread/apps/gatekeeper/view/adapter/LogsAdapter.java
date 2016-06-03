package technology.mainthread.apps.gatekeeper.view.adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import technology.mainthread.apps.gatekeeper.model.firebase.EventLog;

public class LogsAdapter extends FirebaseRecyclerAdapter<EventLog, LogsViewHolder> {

    public LogsAdapter(Class<EventLog> modelClass, int modelLayout, Class<LogsViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public LogsAdapter(Class<EventLog> modelClass, int modelLayout, Class<LogsViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(LogsViewHolder logsViewHolder, EventLog eventLog, int position) {
        logsViewHolder.bindTo(eventLog);
    }

}
