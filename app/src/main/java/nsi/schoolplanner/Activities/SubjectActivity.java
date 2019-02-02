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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nsi.schoolplanner.Adapters.ExamGradeAdapter;
import nsi.schoolplanner.Adapters.SubjectsAdapter;
import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.Exam;
import nsi.schoolplanner.Model.Subject;
import nsi.schoolplanner.R;

public class SubjectActivity extends AppCompatActivity implements View.OnClickListener{

    public static Subject subject;
    private RecyclerView recyclerView;
    private ExamGradeAdapter adapter;
    private List <Exam> exams;
    private DBManagerSingletone manager;
    public static TextView averageRating;
    private Button editSubject;
    private Button deleteSubject;
    private Button deleteSubjectBtn;
    private Button cancelSubjectBtn;
    private Button addSubjectName;
    private Button cancelSubjectName;
    Dialog deleteSubjectDialog;
    Dialog subjectDialog;
    String name;
    private TextView nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        exams=new ArrayList<>();
        manager=new DBManagerSingletone();
        deleteSubjectDialog=new Dialog(this);
        subjectDialog=new Dialog(this);
        deleteSubjectDialog.setContentView(R.layout.delete_subject_dialog);
        subjectDialog.setContentView(R.layout.subject_dialog);
        findViewById();
        setOnClickListener();

        if(subject!=null) {
            getSupportActionBar().setTitle(subject.getName().toString());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setRecycleViewExams();
    }

    @Override
    public boolean onSupportNavigateUp() {

        startActivity(new Intent(SubjectActivity.this,AllSubjectsActivity.class));
        return true;
    }

    private void findViewById(){

        averageRating=findViewById(R.id.average_rating_value);
        editSubject=findViewById(R.id.edit_subject);
        deleteSubject=findViewById(R.id.delete_subject_activity);
        deleteSubjectBtn=deleteSubjectDialog.findViewById(R.id.delete_subject);
        cancelSubjectBtn=deleteSubjectDialog.findViewById(R.id.cancel_delete);
        addSubjectName=subjectDialog.findViewById(R.id.add_subject);
        cancelSubjectName=subjectDialog.findViewById(R.id.cancel_subject);
        nameText=subjectDialog.findViewById(R.id.subject_name_text);
    }

    private void setOnClickListener(){

        editSubject.setOnClickListener(this);
        deleteSubject.setOnClickListener(this);
        deleteSubjectBtn.setOnClickListener(this);
        cancelSubjectBtn.setOnClickListener(this);
        addSubjectName.setOnClickListener(this);
        cancelSubjectName.setOnClickListener(this);
    }

    private void setRecycleViewExams() {

        getAllSubjectsExams();
        if(exams!=null) {
            recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            recyclerView.setHasFixedSize(true);
            adapter = new ExamGradeAdapter(this, exams);
            recyclerView.setAdapter(adapter);
        }

    }

    private void getAllSubjectsExams(){

        exams=manager.getInstance(this).getAllSubjectExams(subject);
    }

    private void editSubject(){
        manager.getInstance(this).editSubject(subject,name);
    }

    private void deleteSubject(){

        manager.getInstance(this).deleteSubject(subject);
    }

    private void openDeleteSubjectDialog(){
        deleteSubjectDialog.show();
    }
    private void openSubjectDialog(){
        subjectDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_subject:
                nameText.setText(subject.getName());
                openSubjectDialog();
                break;
            case R.id.delete_subject_activity:
                openDeleteSubjectDialog();
                break;
            case R.id.delete_subject:
                deleteSubject();
                deleteSubjectDialog.cancel();
                finish();
                startActivity(new Intent(SubjectActivity.this,AllSubjectsActivity.class));
                break;
            case R.id.cancel_delete:
                deleteSubjectDialog.cancel();
                break;
            case R.id.add_subject:
                name=nameText.getText().toString();
                if(name!=null) {
                    editSubject();
                    subjectDialog.cancel();
                    finish();
                    subject=manager.getInstance(this).getSubjectByName(name);
                    startActivity(new Intent(SubjectActivity.this, SubjectActivity.class));
                }
                subjectDialog.cancel();
                break;
            case R.id.cancel_subject:
                subjectDialog.cancel();
                break;
        }
    }
}
