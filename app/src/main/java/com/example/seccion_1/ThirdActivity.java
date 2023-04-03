package com.example.seccion_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity {
  // Captura de los elementos
  private EditText editTextPhone;
  private EditText editTextWeb;
  private ImageButton imgBtnPhone;
  private ImageButton imgBtnWeb;
  private ImageButton imgBtnCamera;
  private final int PHONE_CALL_CODE = 100;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_third);

    editTextPhone = (EditText) findViewById(R.id.editTextPhone);
    editTextWeb = (EditText) findViewById(R.id.editTextWeb);
    imgBtnPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
    imgBtnWeb = (ImageButton) findViewById(R.id.imageButtonWeb);
    imgBtnCamera = (ImageButton) findViewById(R.id.imageButtonCamara);

    imgBtnPhone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String phoneNumber = editTextPhone.getText().toString();

        if (phoneNumber != null && !phoneNumber.isEmpty()){
          // Comprobar version actual de Android que estamos corriendo
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Metodo para verificar las versiones nuevas
            requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
          }else{
            OlderVersions(phoneNumber);
          }
        }else{
          Toast.makeText(ThirdActivity.this, "Insert a phone number", Toast.LENGTH_SHORT).show();
        }
      }

      // Metodo para verificar las versiones antiguas
      private void OlderVersions(String phoneNumber){
        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+ phoneNumber));

        if (CheckPermission(Manifest.permission.CALL_PHONE)){
          startActivity(intentCall);
        }else{
          Toast.makeText(ThirdActivity.this, "You declined the access", Toast.LENGTH_SHORT).show();
        }
      }

    });
  }

  public void onRequestPermissionsResult(int requetsCode, @NonNull String[] permissions, @NonNull int[] grantResults){
    // Estamos en el caso del telefono
    switch (requetsCode){
      case PHONE_CALL_CODE:
        String permission = permissions[0];
        int result = grantResults[0];

        if (permission.equals(Manifest.permission.CALL_PHONE)){
          // Comprobar si ha sido aceptado o denegado la peticion de permiso
          if (result == PackageManager.PERMISSION_GRANTED){
            // Concedio su permiso
            String phoneNumber = editTextPhone.getText().toString();
            Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+ phoneNumber));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) return;
            startActivity(intentCall);
          }else{
            // No concedio su permiso
            Toast.makeText(ThirdActivity.this, "You declined the access", Toast.LENGTH_SHORT).show();
          }
        }
        break;
      default:
        super.onRequestPermissionsResult(requetsCode, permissions, grantResults);
        break;
    }
  }

  // Metodo para comprobar permisos
  private boolean CheckPermission(String permission){
    int result = this.checkCallingOrSelfPermission(permission);
    return result == PackageManager.PERMISSION_GRANTED;
  }
}