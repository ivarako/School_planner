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

import nsi.schoolplanner.Activities.GradesActivity;
import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.Subject;
import nsi.schoolplanner.R;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.ViewHolder> {

    private List<Subject>subjects;
    private Context context;
    private List<String> grades;
    private Dialog addGradeDialog;
    private DBManagerSingletone manager;
    private double calculateRating=0.0;
    private Button addGrade;
    private Button cancelGrade;
    private EditText gradeValue;

    public GradesAdapter(Context context, List<Subject> subjects, List<String> grades){

        this.context=context;
        this.subjects=subjects;
        this.grades=grades;
        this.addGradeDialog= new Dialog(context);
        this.manager=new DBManagerSingletone();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.subjectName.setText(subjects.get(position).getName().toString());
        holder.grades.setText(grades.get(position).toString());
        holder.addGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddGradeDialog(subjects.get(position));
            }
        });
        if(subjects.get(position).getFinalGrade()!=0){
            holder.finalGrade.setText(String.valueOf(subjects.get(position).getFinalGrade()));
            holder.addGrade.setVisibility(View.INVISIBLE);
        }
        double sum=0.0;
        double count=0.0;
        for(int i=0;i<subjects.size();i++){
            if(subjects.get(i).getFinalGrade()!=0){
                sum+=subjects.get(i).getFinalGrade();
                count++;
            }
        }
        if(sum!=0.0 && count!=0.0) {
            calculateRating = sum / count;
        }
        else{
            calculateRating=0.0;
        }

        GradesActivity.averageRating.setText(String.format("%.2f", calculateRating));
    }

    public void openAddGradeDialog(final Subject subject){

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
                    addFinalGrade(subject, grade);
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
    public void addFinalGrade(Subject subject, int grade){

        manager.getInstance(context).addFinalGrade(subject,grade);
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

      TextView subjectName;
      TextView grades;
      TextView finalGrade;
      Button addGrade;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectName=itemView.findViewById(R.id.subject_name);
            addGrade=itemView.findViewById(R.id.add_grade);
            grades=itemView.findViewById(R.id.grades);
            finalGrade=itemView.findViewById(R.id.final_grade);
        }
    }
}
