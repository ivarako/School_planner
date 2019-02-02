package nsi.schoolplanner.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nsi.schoolplanner.Adapters.ChooseSubjectAdapter;
import nsi.schoolplanner.Adapters.ToDoListAdapter;
import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.Subject;
import nsi.schoolplanner.Model.ToDoList;
import nsi.schoolplanner.R;

public class WeekPlanActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txtWeek;
    private TextView txtStartDate;
    private TextView txtEndDate;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<ToDoList> toDoItems;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Boolean modeEdit;
    private Date date;

    public static String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_plan);

        toDoItems = new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        modeEdit = bundle.getBoolean("modeEdit");

        if(modeEdit) {
            date = (Date) bundle.getSerializable("date");
            toDoItems = DBManagerSingletone.getInstance(this).getToDoListForDate(date);
        }

        findViewsByID();
        setOnClickListeners();
        initRecyclerView();
    }

    private void findViewsByID(){
        txtWeek = findViewById(R.id.txtChooseWeek);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        if(modeEdit){
            txtStartDate.setText(dateFormat.format(date));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 6);
            txtEndDate.setText(dateFormat.format(calendar.getTime()));
        }
    }

    private void setOnClickListeners(){
        txtWeek.setOnClickListener(this);
        txtStartDate.setOnClickListener(this);
        txtEndDate.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ToDoListAdapter(this, toDoItems));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.txtChooseWeek:{

            }

            case R.id.txtStartDate:{

            }

            case R.id.txtEndDate:{
                startDatePicker();
                break;
            }

            case R.id.fab:{
                startItemDialog();
                break;
            }
        }
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

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                calendar.add(Calendar.DAY_OF_YEAR, -dayOfWeek);
                calendar.add(Calendar.DAY_OF_YEAR, 2);
                txtStartDate.setText(dateFormat.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_YEAR, 6);
                txtEndDate.setText(dateFormat.format(calendar.getTime()));

                try {
                    updateToDoItems();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.show();
    }

    private void updateToDoItems() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(txtStartDate.getText().toString()));
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.MILLISECOND, 1);

        for(ToDoList item : toDoItems){
            item.setDate(calendar.getTime());
            DBManagerSingletone.getInstance(WeekPlanActivity.this).updateToDoList(item);
        }
    }

    private void startItemDialog(){
        if(txtStartDate.getText().toString().isEmpty()){
            Toast.makeText(WeekPlanActivity.this, getResources().getString(R.string.provideDateFirst), Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.week_item_dialog);
        final TextView txtSubject = dialog.findViewById(R.id.txtSubject);
        final EditText txtItem = dialog.findViewById(R.id.txtItem);
        TextView txtCancel = dialog.findViewById(R.id.txtCancel);
        TextView txtOK = dialog.findViewById(R.id.txtOK);

        txtSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSubjectPicker(txtSubject);
            }
        });

        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtSubject.getText().toString().isEmpty() || txtItem.getText().toString().isEmpty()){
                    Toast.makeText(WeekPlanActivity.this, getResources().getString(R.string.provideData), Toast.LENGTH_SHORT).show();
                    return;
                }

                ToDoList toDoList = new ToDoList();

                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateFormat.parse(txtStartDate.getText().toString()));
                    calendar.set(Calendar.HOUR_OF_DAY, 1);
                    calendar.set(Calendar.MINUTE, 1);
                    calendar.set(Calendar.MILLISECOND, 1);
                    toDoList.setDate(calendar.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                toDoList.setSubjectId(DBManagerSingletone.getInstance(WeekPlanActivity.this).getSubjectByName(subject).getId());
                toDoList.setItem(txtItem.getText().toString());
                toDoList.setChecked(false);
                toDoList.setId(DBManagerSingletone.getInstance(WeekPlanActivity.this).createToDoList(toDoList));
                toDoItems.add(toDoList);
                initRecyclerView();
                dialog.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void startSubjectPicker(final TextView textView){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_subject_dialog);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycle_view_choose_subjects);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setHasFixedSize(true);
        ChooseSubjectAdapter adapter = new ChooseSubjectAdapter(this, DBManagerSingletone.getInstance(this).getAllSubjects(), 2);
        recyclerView.setAdapter(adapter);
        Button btnOk = dialog.findViewById(R.id.choose_subject);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subject != "") {
                    textView.setText(subject);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
}
