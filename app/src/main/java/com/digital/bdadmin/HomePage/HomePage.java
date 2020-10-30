package com.digital.bdadmin.HomePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.digital.bdadmin.BottomPage.Home;
import com.digital.bdadmin.MainActivity;
import com.digital.bdadmin.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    private FirebaseAuth Mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);


        goto_home(new Home());


    }

    private void goto_home(Fragment fragment){
        if(fragment != null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.HomeFream, fragment);
            transaction.commit();
        }
    }
}