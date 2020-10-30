package com.digital.bdadmin.LoginAndSplashScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.digital.bdadmin.HomePage.HomePage;
import com.digital.bdadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends Fragment {

    private EditText email, password;
    private RelativeLayout loginbutton;
    private ProgressBar loginprogress;
    private FirebaseAuth Mauth;
    private LinearLayout design;

    public LoginPage() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_page, container, false);

        design = view.findViewById(R.id.LiniLayout);
        Mauth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.EmailID);
        password = view.findViewById(R.id.PasswordID);
        loginprogress = view.findViewById(R.id.LoginProgressID);
        loginbutton = view.findViewById(R.id.LoginButtonID);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailtext = email.getText().toString().trim();
                String passwordtext = password.getText().toString().trim();
                if(emailtext.isEmpty()){
                    Toast.makeText(getActivity(), "Email require", Toast.LENGTH_LONG).show();
                }
                else if(passwordtext.isEmpty()){
                    Toast.makeText(getActivity(), "Password require", Toast.LENGTH_LONG).show();
                }
                else {
                    loginbutton.setVisibility(View.GONE);
                    loginprogress.setVisibility(View.VISIBLE);

                    Mauth.signInWithEmailAndPassword(emailtext, passwordtext)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_LONG).show();

                                        goto_homepage();
                                    }
                                    else {
                                        loginbutton.setVisibility(View.VISIBLE);
                                        loginprogress.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loginbutton.setVisibility(View.VISIBLE);
                                    loginprogress.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                }
                            });

                }
            }
        });

        return view;
    }

    private void goto_homepage(){
        Intent intent = new Intent(getActivity(), HomePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onStart() {

        FirebaseUser Muser = Mauth.getCurrentUser();
        if(Muser != null){
            design.setVisibility(View.GONE);
            goto_homepage();
        }
        else {
            design.setVisibility(View.VISIBLE);
        }

        super.onStart();
    }
}