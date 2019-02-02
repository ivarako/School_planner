package nsi.schoolplanner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.ToDoList;
import nsi.schoolplanner.R;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder>{

    private Context context;
    private List<ToDoList> toDoItems;

    public ToDoListAdapter(Context context, List<ToDoList> toDoItems){
        this.context = context;
        this.toDoItems = toDoItems;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.to_do_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       String text = toDoItems.get(position).getSubject().getName() + ": " + toDoItems.get(position).getItem();
       holder.checkBox.setText(text);

       if(toDoItems.get(position).getChecked())
           holder.checkBox.setChecked(true);

       holder.setCheckedListener(toDoItems.get(position));
    }

    @Override
    public int getItemCount() {
        return toDoItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        void setCheckedListener(final ToDoList toDoList){
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    toDoList.setChecked(checkBox.isChecked());
                    DBManagerSingletone.getInstance(context).updateToDoList(toDoList);
                }
            });
        }
    }
}
