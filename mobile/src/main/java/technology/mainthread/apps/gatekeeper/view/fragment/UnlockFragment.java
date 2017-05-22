package technology.mainthread.apps.gatekeeper.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.data.AppStateController;
import technology.mainthread.apps.gatekeeper.databinding.FragmentUnlockBinding;
import technology.mainthread.apps.gatekeeper.view.baseHelpers.DaggerRxFragment;
import technology.mainthread.apps.gatekeeper.viewModel.UnlockFragmentViewModel;

public class UnlockFragment extends DaggerRxFragment {

    @Inject
    AppStateController appStateController;

    private UnlockFragmentViewModel viewModel;

    public static Fragment newInstance() {
        return new UnlockFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentUnlockBinding binding = FragmentUnlockBinding.inflate(inflater, container, false);
        viewModel = new UnlockFragmentViewModel(getContext(), binding.getRoot(), appStateController, this.bindToLifecycle());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.initialize();
    }
}
