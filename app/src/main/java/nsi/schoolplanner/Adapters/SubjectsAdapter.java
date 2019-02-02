package nsi.schoolplanner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import nsi.schoolplanner.Activities.AllSubjectsActivity;
import nsi.schoolplanner.Activities.SubjectActivity;
import nsi.schoolplanner.Model.Subject;
import nsi.schoolplanner.R;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    private List<Subject>subjects;
    public static Context context;

    public SubjectsAdapter(Context context, List<Subject> subjects){

        this.context=context;
        this.subjects=subjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjects_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.subjectName.setText(subjects.get(position).getName());

        holder.subjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubjectActivity.subject=subjects.get(position);
                Intent intent = new Intent(context, SubjectActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView subjectName;
        private ImageButton deleteSubject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectName=itemView.findViewById(R.id.subject_name);
            deleteSubject=itemView.findViewById(R.id.subject_delete);
            deleteSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllSubjectsActivity.subjectToDelete=subjectName.getText().toString();
                    Intent intent = new Intent(context, AllSubjectsActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
