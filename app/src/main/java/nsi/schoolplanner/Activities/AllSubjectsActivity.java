package nsi.schoolplanner.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import nsi.schoolplanner.Adapters.SubjectsAdapter;
import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.Subject;
import nsi.schoolplanner.R;

public class AllSubjectsActivity extends AppCompatActivity implements View.OnClickListener {

    private Dialog subjectDialog;
    private Dialog deleteDialog;
    private Button addSubject;
    private Button cancelSubject;
    private Button deleteSubject;
    private Button cancelDelete;
    private EditText subjectName;
    private List<Subject> subjects;
    private RecyclerView recyclerView;
    private SubjectsAdapter adapter;
    public static String subjectToDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_subjects);

        getSupportActionBar().setTitle(getResources().getString(R.string.subjects));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subjectDialog= new Dialog(this);
        subjectDialog.setContentView(R.layout.subject_dialog);

        deleteDialog= new Dialog(this);
        deleteDialog.setContentView(R.layout.delete_subject_dialog);


        setFloatingActionButton();
        findViewById();
        setOnClickListener();

        subjects=new ArrayList<>();
        if(subjectToDelete!=null){

            openDeleteDialog();
        }
        setRecycleViewSubjects();
    }

    private void findViewById() {

        addSubject=subjectDialog.findViewById(R.id.add_subject);
        cancelSubject=subjectDialog.findViewById(R.id.cancel_subject);
        subjectName=subjectDialog.findViewById(R.id.subject_name_text);
        recyclerView=findViewById(R.id.recycle_view_subjects);
        deleteSubject=deleteDialog.findViewById(R.id.delete_subject);
        cancelDelete=deleteDialog.findViewById(R.id.cancel_delete);
    }

    private void setOnClickListener() {

        addSubject.setOnClickListener(this);
        cancelSubject.setOnClickListener(this);
        deleteSubject.setOnClickListener(this);
        cancelDelete.setOnClickListener(this);
    }

    private void setFloatingActionButton(){
        FloatingActionButton fab = findViewById(R.id.open_subject_dialog);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!subjectName.getText().equals("")){
                    subjectName.getText().clear();
                }
                openSubjectDialog();
            }
        });
    }

    private void openSubjectDialog(){

        subjectDialog.show();
    }
    private void openDeleteDialog(){

        deleteDialog.show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(AllSubjectsActivity.this,HomeActivity.class));
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.add_subject:
                if(!subjectName.getText().equals("")) {
                    addSubject(subjectName.getText().toString());
                    subjectDialog.cancel();
                }
                break;

            case R.id.cancel_subject:
                subjectDialog.cancel();
                break;

            case R.id.delete_subject:
                deleteSubject(subjectToDelete);
                subjectToDelete=null;
                deleteDialog.cancel();
                break;

            case R.id.cancel_delete:
                deleteDialog.cancel();
                break;

        }
    }

    private void deleteSubject(String name){

        DBManagerSingletone.getInstance(this).deleteSubject(name);
        setRecycleViewSubjects();
    }
    private void addSubject(String name){

        Subject subject=new Subject();
        subject.setName(name);
        DBManagerSingletone.getInstance(this).createSubject(subject);
        setRecycleViewSubjects();
    }
    private void getAllSubject(){

        subjects=DBManagerSingletone.getInstance(this).getAllSubjects();
    }


    private void setRecycleViewSubjects() {

        getAllSubject();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setHasFixedSize(true);
        adapter = new SubjectsAdapter(this, subjects);
        recyclerView.setAdapter(adapter);

    }
}
