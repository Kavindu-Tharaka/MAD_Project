package com.ktm.thumb_first;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class DiaryListsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_lists);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryListsActivity.this,DiaryAddActivity.class);
                finish();
                startActivity(intent);
            }
        });

        editText  = findViewById(R.id.searchTextDiary);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = editText.getText().toString();

                ListView listView = findViewById(R.id.diaryListView); //access the listView which is ID = "shopping_list"

                DatabaseHelper mydb = new DatabaseHelper(DiaryListsActivity.this);
                SQLiteDatabase db = mydb.getReadableDatabase();

                String query = "select * from "+ThumbMaster.Diary.TABLE_NAME+" where "+ThumbMaster.Diary.COLUMN_NAME_DATE+" like '%"+searchText+"%' ";
                Cursor cursor = db.rawQuery(query,null);   //Cursor -> resultSet , rawQuery -> executeQuery()
                //cursor.moveToFirst();

                int layout = R.layout.one_item_diary_list;  //choose layout created manually
                String[] columns = {"_id","date","time","content"};  //get values from table columns
                int[] views = {R.id.diary_item_id,R.id.diary_item_date,R.id.diary_item_time,R.id.diary_item_content}; //IDs of fields that table data will be mapped

                SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(DiaryListsActivity.this,layout,cursor,columns,views);
                listView.setAdapter(simpleCursorAdapter);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void showDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = " "+year+" - "+(month+1)+" - "+dayOfMonth+" ";
        editText.setText(date);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListView listView = findViewById(R.id.diaryListView); //access the listView which is ID = "shopping_list"

        DatabaseHelper mydb = new DatabaseHelper(this);
        SQLiteDatabase db = mydb.getReadableDatabase();

        String query = "select * from "+ThumbMaster.Diary.TABLE_NAME+"";
        Cursor cursor = db.rawQuery(query,null);   //Cursor -> resultSet , rawQuery -> executeQuery()
        //cursor.moveToFirst();

        int layout = R.layout.one_item_diary_list;  //choose layout created manually
        String[] columns = {"_id","date","time","content"};  //get values from table columns
        int[] views = {R.id.diary_item_id,R.id.diary_item_date,R.id.diary_item_time,R.id.diary_item_content}; //IDs of fields that table data will be mapped

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,layout,cursor,columns,views);
        listView.setAdapter(simpleCursorAdapter);
    }

    public void deleteDiaryItem(View v){
        final View view = v;

        AlertDialog.Builder builder = new AlertDialog.Builder(DiaryListsActivity.this);

        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LinearLayout parent = (LinearLayout) view.getParent();
                        TextView textViewv = parent.findViewById(R.id.diary_item_id);
                        String idTemp = textViewv.getText().toString();
                        int id = Integer.parseInt(idTemp);

                        DatabaseHelper myDB = new DatabaseHelper(DiaryListsActivity.this);
                        SQLiteDatabase db = myDB.getWritableDatabase();
                        String sqlQuery = "delete from Diary where _id = " + id+ "";
                        db.execSQL(sqlQuery);

                        Toasty.success(DiaryListsActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();

                        //to refresh
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("No",null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    public void editDiaryItem(View view){
        LinearLayout linearLayout = (LinearLayout) view.getParent();
        TextView textView = linearLayout.findViewById(R.id.diary_item_id);
        TextView textView1 = linearLayout.findViewById(R.id.diary_item_content);

        LinearLayout linearLayout1 = (LinearLayout) linearLayout.getParent();
        TextView textView2 = linearLayout1.findViewById(R.id.diary_item_date);
        TextView textView3 = linearLayout1.findViewById(R.id.diary_item_time);

        String id = textView.getText().toString();
        String content = textView1.getText().toString();
        String date = textView2.getText().toString();
        String time = textView3.getText().toString();

        Intent intent = new Intent(this,DiaryEditActivity.class);

        intent.putExtra("ID",id);
        intent.putExtra("DATE",date);
        intent.putExtra("TIME",time);
        intent.putExtra("CONTENT",content);

        finish();

        startActivity(intent);
    }

}
