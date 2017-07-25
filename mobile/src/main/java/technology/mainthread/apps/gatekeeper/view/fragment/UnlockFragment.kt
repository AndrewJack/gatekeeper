package technology.mainthread.apps.gatekeeper.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import technology.mainthread.apps.gatekeeper.data.AppStateController
import technology.mainthread.apps.gatekeeper.databinding.FragmentUnlockBinding
import technology.mainthread.apps.gatekeeper.model.event.AppEvent
import technology.mainthread.apps.gatekeeper.view.baseHelpers.DaggerRxFragment
import technology.mainthread.apps.gatekeeper.viewModel.UnlockFragmentViewModel

fun buildUnlockFragment(): Fragment {
    return UnlockFragment()
}

class UnlockFragment : DaggerRxFragment() {

    @Inject
    internal lateinit var appStateController: AppStateController

    private lateinit var viewModel: UnlockFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentUnlockBinding.inflate(inflater, container, false)
        viewModel = UnlockFragmentViewModel(context, binding.root, appStateController, this.bindToLifecycle<AppEvent>())
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initialize()
    }

}
