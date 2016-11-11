package technology.mainthread.apps.gatekeeper.viewModel;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Collections;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.view.adapter.ImagesAdapter;

public class ImagesFragmentViewModel extends BaseObservable {

    private StorageReference storageRef;
    private Picasso picasso;

    @Inject
    public ImagesFragmentViewModel(FirebaseStorage storage, Picasso picasso, Resources resources) {
        this.storageRef = storage.getReferenceFromUrl(resources.getString(R.string.bucket_name));
        this.picasso = picasso;
    }

    public void initialise(RecyclerView imagesRecyclerView) {
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(imagesRecyclerView.getContext()));
        imagesRecyclerView.setHasFixedSize(true);

        storageRef.child("0cc05036-635c-4d0b-8ed2-517a392ec2f3.jpg").getDownloadUrl().addOnSuccessListener(uri -> {
            ImagesAdapter imagesAdapter = new ImagesAdapter(picasso, Collections.singletonList(uri));
            imagesRecyclerView.setAdapter(imagesAdapter);
        });
    }

}
