package com.example.mymusic;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView =findViewById(R.id.listView);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ArrayList<File> mySong=fetchsong(Environment.getExternalStorageDirectory());
                        String[] items= new String[mySong.size()];
                        for(int i=0;i< mySong.size();i++){
                            items[i] =mySong.get(i).getName().replace(".mp3","");
                        }
                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(arrayAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                                String currentSong =listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList",mySong);
                                intent.putExtra("position",position);
                                intent.putExtra("currentSong",currentSong);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
    public ArrayList<File> fetchsong(File file){
        ArrayList<File> arrayList=new ArrayList<File>();
        File[] songs=file.listFiles();
        if(songs!=null){
            for(File myFile:songs){
                if(!myFile.isHidden()&&myFile.isDirectory()){
                    arrayList.addAll(fetchsong(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp3")&& !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                    }
                }
            }
        }return arrayList;
    }
}