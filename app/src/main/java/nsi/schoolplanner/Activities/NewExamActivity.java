package nsi.schoolplanner.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nsi.schoolplanner.Adapters.ChooseSubjectAdapter;
import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.Exam;
import nsi.schoolplanner.Model.Subject;
import nsi.schoolplanner.R;

public class NewExamActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txtTitle;
    private TextView txtSubject;
    private TextView txtDate;
    private TextView txtTime;
    private EditText txtNote;
    private Button btnCancel;
    private Button btnOk;

    private boolean modeAdd; //true-add, false-edit
    private Exam exam;

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    public static String subject = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exam);

        getSupportActionBar().setTitle(getResources().getString(R.string.newExam));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle modeBundle = intent.getExtras();
        modeAdd = modeBundle.getBoolean("mode");
        if(!modeAdd){
            long id = modeBundle.getLong("id");
            exam = DBManagerSingletone.getInstance(this).getExam(id);
            getSupportActionBar().setTitle(getResources().getString(R.string.editExam));
        }
        else
            exam = new Exam();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("hh:mm");

        findViewsById();
        setOnClickListeners();

        if(!modeAdd)
            setViews();
    }

    private void findViewsById(){
        txtTitle = findViewById(R.id.txtTitle);
        txtSubject = findViewById(R.id.txtSubject);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtNote = findViewById(R.id.txtNote);
        btnCancel = findViewById(R.id.btnCancel);
        btnOk = findViewById(R.id.btnOK);
        btnOk.setBackgroundColor(getResources().getColor(R.color.green));
    }

    private void setOnClickListeners(){
        txtSubject.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    private void setViews(){
        txtTitle.setText(exam.getTitle());
        txtSubject.setText(exam.getSubject().getName());
        txtDate.setText(dateFormat.format(exam.getDate()));
        txtTime.setText(timeFormat.format(exam.getDate()));
        txtNote.setText(exam.getNote());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.txtSubject:{
                startSubjectPicker();
                break;
            }

            case R.id.txtDate:{
                startDatePicker();
                break;
            }

            case R.id.txtTime:{
                startTimePicker();
                break;
            }

            case R.id.btnCancel:{
                setResult(Activity.RESULT_CANCELED);
                this.finish();
                break;
            }

            case R.id.btnOK:{
                saveDataAndFinish();
                break;
            }

        }
    }

    private void startSubjectPicker(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_subject_dialog);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycle_view_choose_subjects);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setHasFixedSize(true);
        ChooseSubjectAdapter adapter = new ChooseSubjectAdapter(this, DBManagerSingletone.getInstance(this).getAllSubjects(), 1);
        recyclerView.setAdapter(adapter);
        Button btnOk = dialog.findViewById(R.id.choose_subject);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subject != "") {
                    Subject s = DBManagerSingletone.getInstance(NewExamActivity.this).getSubjectByName(subject);
                    txtSubject.setText(subject);
                    exam.setSubject(s);
                    exam.setSubjectId(s.getId());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void startDatePicker(){
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); //new date = current date

        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                if(!txtTime.getText().toString().isEmpty()){
                    Calendar calendarTmp = Calendar.getInstance();
                    calendarTmp.setTime(exam.getDate());
                    calendar.set(Calendar.HOUR, calendarTmp.get(Calendar.HOUR));
                    calendar.set(Calendar.MINUTE, calendarTmp.get(Calendar.MINUTE));
                }

                exam.setDate(calendar.getTime());
                txtDate.setText(dateFormat.format(exam.getDate()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.show();
    }

    private void startTimePicker(){
        TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());

                if(!txtDate.getText().toString().isEmpty()){
                    Calendar calendarTmp = Calendar.getInstance();
                    calendarTmp.setTime(exam.getDate());
                    calendar.set(Calendar.YEAR, calendarTmp.get(Calendar.YEAR));
                    calendar.set(Calendar.MONTH, calendarTmp.get(Calendar.MONTH));
                    calendar.set(Calendar.DAY_OF_MONTH, calendarTmp.get(Calendar.DAY_OF_MONTH));
                }

                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);

                exam.setDate(calendar.getTime());
                txtTime.setText(timeFormat.format(exam.getDate()));
            }
        }, 8, 0, true);

        timePicker.show();
    }

    private void saveDataAndFinish(){
        if(!checkInput()){
            Toast.makeText(this, getResources().getString(R.string.provideData), Toast.LENGTH_SHORT).show();
            return;
        }

        exam.setTitle(txtTitle.getText().toString());
        if(!txtNote.getText().toString().isEmpty())
            exam.setNote(txtNote.getText().toString());

        Long id;
        if(modeAdd)
            id = DBManagerSingletone.getInstance(this).createExam(exam);
        else {
            DBManagerSingletone.getInstance(this).updateExam(exam);
            id = exam.getId();
        }

        Toast.makeText(this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ExamActivity.class);
        Bundle idBundle = new Bundle();
        idBundle.putLong("id", id);
        intent.putExtras(idBundle);
        startActivity(intent);
        this.finish();
    }

    private boolean checkInput(){
        if(txtTitle.getText().toString().isEmpty())
            return false;

        if(txtSubject.getText().toString().isEmpty())
            return false;

        if(txtDate.getText().toString().isEmpty())
            return false;

        if(txtTime.getText().toString().isEmpty())
            return false;

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
