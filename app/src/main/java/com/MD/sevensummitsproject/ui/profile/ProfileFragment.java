package com.MD.sevensummitsproject.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.MD.sevensummitsproject.RegisterActivity;
import com.MD.sevensummitsproject.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private EditText name, vorname, email, straße, hr, ort, plz, datum, telefon;
    private FloatingActionButton btnEdit;
    private Button saveChange;
    FirebaseFirestore db;
    FirebaseAuth currentUser;
    String userID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance();

        name = binding.ProfilName;
        vorname = binding.ProfilVorname;
        email = binding.ProfilEmail;
        straße = binding.ProfilStrasse;
        hr = binding.ProfilHNR;
        ort = binding.ProfilOrt;
        plz = binding.ProfilPLZ;
        datum = binding.ProfilGebDatum;
        btnEdit = binding.ProfilEdit;
        saveChange = binding.SaveChanges;
        telefon = binding.ProfilTelefonnummer;

        name.setText("");
        name.setFocusable(false);

        vorname.setText("");
        vorname.setFocusable(false);

        email.setText("");
        email.setFocusable(false);

        straße.setText("");
        straße.setFocusable(false);

        hr.setText("");
        hr.setFocusable(false);

        ort.setText("");
        ort.setFocusable(false);

        plz.setText("");
        plz.setFocusable(false);

        datum.setText("");
        datum.setFocusable(false);

        telefon.setText("");
        telefon.setFocusable(false);

        btnEdit.setVisibility(View.VISIBLE);
        saveChange.setVisibility(View.GONE);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setFocusableInTouchMode(true);
                vorname.setFocusableInTouchMode(true);
                straße.setFocusableInTouchMode(true);
                hr.setFocusableInTouchMode(true);
                plz.setFocusableInTouchMode(true);
                ort.setFocusableInTouchMode(true);
                telefon.setFocusableInTouchMode(true);
                saveChange.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.GONE);
            }
        });


        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setFocusable(false);
                vorname.setFocusable(false);
                straße.setFocusable(false);
                hr.setFocusable(false);
                plz.setFocusable(false);
                ort.setFocusable(false);
                telefon.setFocusable(false);
                saveChange.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                putUserData();
                getUserData();
            }
        });

        getUserData();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getUserData(){
        userID = currentUser.getCurrentUser().getUid();

        DocumentReference doc = db.collection("users").document(userID);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                email.setText(value.getString("email"));
                if(value.getString("vorname") != null){
                    vorname.setText(value.getString("vorname"));
                }
                if(value.getString("name") != null){
                    name.setText(value.getString("name"));
                }
                if(value.getString("strasse") != null){
                    straße.setText(value.getString("strasse"));
                }
                if(value.getString("ort") != null){
                    ort.setText(value.getString("ort"));
                }
                if(value.getString("plz") != null){
                    plz.setText(value.getString("plz"));
                }
                if(value.getString("hnr") != null){
                    hr.setText(value.getString("hnr"));
                }
                if(value.getString("telefon") != null) {
                    telefon.setText(value.getString("telefon"));
                }
            }
        });
    }

    public void putUserData(){
        userID = currentUser.getCurrentUser().getUid();

        DocumentReference doc = db.collection("users").document(userID);
        String nachname = name.getText().toString().trim();
        String cVorname = vorname.getText().toString().trim();
        String cOrt = ort.getText().toString().trim();
        String cPLZ = plz.getText().toString().trim();
        String strasse = straße.getText().toString().trim();
        String hnr = hr.getText().toString().trim();
        String phone = telefon.getText().toString().trim();

        Map<String, Object> uUser = new HashMap<>();
        uUser.put("name", nachname);
        uUser.put("vorname", cVorname);
        uUser.put("ort", cOrt);
        uUser.put("plz", cPLZ);
        uUser.put("strasse", strasse);
        uUser.put("hnr", hnr);
        uUser.put("telefon", phone);

        doc.update(uUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Daten wurden erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),"Bei der Aktualisierung ist ein Fehler aufgetreten!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}