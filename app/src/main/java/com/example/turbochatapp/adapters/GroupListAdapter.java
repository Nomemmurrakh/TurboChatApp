package com.example.turbochatapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turbochatapp.R;
import com.example.turbochatapp.databinding.LayoutGroupItemBinding;
import com.example.turbochatapp.models.Group;
import com.example.turbochatapp.ui.group.GroupListFragmentDirections;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class GroupListAdapter extends ListAdapter<Group, RecyclerView.ViewHolder> {

    private static final DiffUtil.ItemCallback<Group> CALLBACK = new DiffUtil.ItemCallback<Group>() {
        @Override
        public boolean areItemsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
            return oldItem.equals(newItem);
        }
    };

    public GroupListAdapter() {
        super(CALLBACK);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isShimmer() ? 1 : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            return new GroupShimmerHolder(
                    LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_group_item_shimmer, parent, false)
            );
        }else {
            return new GroupHolder(
                    LayoutGroupItemBinding.inflate(
                            LayoutInflater.from(
                                    parent.getContext()
                            ),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 0){
            GroupHolder groupHolder = (GroupHolder) holder;
            Group group = getItem(position);
            groupHolder.bindWith(group);
        }
    }

    static class GroupShimmerHolder extends RecyclerView.ViewHolder{

        public GroupShimmerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class GroupHolder extends RecyclerView.ViewHolder{

        private LayoutGroupItemBinding binding;
        private SimpleDateFormat simpleDateFormat;

        public GroupHolder(@NonNull LayoutGroupItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            this.simpleDateFormat = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
        }

        public void bindWith(Group group) {
            binding.txtGroupName.setText(group.getName());
            binding.txtGroupSize.setText(String.valueOf(group.getSize()));
            Picasso.get()
                    .load(group.getDisplayPicture())
                    .fit()
                    .into(binding.imgGroup);
            binding.txtGroupCreated.setText(
                    simpleDateFormat.format(
                            group.getDateCreated()
                            .toDate()
                    )
            );

            itemView.setOnClickListener(v -> {
                Navigation.findNavController(v)
                        .navigate(
                                GroupListFragmentDirections.actionGrouplistToUsernameFragment(group)
                        );
            });
        }
    }
}
