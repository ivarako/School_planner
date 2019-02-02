package nsi.schoolplanner.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import nsi.schoolplanner.Adapters.ExamsAdapter;
import nsi.schoolplanner.Interfaces.ItemClickListener;
import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.Person;
import nsi.schoolplanner.R;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ItemClickListener{

    private ActionBarDrawerToggle toggle;
    private Person person;
    private ImageView img;
    private TextView txtName;

    public static int REQUEST_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        person = DBManagerSingletone.getInstance(this).getPerson();
        setNavigationView();
        setFloatingActionButton();
        setRecyclerView();
    }

    @Override
    protected void onResume() {
        setRecyclerView();
        super.onResume();
    }

    private void setNavigationView(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        toggle= new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationHeader(navigationView);
    }

    private void setNavigationHeader(NavigationView navigationView){
        View headerView = navigationView.getHeaderView(0);
        txtName = headerView.findViewById(R.id.txtName);
        img = headerView.findViewById(R.id.img);

        if(person != null){
            if(person.getName()!= null && !person.getName().isEmpty())
                txtName.setText(person.getName());

            if(person.getPathToImg() != null) {
                img.setImageURI(Uri.parse(person.getPathToImg()));
            }
        }

        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNameDialog();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) { }
                    else {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                    }
                } else {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 0);
                }
            }
        });
    }

    private void setFloatingActionButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NewExamActivity.class);
                Bundle modeBundle = new Bundle();
                modeBundle.putBoolean("mode", true);
                intent.putExtras(modeBundle);
                startActivity(intent);
            }
        });
    }

    private void setRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ExamsAdapter examsAdapter = new ExamsAdapter(this, DBManagerSingletone.getInstance(this).getFutureExams(), this);
        recyclerView.setAdapter(examsAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.timetable:{
                Intent intent = new Intent(this, TimeTableActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.calendar:{
                Intent intent = new Intent(this, CalendarActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.grades:{
                Intent intent = new Intent(this, GradesActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.subjects:{
                Intent intent = new Intent(this, AllSubjectsActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.newSemestar:{
                startDeleteDialog();
                break;
            }

            case R.id.weekPlans:{
                Intent intent = new Intent(this, AllWeekPlansActivity.class);
                startActivity(intent);
                break;
            }
        }

        return true;
    }

    private void startDeleteDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_dialog);
        TextView txt = dialog.findViewById(R.id.txtText);
        TextView txtOk = dialog.findViewById(R.id.txtOk);
        TextView txtCancel = dialog.findViewById(R.id.txtCancel);

        txt.setText(getResources().getString(R.string.areYouSureAll));

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManagerSingletone.getInstance(HomeActivity.this).deleteGradesAndTimeTable();
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

    private void startNameDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.name_dialog);
        final EditText editTxtName = dialog.findViewById(R.id.txtName);
        TextView txtOk = dialog.findViewById(R.id.txtOk);
        TextView txtCancel = dialog.findViewById(R.id.txtCancel);

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTxtName.getText().toString().isEmpty()){
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.provideData), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(person == null)
                    person = new Person();

                person.setName(editTxtName.getText().toString());
                txtName.setText(editTxtName.getText().toString());

                if(person.getId() == null)
                    DBManagerSingletone.getInstance(HomeActivity.this).createPerson(person);
                else
                    DBManagerSingletone.getInstance(HomeActivity.this).updatePerson(person);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 0){
                Uri imageUri = intent.getData();
                img.setImageURI(imageUri);
                if(person == null) {
                    person = new Person();
                    person.setName("");
                }
                person.setPathToImg(imageUri.toString());

                if(person.getId()!= null)
                     DBManagerSingletone.getInstance(HomeActivity.this).updatePerson(person);
                else
                    DBManagerSingletone.getInstance(HomeActivity.this).createPerson(person);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Object object) {
        Intent intent = new Intent(this, ExamActivity.class);
        Bundle idBundle = new Bundle();
        idBundle.putLong("id", (Long)object);
        intent.putExtras(idBundle);
        startActivity(intent);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 0);
        }
    }
}
