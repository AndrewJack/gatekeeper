package technology.mainthread.apps.gatekeeper.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import technology.mainthread.apps.gatekeeper.R

fun buildComingSoonFragment(): Fragment {
    return ComingSoonFragment()
}

class ComingSoonFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_coming_soon, container, false)
    }

}
