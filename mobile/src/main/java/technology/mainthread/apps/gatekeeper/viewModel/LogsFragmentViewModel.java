package technology.mainthread.apps.gatekeeper.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.model.firebase.EventLog;
import technology.mainthread.apps.gatekeeper.view.adapter.LogsAdapter;
import technology.mainthread.apps.gatekeeper.view.adapter.LogsViewHolder;
import timber.log.Timber;

public class LogsFragmentViewModel extends BaseObservable {

    private final Context context;
    private final DatabaseReference events;

    @Inject
    LogsFragmentViewModel(Context context, DatabaseReference firebase) {
        this.context = context;
        this.events = firebase.child("door-events");
    }

    public void initialize(RecyclerView logsRecyclerView) {
        Timber.d("init");
        logsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        logsRecyclerView.setHasFixedSize(true);
        Query query = events.orderByChild("timestamp").limitToLast(100);
        LogsAdapter logsAdapter = new LogsAdapter(EventLog.class, R.layout.item_log, LogsViewHolder.class, query);
        logsRecyclerView.setAdapter(logsAdapter);
    }

}
