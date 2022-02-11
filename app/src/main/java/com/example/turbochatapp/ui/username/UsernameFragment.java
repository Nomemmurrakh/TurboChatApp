package com.example.turbochatapp.ui.username;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.turbochatapp.R;
import com.example.turbochatapp.databinding.FragmentUsernameBinding;
import com.example.turbochatapp.models.Group;

public class UsernameFragment extends Fragment {

    private FragmentUsernameBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsernameBinding.inflate(getLayoutInflater());

        Group group = UsernameFragmentArgs.fromBundle(getArguments()).getGroup();

        binding.btnDone.setOnClickListener(v -> {

            String username = binding.editUsername.getText().toString();

            Navigation.findNavController(v)
                    .navigate(
                            UsernameFragmentDirections.actionUsernameFragmentToMessagelist(group,username)
                    );
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                navController.getGraph()
        ).build();

        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
    }
}