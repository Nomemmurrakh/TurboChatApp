package com.example.turbochatapp.ui.message;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.turbochatapp.R;
import com.example.turbochatapp.adapters.MessageListAdapter;
import com.example.turbochatapp.databinding.FragmentMessageListBinding;
import com.example.turbochatapp.models.Group;
import com.example.turbochatapp.models.Message;
import com.example.turbochatapp.utils.Paths;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageListFragment extends Fragment {

    private FragmentMessageListBinding binding;

    private MessageListAdapter adapter;
    private String username;
    private Group group;

    private ListenerRegistration listenerRegistration;
    private InputMethodManager inputManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MessageListFragmentArgs args = MessageListFragmentArgs.fromBundle(getArguments());
        username = args.getUsername();
        group = args.getGroup();

        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
        setReturnTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageListBinding.inflate(inflater);

        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.messagesToolbar);

        inputManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        adapter = new MessageListAdapter(username);
        binding.listMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listMessages.setAdapter(adapter);

        listenerRegistration = FirebaseFirestore.getInstance()
                .collection(Paths.GROUP_PATH)
                .document(group.getDocumentId())
                .collection("chat")
                .orderBy("timestamp")
                .addSnapshotListener((snapshot, err) -> handleQuerySnapshot(snapshot));

        binding.btnSend.setOnClickListener(v -> {
            String content = binding.editMessage.getText().toString();
            Message message = new Message();
            message.setContent(content);
            message.setSender(username);
            message.setTimestamp(Timestamp.now());

            FirebaseFirestore.getInstance()
                    .collection(Paths.GROUP_PATH)
                    .document(group.getDocumentId())
                    .collection("chat")
                    .add(message);

            binding.editMessage.setText("");
            binding.editMessage.clearFocus();
            hideKeyboard();
        });

        Picasso.get()
                .load(group.getDisplayPicture())
                .fit()
                .into(binding.imgGroup);

        binding.txtGroupName.setText(group.getName());

        return binding.getRoot();
    }

    private void hideKeyboard() {
        IBinder windowToken = requireActivity().getCurrentFocus().getWindowToken();
        if (windowToken != null){
            inputManager.hideSoftInputFromWindow(windowToken, 0);
        }
    }

    private void handleQuerySnapshot(QuerySnapshot snapshot) {
        if (snapshot != null){
            List<Message> messages = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : snapshot.getDocuments()){
                Message message = documentSnapshot.toObject(Message.class);
                message.setId(documentSnapshot.getId());
                messages.add(message);
            }
            adapter.submitList(messages);
            int size = messages.size() - 1;
            if (size > 0) binding.listMessages.smoothScrollToPosition(size);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listenerRegistration.remove();
    }
}