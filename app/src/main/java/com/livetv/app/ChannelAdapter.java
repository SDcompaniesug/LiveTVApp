package com.livetv.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.Holder> {
    public interface Listener { void onChannelClick(Channel channel); }
    private final List<Channel> channels;
    private final Listener listener;

    public ChannelAdapter(List<Channel> channels, Listener listener) {
        this.channels = channels;
        this.listener = listener;
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, parent, false);
        return new Holder(view);
    }

    @Override public void onBindViewHolder(@NonNull Holder h, int position) {
        Channel c = channels.get(position);
        h.number.setText(String.format("%02d", position + 1));
        h.name.setText(c.name);
        h.category.setText(c.category);
        h.itemView.setOnClickListener(v -> listener.onChannelClick(c));
    }

    @Override public int getItemCount() { return channels.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView number, name, category;
        Holder(View item) {
            super(item);
            number = item.findViewById(R.id.channelNumber);
            name = item.findViewById(R.id.channelName);
            category = item.findViewById(R.id.channelCategory);
        }
    }
}
