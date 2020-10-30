package com.digital.bdadmin.CatagoryPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.digital.bdadmin.DataManager;
import com.digital.bdadmin.Model.CatagoryList;
import com.digital.bdadmin.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Services extends AppCompatActivity {

    private FloatingActionButton addbutton;
    private DatabaseReference DailyInstumentDatabase;
    private RecyclerView catagorylist;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services);


        catagorylist = findViewById(R.id.CatagoryListID);
        catagorylist.setHasFixedSize(true);
        catagorylist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        DailyInstumentDatabase = FirebaseDatabase.getInstance().getReference().child(DataManager.Services);

        addbutton = findViewById(R.id.DailyInstumentButton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialAlertDialogBuilder Mbuilder = new MaterialAlertDialogBuilder(Services.this);
                View Mview = LayoutInflater.from(Services.this).inflate(R.layout.input_subcatagory, null, false);
                Mbuilder.setView(Mview);

                EditText catagoryname = Mview.findViewById(R.id.SubcatagoryInput);
                MaterialButton insertbutton = Mview.findViewById(R.id.InsertButtonID);

                insertbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String subcatagry = catagoryname.getText().toString().trim();
                        if (subcatagry.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter category name", Toast.LENGTH_LONG).show();
                        } else {
                            Map<String, Object> categorymap = new HashMap<String, Object>();
                            categorymap.put(DataManager.CategoryName, subcatagry);
                            categorymap.put(DataManager.CategoryTitle, subcatagry);
                            categorymap.put(DataManager.Type, DataManager.Services);

                            DailyInstumentDatabase.push().updateChildren(categorymap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), subcatagry + " is added", Toast.LENGTH_LONG).show();
                                                catagoryname.setText("");

                                            } else {
                                                catagoryname.setText("");
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            catagoryname.setText("");
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });


                AlertDialog alertDialog = Mbuilder.create();
                alertDialog.show();
            }
        });

        showcatagory();

    }

    private void showcatagory() {

        FirebaseRecyclerAdapter<CatagoryList, DailyInstument.CatagoryHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CatagoryList, DailyInstument.CatagoryHolder>(
                CatagoryList.class,
                R.layout.subcatagory,
                DailyInstument.CatagoryHolder.class,
                DailyInstumentDatabase
        ) {
            @Override
            protected void populateViewHolder(DailyInstument.CatagoryHolder catagoryHolder, CatagoryList catagoryList, int i) {
                String UID = getRef(i).getKey();
                DailyInstumentDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    if (dataSnapshot.hasChild(DataManager.CategoryTitle)) {
                                        String title = dataSnapshot.child(DataManager.CategoryTitle).getValue().toString();
                                        catagoryHolder.setCatagorynameset(title);
                                    }


                                    catagoryHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {


                                            MaterialAlertDialogBuilder Mbuilder = new MaterialAlertDialogBuilder(Services.this);
                                            View Mview = LayoutInflater.from(Services.this).inflate(R.layout.catagory_control, null, false);

                                            Mbuilder.setView(Mview);

                                            AlertDialog alertDialog = Mbuilder.create();
                                            alertDialog.show();


                                            EditText name = Mview.findViewById(R.id.CatagorynameID);
                                            MaterialButton update = Mview.findViewById(R.id.UpdateButtonID);
                                            MaterialButton remove = Mview.findViewById(R.id.RemoveID);


                                            DailyInstumentDatabase.child(UID)
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                if (dataSnapshot.hasChild(DataManager.CategoryTitle)) {
                                                                    title = dataSnapshot.child(DataManager.CategoryTitle).getValue().toString();
                                                                    name.setText(title);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                            update.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String titletext = name.getText().toString().trim();
                                                    if (titletext.isEmpty()) {
                                                        Toast.makeText(getApplicationContext(), "Name require", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        DailyInstumentDatabase.child(UID).child(DataManager.CategoryTitle).setValue(titletext)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            alertDialog.dismiss();
                                                                            Toast.makeText(getApplicationContext(), "Data is update", Toast.LENGTH_LONG).show();
                                                                        } else {
                                                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        alertDialog.dismiss();
                                                                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                    }
                                                }
                                            });

                                            remove.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    DailyInstumentDatabase.child(UID).removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        alertDialog.dismiss();
                                                                        Toast.makeText(getApplicationContext(), "Data is remove success", Toast.LENGTH_LONG).show();
                                                                    } else {
                                                                        alertDialog.dismiss();
                                                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    alertDialog.dismiss();
                                                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                }
                                            });

                                            return true;
                                        }
                                    });

                                } else {

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };

        catagorylist.setAdapter(firebaseRecyclerAdapter);

    }

    public static class CatagoryHolder extends RecyclerView.ViewHolder {

        private MaterialTextView catagoryname;

        public CatagoryHolder(@NonNull View itemView) {
            super(itemView);

            catagoryname = itemView.findViewById(R.id.CatagoryName);
        }

        public void setCatagorynameset(String nam) {
            catagoryname.setText(nam);
        }
    }
}