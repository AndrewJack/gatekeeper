package technology.mainthread.apps.gatekeeper.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.databinding.FragmentImagesBinding;
import technology.mainthread.apps.gatekeeper.view.baseHelpers.DaggerRxFragment;
import technology.mainthread.apps.gatekeeper.viewModel.ImagesFragmentViewModel;

public class ImagesFragment extends DaggerRxFragment {

    @Inject
    ImagesFragmentViewModel viewModel;

    public static Fragment newInstance() {
        return new ImagesFragment();
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
