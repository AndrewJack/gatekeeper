package technology.mainthread.apps.gatekeeper.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import technology.mainthread.apps.gatekeeper.databinding.FragmentLogsBinding
import technology.mainthread.apps.gatekeeper.viewModel.LogsFragmentViewModel
import javax.inject.Inject

fun buildLogsFragment(): Fragment {
    return LogsFragment()
}

class LogsFragment : DaggerFragment() {

    @Inject
    internal lateinit var viewModel: LogsFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLogsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        viewModel.initialize(binding.logsRecyclerView)
        return binding.root
    }

}
