package nsi.schoolplanner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nsi.schoolplanner.Interfaces.ItemClickListener;
import nsi.schoolplanner.Model.Exam;
import nsi.schoolplanner.R;

public class ExamsAdapter extends RecyclerView.Adapter<ExamsAdapter.ViewHolder>{

    private Context context;
    private List<Exam> exams;
    private ItemClickListener itemClickListener;
    private ArrayList<Integer> colors;

    public ExamsAdapter(Context context, List<Exam> exams, ItemClickListener itemClickListener){
        this.context = context;
        this.exams = exams;
        this.itemClickListener = itemClickListener;

        colors = new ArrayList<>();
        colors.add(context.getResources().getColor(R.color.blue));
        colors.add(context.getResources().getColor(R.color.yellow));
        colors.add(context.getResources().getColor(R.color.green));
        colors.add(context.getResources().getColor(R.color.orange));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exam_adapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setClickListener(exams.get(position).getId());
        holder.txtTitle.setText(exams.get(position).getTitle());
        holder.txtSubject.setText(exams.get(position).getSubject().getName());

        Date date = new Date();
        long diff = exams.get(position).getDate().getTime() - date.getTime();
        long days = diff / (24 * 60 * 60 * 1000);
        long hours = diff / (60 * 60 * 1000) % 24;
        String tmp = String.format("%02d", days) + " " + context.getResources().getString(R.string.daysAnd) +" " + String.format("%02d", hours) +" " +  context.getResources().getString(R.string.hours);
        holder.txtCountdown.setText(tmp);

       holder.backView.setBackgroundColor(colors.get(position%4));
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
         TextView txtTitle;
         TextView txtSubject;
         TextView txtCountdown;
         View view;
         View backView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            txtTitle = view.findViewById(R.id.txtTitle);
            txtSubject = view.findViewById(R.id.txtSubject);
            txtCountdown = view.findViewById(R.id.txtCountDown);
            backView = view.findViewById(R.id.view);
        }

        void setClickListener(final Long id){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(id);
                }
            });
        }
    }
}
