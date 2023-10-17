package com.example.androidcomicreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.androidcomicreader.Common.Common;
import com.example.androidcomicreader.Model.Chapter;
import com.example.androidcomicreader.Model.Comic;
import com.example.androidcomicreader.Retrofit.IComicAPI;
import com.example.androidcomicreader.Retrofit.INodeJS;
import com.example.androidcomicreader.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Admin_Chapter_Add extends AppCompatActivity {
    IComicAPI myAPI;
    INodeJS nodeAPI;
    //Spinner spinnerUpdate;

//    Retrofit retrofit = RetrofitClient.getInstance();
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edit_Name;
    Button btn_Add;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chapter_add);

        Retrofit retrofit = RetrofitClient.getInstance();
        edit_Name = findViewById(R.id.edt_name);

        myAPI = retrofit.create(IComicAPI.class);
        nodeAPI = retrofit.create(INodeJS.class);

//        deleteManga(1);
        btn_Add = (Button) findViewById(R.id.button_Add);
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comic selectedItem = (Comic) spinnerUpdate.getSelectedItem();
                int mangaID = Common.selected_comic.getID();
                addChapter(edit_Name.getText().toString(), mangaID);
            }
        });
    }
    private void addChapter(String name, int mangaid) {
        if(name.equals("") || mangaid == 0){
            Toast.makeText(Admin_Chapter_Add.this,"Please input all form",Toast.LENGTH_SHORT).show();
        }else{
            compositeDisposable
                    .add(nodeAPI.addChapter(name,mangaid)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    Toast.makeText(Admin_Chapter_Add.this,""+s,Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Admin_Chapter_Add.this, ChapterActivity.class));
                                }

                            }));
        }
        // View register_layout = LayoutInflater.from(this).inflate(R.layout.activity_admin_manga_add, null);

    }
}