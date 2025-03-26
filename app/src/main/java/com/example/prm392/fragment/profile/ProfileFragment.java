package com.example.prm392.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.prm392.R;
import com.example.prm392.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    ImageView profileImg;
    EditText name, email, number, address;
    Button update;
    private String password;
    FirebaseUser user;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImg = root.findViewById(R.id.profile_view);
        name = root.findViewById(R.id.profile_name);
        email = root.findViewById(R.id.profile_email);
        number = root.findViewById(R.id.profile_number);
        address = root.findViewById(R.id.profile_address);
        update = root.findViewById(R.id.update);

        // Get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        storageReference = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(user.getUid());

        // Load existing profile data
        loadUserProfile();

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        return root;
    }

    private void loadUserProfile() {
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    DataSnapshot dataSnapshot = task.getResult();
                    password = dataSnapshot.child("password").getValue(String.class);
                    User userProfile = dataSnapshot.getValue(User.class);
                    if (userProfile != null) {
                        name.setText(userProfile.getName());
                        email.setText(userProfile.getEmail());
                        number.setText(userProfile.getPhoneNumber());
                        address.setText(userProfile.getAddress());
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load profile data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUserProfile() {
        String nameText = name.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String numberText = number.getText().toString().trim();
        String addressText = address.getText().toString().trim();

        if (TextUtils.isEmpty(nameText) || TextUtils.isEmpty(emailText) || TextUtils.isEmpty(numberText) || TextUtils.isEmpty(addressText)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("name", nameText);
        userProfile.put("email", emailText);
        userProfile.put("phoneNumber", numberText);
        userProfile.put("address", addressText);
        updateUserInDatabase(userProfile, emailText);
    }

    private void updateUserInDatabase(Map<String, Object> userProfile, String newEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Update the email address
                    user.verifyBeforeUpdateEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Proceed to update user profile in the database
                                databaseReference.updateChildren(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Profile update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Email update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Reauthentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
