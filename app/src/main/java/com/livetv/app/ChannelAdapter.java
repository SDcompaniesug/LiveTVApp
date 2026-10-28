package com.livetv.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.Holder> {
    public interface Listener { void onChannelClick(Channel channel); }

    private final List<Channel> channels = new ArrayList<>();
    private final Listener listener;
    private final Set<String> favorites = new HashSet<>();

    public ChannelAdapter(List<Channel> channels, Listener listener) {
        this.channels.addAll(channels);
        this.listener = listener;
    }

    public void setChannels(List<Channel> updated) {
        channels.clear();
        channels.addAll(updated);
        notifyDataSetChanged();
    }

    public Set<String> getFavorites() {
        return new HashSet<>(favorites);
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, parent, false);
        return new Holder(view);
    }

    @Override public void onBindViewHolder(@NonNull Holder h, int position) {
        Channel channel = channels.get(position);
        h.name.setText(channel.name);
        h.category.setText(channel.category);
        h.logo.setImageResource(R.drawable.default_image);
        updateFavoriteIcon(h.favorite, channel.name);

        h.itemView.setOnClickListener(v -> listener.onChannelClick(channel));
        h.favorite.setOnClickListener(v -> {
            if (!favorites.add(channel.name)) favorites.remove(channel.name);
            updateFavoriteIcon(h.favorite, channel.name);
        });
    }

    private void updateFavoriteIcon(ImageView image, String name) {
        image.setImageResource(favorites.contains(name)
                ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        image.setContentDescription(favorites.contains(name) ? "Remove favorite" : "Add favorite");
    }

    @Override public int getItemCount() { return channels.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView name, category;
        final ImageView logo, favorite;
        Holder(View item) {
            super(item);
            name = item.findViewById(R.id.channelName);
            category = item.findViewById(R.id.channelCategory);
            logo = item.findViewById(R.id.channelLogo);
            favorite = item.findViewById(R.id.favoriteIcon);
        }
    }
}