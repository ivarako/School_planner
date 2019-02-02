package nsi.schoolplanner.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nsi.schoolplanner.Adapters.ChooseSubjectAdapter;
import nsi.schoolplanner.Adapters.SubjectsAdapter;
import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.Subject;
import nsi.schoolplanner.R;

public class TimeTableActivity extends AppCompatActivity implements View.OnClickListener {

    Dialog subjectDialog;
    RecyclerView recyclerView;
    ChooseSubjectAdapter adapter;
    List<Subject> subjects;
    Button cancelBtn;
    Button chooseSubjectBtn;
    public static int rowTable;
    public static int columnTable;
    public static String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        getSupportActionBar().setTitle(getResources().getString(R.string.timetable));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subjectDialog= new Dialog(this);
        subjectDialog.setContentView(R.layout.choose_subject_dialog);
        subjects=new ArrayList<>();
        findViewById();
        setOnClickListener();

        TableLayout table = findViewById(R.id.table);
        int count = table.getChildCount();
        for (int i = 0; i < count; i++) {
            final int rowPos=i;
            View v = table.getChildAt(i);
            if (v instanceof TableRow) {
                TableRow row = (TableRow) v;
                int rowCount = row.getChildCount();
                for (int r = 0; r < rowCount; r++) {
                    final int columnPos=r;
                    View v2 = row.getChildAt(r);
                    if (v2 instanceof TextView) {
                        TextView tv = (TextView) v2;
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                setRecycleViewSubjects();
                                subjectDialog.show();
                                rowTable=rowPos;
                                columnTable =columnPos;

                            }
                        });
                    }
                }
            }
        }

        setTimetable();
    }
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(TimeTableActivity.this, HomeActivity.class));
        return true;
    }

    private void findViewById(){

        cancelBtn=subjectDialog.findViewById(R.id.cancel_subject);
        chooseSubjectBtn=subjectDialog.findViewById(R.id.choose_subject);
    }

    private void setOnClickListener(){

        cancelBtn.setOnClickListener(this);
        chooseSubjectBtn.setOnClickListener(this);
    }

    private  void addSubjectToTimeTable(){

        DBManagerSingletone.getInstance(this).createTimetable(rowTable,columnTable,subject);
        setTimetable();
    }
    private void editTimetable(){

        DBManagerSingletone.getInstance(this).editTimetable(rowTable,columnTable,subject);
        setTimetable();
    }
    private void setTimetable(){
        TableLayout table = findViewById(R.id.table);
        int count = table.getChildCount();
        for (int i = 0; i < count; i++) {
            final int rowPos = i;
            View v = table.getChildAt(i);
            if (v instanceof TableRow) {
                TableRow row = (TableRow) v;
                int rowCount = row.getChildCount();
                for (int r = 0; r < rowCount; r++) {
                    final int columnPos = r;
                    View v2 = row.getChildAt(r);
                    if (v2 instanceof TextView) {
                        if(rowPos>=2){
                            if(columnPos>=1){
                                String subject=DBManagerSingletone.getInstance(this).getTimetable(rowPos,columnPos);
                                if(subject!=null) {
                                    ((TextView) v2).setText(subject);
                                }
                            }
                        }
                        if(columnPos==1){
                            ((TextView) v2).setTextColor(getResources().getColor(android.R.color.holo_red_light));
                        }
                        else if(columnPos==2){
                            ((TextView) v2).setTextColor(getResources().getColor(android.R.color.holo_green_light));
                        }
                        else if(columnPos==3){
                            ((TextView) v2).setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                        }
                        else if(columnPos==4){
                            ((TextView) v2).setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                        }
                        else if(columnPos==5){
                            ((TextView) v2).setTextColor(getResources().getColor(android.R.color.holo_purple));
                        }
                        else{

                        }
                    }
                }
            }
        }
    }
    private void setRecycleViewSubjects() {

        getAllSubject();

        recyclerView=subjectDialog.findViewById(R.id.recycle_view_choose_subjects);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setHasFixedSize(true);
        adapter = new ChooseSubjectAdapter(this, subjects, 0);
        recyclerView.setAdapter(adapter);

    }

    private void getAllSubject(){

        subjects= DBManagerSingletone.getInstance(this).getAllSubjects();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_subject:
                subjectDialog.cancel();
                break;
            case R.id.choose_subject:
                String subject=DBManagerSingletone.getInstance(this).getTimetable(rowTable,columnTable);
                if(subject==null) {
                    addSubjectToTimeTable();
                }
                else if(subject.equals("")){
                    addSubjectToTimeTable();
                }
                else{
                    editTimetable();
                }
                subjectDialog.cancel();
                break;
        }
    }
}
