package technology.mainthread.apps.gatekeeper.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.databinding.ItemImageBinding;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemImageBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = ItemImageBinding.bind(view);
        }

        public void bindTo(Picasso picasso, Uri imageUri) {
            picasso.load(imageUri).into(binding.image);
        }
    }

    private Picasso picasso;
    private final List<Uri> images;

    public ImagesAdapter(Picasso picasso, List<Uri> images) {
        this.picasso = picasso;
        this.images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindTo(picasso, images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

}
