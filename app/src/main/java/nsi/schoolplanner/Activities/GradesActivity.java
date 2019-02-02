package nsi.schoolplanner.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import nsi.schoolplanner.Adapters.GradesAdapter;
import nsi.schoolplanner.Adapters.SubjectsAdapter;
import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.Exam;
import nsi.schoolplanner.Model.GradeDao;
import nsi.schoolplanner.Model.Subject;
import nsi.schoolplanner.R;

public class GradesActivity extends AppCompatActivity {

    private GradesAdapter adapter;
    private RecyclerView recyclerView;
    private List<Subject> subjects;
    private List<String>grades;
    public static TextView averageRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        subjects=new ArrayList<>();
        grades=new ArrayList<>();
        averageRating=findViewById(R.id.average_rating_value);

        getSupportActionBar().setTitle(getResources().getString(R.string.grades));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setRecycleViewSubjects();
    }
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(GradesActivity.this,HomeActivity.class));
        return true;
    }

    private void getAllSubject(){

        subjects= DBManagerSingletone.getInstance(this).getAllSubjects();
    }

    private void getGrades(){
        String pom="";
        List <Exam>exams=new ArrayList<>();
        for(int i=0;i<subjects.size();i++){
            exams=DBManagerSingletone.getInstance(this).getAllSubjectExams(subjects.get(i));
            if(exams!=null) {
                for (int j = 0; j < exams.size(); j++) {
                    if (exams.get(j).getGrade() != null) {
                        if (pom.equals("")) {

                            pom = String.valueOf(exams.get(j).getGrade().getGrade());
                        } else {
                            pom += "," + String.valueOf(exams.get(j).getGrade().getGrade());
                        }
                    }
                }
            }
            grades.add(pom);
            pom="";
        }
    }

    private void setRecycleViewSubjects() {

        getAllSubject();

        getGrades();

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setHasFixedSize(true);
        adapter = new GradesAdapter(this, subjects,grades);
        recyclerView.setAdapter(adapter);

    }
}
