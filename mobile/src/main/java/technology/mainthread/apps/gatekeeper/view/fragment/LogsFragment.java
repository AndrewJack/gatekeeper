package technology.mainthread.apps.gatekeeper.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.databinding.FragmentLogsBinding;
import technology.mainthread.apps.gatekeeper.view.baseHelpers.DaggerRxFragment;
import technology.mainthread.apps.gatekeeper.viewModel.LogsFragmentViewModel;

public class LogsFragment extends DaggerRxFragment {

    @Inject
    LogsFragmentViewModel viewModel;

    public static Fragment newInstance() {
        return new LogsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentLogsBinding binding = FragmentLogsBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

        viewModel.initialize(binding.logsRecyclerView);
        return binding.getRoot();
    }

}
