package technology.mainthread.apps.gatekeeper.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import technology.mainthread.apps.gatekeeper.data.AppStateController
import technology.mainthread.apps.gatekeeper.databinding.FragmentUnlockBinding
import technology.mainthread.apps.gatekeeper.viewModel.UnlockFragmentViewModel
import javax.inject.Inject

fun buildUnlockFragment(): Fragment {
    return UnlockFragment()
}

class UnlockFragment : DaggerFragment() {

    @Inject
    internal lateinit var appStateController: AppStateController

    private var viewModel: UnlockFragmentViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentUnlockBinding.inflate(inflater, container, false)
        viewModel = UnlockFragmentViewModel(context as Context, binding.root, appStateController)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel?.initialize()
    }

    override fun onDestroyView() {
        viewModel?.dispose()
        super.onDestroyView()
    }

}
