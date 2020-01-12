package com.pjct.ddb;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.pjct.ddb.Entities.Parcel;

import java.util.List;


public class ParcelsHistory extends AppCompatActivity{

    public RecyclerView studentsRecycleView;
    private List<Parcel> students;


    public void Toasts(){
        Toast.makeText(getApplicationContext(),"rtg",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcels_history);

        studentsRecycleView = findViewById(R.id.studentsRecycleView);
        studentsRecycleView.setHasFixedSize(true);
        studentsRecycleView.setLayoutManager(new LinearLayoutManager(this));


        Firebase_DBManager.notifyToParcelList(new Firebase_DBManager.NotifyDataChange<List<Parcel>>() {
            @Override
            public void OnDataChanged(List<Parcel> obj) {
                if (studentsRecycleView.getAdapter() == null) {
                    students = obj;
                    studentsRecycleView.setAdapter(new StudentsRecycleViewAdapter(ParcelsHistory.this, students));
                } else
                    studentsRecycleView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getBaseContext(), "error to get parcels list\n" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Firebase_DBManager.stopNotifyToParcelList();
        super.onDestroy();
    }



}
