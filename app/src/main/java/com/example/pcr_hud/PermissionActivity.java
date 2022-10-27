package com.example.pcr_hud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PermissionActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        com.example.pcr_hud.OutlineTextView permission_title = (com.example.pcr_hud.OutlineTextView)findViewById(R.id.tx_permission_title);
        com.example.pcr_hud.OutlineTextView permission_text = (com.example.pcr_hud.OutlineTextView)findViewById(R.id.tx_permission_text);
        ImageView permission_character = (ImageView)findViewById(R.id.img_permission_chracter);
        ImageView permission_notice = (ImageView)findViewById(R.id.img_permission_image);



        //버튼 설정
        ImageButton gps_permission_button = (ImageButton)findViewById(R.id.btn_gps_permission);
        gps_permission_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            }
        });

        //버튼 설정
        ImageButton birght_permission_button = (ImageButton)findViewById(R.id.btn_biright_permission);
        birght_permission_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + PermissionActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //버튼 설정
        ImageButton i_git_it = (ImageButton)findViewById(R.id.btn_i_got_it);
        i_git_it.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                startActivity(mainIntent);
                System.exit(0);
            }
        });


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int permissionCheck = ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                                if(permissionCheck == PackageManager.PERMISSION_GRANTED && Settings.System.canWrite(PermissionActivity.this)){ //둘다 권한이 될경우

                                    birght_permission_button.setVisibility(View.GONE);
                                    gps_permission_button.setVisibility(View.GONE);
                                    i_git_it.setVisibility(View.VISIBLE);
                                    permission_character.setImageResource(R.drawable.chara_ames);
                                    permission_notice.setImageResource(R.drawable.img_routin);
                                    permission_title.setText("확인해주세요!");
                                    permission_text.setText("해당 어플리케이션은 차량에서 사용 할 경우\n충전 상태에 따른 자동화가 불가능합니다.\n\n Tasker, AutoSet, Automate 및 빅스비 루틴을\n이용하여 충전 on/off 루틴 작업을 부탁드립니다.");
                                }

                                else if(permissionCheck == PackageManager.PERMISSION_DENIED && Settings.System.canWrite(PermissionActivity.this)){ //로케이션 권한이 안될경우(로케이션 권한 요청)
                                    birght_permission_button.setVisibility(View.GONE);
                                    gps_permission_button.setVisibility(View.VISIBLE);
                                    permission_character.setImageResource(R.drawable.permission_chara_kasumi);
                                    permission_notice.setImageResource(R.drawable.check_location);
                                    permission_title.setText("로케이션 권한을\n허용해 주세요!");
                                    permission_text.setText("해당 어플리케이션은 상시 속도 측정을 위한 \n로케이션 권한이 필요합니다.\n\n 로케이션 권한 체크 버튼을 누른 후 \n앱 사용 중에만 허용 을 클릭 해주세요");
                                }

                                else if(permissionCheck == PackageManager.PERMISSION_GRANTED && !(Settings.System.canWrite(PermissionActivity.this))){ //밝기 권한이 안될 경우(밝기 권한 요청)
                                    gps_permission_button.setVisibility(View.GONE);
                                    birght_permission_button.setVisibility(View.VISIBLE);
                                    permission_character.setImageResource(R.drawable.permission_chara_ames);
                                    permission_notice.setImageResource(R.drawable.check_bright);
                                    permission_title.setText("시스템 권한을 허용해 주세요!");
                                    permission_text.setText("해당 어플리케이션은 상시 밝기 최대를 위한 \n시스템 권한이 필요합니다.\n\n 밝기 권한 체크 버튼을 누른 후 해당 어플리케이션 권한을 체크 후 뒤로 돌아와 주세요");
                                }
                                else{} // 둘다 안될경우

                                handler.postDelayed(this, 200);

                            }
                        }, 200);

    } // 여기까지 oncreate










}