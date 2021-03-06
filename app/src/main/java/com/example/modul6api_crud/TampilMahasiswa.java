package com.example.modul6api_crud;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TampilMahasiswa extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextId;
    private EditText editTextName;
    private EditText editTextNim;
    private EditText editTextJurusan;

    private Button buttonUpdate;
    private Button buttonDelete;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_mahasiswa);

        Intent intent = getIntent();

        id = intent.getStringExtra(konfigurasi.MHS_ID);

        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextNim = (EditText) findViewById(R.id.editTextNim);
        editTextJurusan = (EditText) findViewById(R.id.editTextJurusan);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        editTextId.setText(id);

        getMahasiswa();
    }

    private void getMahasiswa(){
        class GetMahasiswa extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilMahasiswa.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showMahasiswa(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_MHS,id);
                return s;
            }
        }
        GetMahasiswa ge = new GetMahasiswa();
        ge.execute();
    }

    private void showMahasiswa(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String name = c.getString(konfigurasi.TAG_NAMA);
            String nim = c.getString(konfigurasi.TAG_NIM);
            String jur = c.getString(konfigurasi.TAG_JURUSAN);

            editTextName.setText(name);
            editTextNim.setText(nim);
            editTextJurusan.setText(jur);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateMahasiswa(){
        final String name = editTextName.getText().toString().trim();
        final String nim = editTextNim.getText().toString().trim();
        final String jurusan = editTextJurusan.getText().toString().trim();

        class UpdateMahasiswa extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilMahasiswa.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilMahasiswa.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(konfigurasi.KEY_MHS_ID,id);
                hashMap.put(konfigurasi.KEY_MHS_NAMA,name);
                hashMap.put(konfigurasi.KEY_MHS_NIM,nim);
                hashMap.put(konfigurasi.KEY_MHS_JURUSAN,jurusan);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(konfigurasi.URL_UPDATE_MHS,hashMap);

                return s;
            }
        }

        UpdateMahasiswa ue = new UpdateMahasiswa();
        ue.execute();
    }

    private void deleteMahasiswa(){
        class DeleteMahasiswa extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilMahasiswa.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilMahasiswa.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_DELETE_MHS, id);
                return s;
            }
        }

        DeleteMahasiswa de = new DeleteMahasiswa();
        de.execute();
    }

    private void confirmDeleteMahasiswa(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Mahasiswa ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteMahasiswa();
                        startActivity(new Intent(TampilMahasiswa.this, TampilSemuaMhs.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateMahasiswa();
        }

        if(v == buttonDelete){
            confirmDeleteMahasiswa();
        }
    }
}
