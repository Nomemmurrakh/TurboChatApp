package com.example.turbochatapp.ui.group;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.turbochatapp.R;
import com.example.turbochatapp.adapters.GroupListAdapter;
import com.example.turbochatapp.animators.CustomItemAnimator;
import com.example.turbochatapp.databinding.FragmentGroupListBinding;
import com.example.turbochatapp.models.Group;
import com.example.turbochatapp.utils.Paths;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment {

    private FragmentGroupListBinding binding;
    private GroupListAdapter adapter;
    private List<Group> groups;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
        setReenterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroupListBinding.inflate(inflater);


        adapter = new GroupListAdapter();
        binding.getRoot().setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.getRoot().setAdapter(adapter);

        groups = new ArrayList<>();
        Group placeholder = new Group();
        Group placeholder2 = new Group();
        Group placeholder3 = new Group();
        groups.add(placeholder);
        groups.add(placeholder2);
        groups.add(placeholder3);

        adapter.submitList(groups);

        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#DC143C"));

        Drawable drawable = ActivityCompat.getDrawable(requireContext(), R.drawable.ic_baseline_delete_24);
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();

        int margin = 20;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (dX > 0){
                    long x = Math.round((Math.floor(dX) / width) * 60);
                    ((MaterialCardView)viewHolder.itemView).setRadius(x);


                    View view = viewHolder.itemView;

                    int marginTop = ((view.getHeight() - drawableHeight) / 2) + view.getTop();

                    colorDrawable.setBounds(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    colorDrawable.draw(c);

                    drawable.setBounds(margin, marginTop, drawableWidth + margin, drawableHeight + marginTop);
                    drawable.draw(c);


                }
            }
        }).attachToRecyclerView(binding.getRoot());

        FirebaseFirestore.getInstance()
                .collection(Paths.GROUP_PATH)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) handleQuerySnapshot(task.getResult());
                });

        return binding.getRoot();
    }

    private void handleQuerySnapshot(QuerySnapshot result) {
        if (result != null){
            List<Group> groupList = new ArrayList<>();
            List<DocumentSnapshot> documentSnapshots = result.getDocuments();
            for(int i = 0; i < documentSnapshots.size(); i++){
                DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                Group group = documentSnapshot.toObject(Group.class);
                group.setDocumentId(documentSnapshot.getId());

                if (i < groups.size()) {
                    group.setId(groups.get(i).getId());
                }
                groupList.add(group);
            }
            adapter.submitList(groupList);
        }
        else
            Toast.makeText(requireContext(), "No Groups Found.", Toast.LENGTH_LONG).show();
    }
}