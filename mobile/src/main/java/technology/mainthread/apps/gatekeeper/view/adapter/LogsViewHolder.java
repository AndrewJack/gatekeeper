package technology.mainthread.apps.gatekeeper.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import technology.mainthread.apps.gatekeeper.databinding.ItemLogBinding;
import technology.mainthread.apps.gatekeeper.model.firebase.EventLog;

public class LogsViewHolder extends RecyclerView.ViewHolder {

    private final ItemLogBinding binding;

    public LogsViewHolder(View view) {
        super(view);
        binding = ItemLogBinding.bind(view);
    }

    public void bindTo(EventLog event) {
        binding.setLog(event);
    }
}
