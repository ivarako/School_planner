package nsi.schoolplanner.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.Exam;
import nsi.schoolplanner.R;

public class ExamActivity extends AppCompatActivity implements View.OnClickListener{

    private Exam exam;
    private TextView txtSubject;
    private TextView txtDate;
    private TextView txtTime;
    private TextView txtNote;
    private Button btnEdit;
    private Button btnDelete;

    private SimpleDateFormat dateFormat;
    private java.text.SimpleDateFormat timeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        Intent intent = getIntent();
        Bundle idBundle = intent.getExtras();
        Long id = idBundle.getLong("id");
        exam = DBManagerSingletone.getInstance(this).getExam(id);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(exam.getTitle());

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("hh:mm");

        findItemsById();
        setOnClickListeners();
        setData();
    }

    private void findItemsById(){
        txtSubject = findViewById(R.id.txtSubject);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtNote = findViewById(R.id.txtNote);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setBackgroundColor(getResources().getColor(R.color.red));
    }

    private void setOnClickListeners(){
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    private void setData(){
        txtSubject.setText(exam.getSubject().getName());
        txtDate.setText(dateFormat.format(exam.getDate()));
        txtTime.setText(timeFormat.format(exam.getDate()));
        txtNote.setText(exam.getNote());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEdit:{
                Intent intent = new Intent(this, NewExamActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("mode", false);
                bundle.putLong("id", exam.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                this.finish();
                break;
            }

            case R.id.btnDelete:{
                startDeleteDialog();
                break;
            }
        }
    }

    private void startDeleteDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_dialog);
        TextView txtText = dialog.findViewById(R.id.txtText);
        TextView txtCancel = dialog.findViewById(R.id.txtCancel);
        TextView txtOk = dialog.findViewById(R.id.txtOk);

        txtText.setText(getResources().getString(R.string.areYouSure));

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManagerSingletone.getInstance(ExamActivity.this).deleteExam(exam);
                Toast.makeText(ExamActivity.this, getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                ExamActivity.this.finish();
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
