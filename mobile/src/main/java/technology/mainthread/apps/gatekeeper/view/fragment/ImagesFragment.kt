package technology.mainthread.apps.gatekeeper.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import technology.mainthread.apps.gatekeeper.databinding.FragmentImagesBinding
import technology.mainthread.apps.gatekeeper.view.baseHelpers.DaggerRxFragment
import technology.mainthread.apps.gatekeeper.viewModel.ImagesFragmentViewModel
import javax.inject.Inject

class ImagesFragment : DaggerRxFragment() {

    @Inject
    internal lateinit var viewModel: ImagesFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentImagesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        viewModel.initialise(binding.imagesRecyclerView)
        return binding.root
    }

}
