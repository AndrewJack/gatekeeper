package technology.mainthread.apps.gatekeeper.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.model.firebase.EventLog;
import technology.mainthread.apps.gatekeeper.view.adapter.LogsAdapter;
import technology.mainthread.apps.gatekeeper.view.adapter.LogsViewHolder;
import timber.log.Timber;

public class LogsFragmentViewModel extends BaseObservable {

    private final Context context;
    private final Firebase events;

    @Inject
    public LogsFragmentViewModel(Context context, Firebase firebase) {
        this.context = context;
        this.events = firebase.child("events");
    }

    public void initialize(RecyclerView logsRecyclerView) {
        Timber.d("init");
        logsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        logsRecyclerView.setHasFixedSize(true);
        Query query = events.orderByValue().limitToLast(100);
        LogsAdapter logsAdapter = new LogsAdapter(EventLog.class, R.layout.item_log, LogsViewHolder.class, query);
        logsRecyclerView.setAdapter(logsAdapter);
    }

}
