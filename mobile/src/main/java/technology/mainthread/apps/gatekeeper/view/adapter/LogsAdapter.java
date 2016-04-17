package technology.mainthread.apps.gatekeeper.view.adapter;

import android.support.annotation.LayoutRes;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import technology.mainthread.apps.gatekeeper.model.firebase.EventLog;

public class LogsAdapter extends FirebaseRecyclerAdapter<EventLog, LogsViewHolder> {

    public LogsAdapter(Class<EventLog> modelClass, @LayoutRes int modelLayout, Class<LogsViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public LogsAdapter(Class<EventLog> modelClass, @LayoutRes int modelLayout, Class<LogsViewHolder> viewHolderClass, Firebase ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(LogsViewHolder logsViewHolder, EventLog eventLog, int position) {
        logsViewHolder.bindTo(eventLog);
    }

}
