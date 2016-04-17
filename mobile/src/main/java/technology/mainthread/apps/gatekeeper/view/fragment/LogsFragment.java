package technology.mainthread.apps.gatekeeper.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.databinding.FragmentLogsBinding;
import technology.mainthread.apps.gatekeeper.viewModel.LogsFragmentViewModel;
import timber.log.Timber;

public class LogsFragment extends RxFragment {

    @Inject
    LogsFragmentViewModel viewModel;

    public static Fragment newInstance() {
        return new LogsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GatekeeperApp.get(getActivity()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("logs fragment");
        FragmentLogsBinding binding = FragmentLogsBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

        viewModel.initialize(binding.logsRecyclerView);
        return binding.getRoot();
    }

}
