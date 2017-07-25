package technology.mainthread.apps.gatekeeper.viewModel

import android.content.res.Resources
import android.databinding.BaseObservable
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import technology.mainthread.apps.gatekeeper.R
import technology.mainthread.apps.gatekeeper.view.adapter.ImagesAdapter
import javax.inject.Inject

class ImagesFragmentViewModel @Inject
constructor(storage: FirebaseStorage, private val picasso: Picasso, resources: Resources) : BaseObservable() {

    private val storageRef: StorageReference = storage.getReferenceFromUrl(resources.getString(R.string.bucket_name))

    fun initialise(imagesRecyclerView: RecyclerView) {
        imagesRecyclerView.layoutManager = LinearLayoutManager(imagesRecyclerView.context)
        imagesRecyclerView.setHasFixedSize(true)

        storageRef.child("0cc05036-635c-4d0b-8ed2-517a392ec2f3.jpg").downloadUrl.addOnSuccessListener { uri ->
            val imagesAdapter = ImagesAdapter(picasso, listOf<Uri>(uri))
            imagesRecyclerView.adapter = imagesAdapter
        }
    }

}
