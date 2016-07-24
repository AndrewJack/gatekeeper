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
import technology.mainthread.apps.gatekeeper.databinding.FragmentImagesBinding;
import technology.mainthread.apps.gatekeeper.viewModel.ImagesFragmentViewModel;

public class ImagesFragment extends RxFragment {

    @Inject
    ImagesFragmentViewModel viewModel;

    public static Fragment newInstance() {
        return new ImagesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GatekeeperApp.get(getActivity()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentImagesBinding binding = FragmentImagesBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

        viewModel.initialise(binding.imagesRecyclerView);
        return binding.getRoot();
    }
}
