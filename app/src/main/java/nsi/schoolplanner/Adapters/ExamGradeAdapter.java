package nsi.schoolplanner.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import nsi.schoolplanner.Activities.SubjectActivity;
import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.Exam;
import nsi.schoolplanner.Model.Grade;
import nsi.schoolplanner.R;

public class ExamGradeAdapter extends RecyclerView.Adapter<ExamGradeAdapter.ViewHolder> {

    private List<Exam>exams;
    private Context context;
    private Dialog addGradeDialog;
    private Dialog editGradeDialog;
    private Button addGrade;
    private Button cancelGrade;
    private Button editGrade;
    private Button cancelEdit;
    private EditText gradeValue;
    private DBManagerSingletone manager;
    private double calculateRating=0.0;

    public ExamGradeAdapter(Context context, List<Exam> exams){

        this.context=context;
        this.exams=exams;
        this.addGradeDialog= new Dialog(context);
        this.editGradeDialog=new Dialog(context);
        this.manager=new DBManagerSingletone();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_grade_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.examName.setText(exams.get(position).getTitle().toString());
        double count=0.0;
        double sum=0.0;
        for(int i=0;i<exams.size();i++){
            if(exams.get(i).getGrade()!=null) {
                sum += exams.get(i).getGrade().getGrade();
                count++;
            }
            else{
                holder.grade.setVisibility(View.INVISIBLE);
                holder.editGrade.setVisibility(View.INVISIBLE);
                holder.addGrade.setVisibility(View.VISIBLE);
            }
        }
        if(sum!=0.0 && count!=0.0) {
            calculateRating = sum / count;
        }
        else{
            calculateRating=0.0;
        }
        SubjectActivity.averageRating.setText(String.format("%.2f", calculateRating));
        if(manager.getInstance(context).getGrade(exams.get(position))!=null){

            Grade getGrade=manager.getInstance(context).getGrade(exams.get(position));
            holder.grade.setText(String.valueOf(getGrade.getGrade()));
            holder.addGrade.setVisibility(View.INVISIBLE);
            holder.editGrade.setVisibility(View.VISIBLE);
            holder.grade.setVisibility(View.VISIBLE);
            holder.editGrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   editGradeDialog(exams.get(position));
                }
            });
        }
        holder.addGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddGradeDialog(exams.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    public void editGradeDialog(final Exam exam){

        editGradeDialog.setContentView(R.layout.edit_grade_dialog);
        editGradeDialog.show();
        editGrade=editGradeDialog.findViewById(R.id.edit_grade);
        cancelEdit=editGradeDialog.findViewById(R.id.cancel_edit);
        gradeValue=editGradeDialog.findViewById(R.id.grade_value);
        if(exam.getGrade().getGrade()>0 && exam.getGrade().getGrade()<6) {
            gradeValue.setText(String.valueOf(exam.getGrade().getGrade()));
        }
        editGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grade=0;
                if(!gradeValue.getText().toString().isEmpty()) {
                    grade = Integer.parseInt(gradeValue.getText().toString());
                }
                if(grade>0 && grade<6) {
                    editGrade(exam, grade);
                    notifyDataSetChanged();
                    editGradeDialog.cancel();
                }
                else{
                    deleteGrade(exam);
                    notifyDataSetChanged();
                    editGradeDialog.cancel();
                }
            }
        });
        cancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGradeDialog.cancel();
            }
        });

    }

    public void openAddGradeDialog(final Exam exam){

        addGradeDialog.setContentView(R.layout.add_grade_dialog);
        addGradeDialog.show();
        addGrade=addGradeDialog.findViewById(R.id.add_grade);
        cancelGrade=addGradeDialog.findViewById(R.id.cancel_grade);
        gradeValue=addGradeDialog.findViewById(R.id.grade_value);
        addGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grade=0;
                if(!gradeValue.getText().toString().isEmpty()) {
                    grade = Integer.parseInt(gradeValue.getText().toString());
                }
                if(grade>0 && grade<6) {
                    addGrade(exam, grade);
                    notifyDataSetChanged();
                    addGradeDialog.cancel();
                }
            }
        });

        cancelGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGradeDialog.cancel();
            }
        });
    }

    public void addGrade(Exam exam, int grade){

        manager.getInstance(context).addGrade(exam,grade);
    }
    public void editGrade(Exam exam, int grade){

        manager.getInstance(context).editGrade(exam, grade);

}
    public void deleteGrade(Exam exam){

        manager.getInstance(context).deleteGrade(exam);

}
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView examName;
        TextView grade;
        Button addGrade;
        Button editGrade;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            examName=itemView.findViewById(R.id.exam_name);
            addGrade=itemView.findViewById(R.id.add_grade);
            grade=itemView.findViewById(R.id.grade);
            editGrade=itemView.findViewById(R.id.edit_grade);
        }
    }
}
