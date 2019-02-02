package nsi.schoolplanner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.List;

import nsi.schoolplanner.Activities.NewExamActivity;
import nsi.schoolplanner.Activities.TimeTableActivity;
import nsi.schoolplanner.Activities.WeekPlanActivity;
import nsi.schoolplanner.Model.Subject;
import nsi.schoolplanner.R;

public class ChooseSubjectAdapter extends RecyclerView.Adapter<ChooseSubjectAdapter.ViewHolder> {

    private List<Subject>subjects;
    public static Context context;
    public static RadioButton lastCheckedRB = null;
    public int mode; //mode 0- timetable, 1-new exam, 2- week plan

    public ChooseSubjectAdapter(Context context, List<Subject> subjects, int mode){

        this.context=context;
        this.subjects=subjects;
        this.mode = mode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_subject_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.subject.setText(subjects.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{

        private RadioButton subject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subject=itemView.findViewById(R.id.radio_subject);

            subject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lastCheckedRB != null){
                        lastCheckedRB.setChecked(false);
                    }
                    lastCheckedRB = subject;

                    switch (mode){
                        case 0:{
                            TimeTableActivity.subject=lastCheckedRB.getText().toString();
                            break;
                        }

                        case 1:{
                            NewExamActivity.subject = lastCheckedRB.getText().toString();
                            break;
                        }

                        case 2:{
                            WeekPlanActivity.subject = lastCheckedRB.getText().toString();
                            break;
                        }
                    }
                }
            });


        }

    }
}
