package com.example.pcr_hud;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity implements LocationListener, DataTransmissionListener {

    //SharedPreferences 활성화
    SharedPreferences hud_pref;
    SharedPreferences.Editor hud_editor;


    //gps 동작을 위한 선언
    private LocationManager locationManager;
    private Location mLastlocation = null;

    //프래그먼트 선언
    private FragmentManager fragmentManager;
    private CharacterFragment CharacterFragment;
    private NaviFragment NaviFragment;
    private AdvanceFragment AdvanceFragment;
    private FragmentTransaction transaction;


    //밝기 조절을 위한 선언
    private WindowManager.LayoutParams params;
    private float brightness;

    private double gps_speed;

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;

    LinearLayout main_gps_layout;
    LinearLayout title_bar;

    LinearLayout date_layout;
    LinearLayout watch_time;

    ImageButton setting_button;
    ImageButton setting_navi;

    ImageView imagetest;
    ImageView image_gps_num_100;
    ImageView image_gps_num_10;
    ImageView image_gps_num_1;
    ImageView api_speedlock;

    ImageView num_10h;
    ImageView num_1h;
    ImageView num_10min;
    ImageView num_1min;
    ImageView num_10s;
    ImageView num_1s;

    ImageView num_1000y;
    ImageView num_100y;
    ImageView num_10y;
    ImageView num_1y;
    ImageView num_10m;
    ImageView num_1m;
    ImageView num_10d;
    ImageView num_1d;



    TypedArray typedArray; // 이미지 배열
    TypedArray typedArray_num; // 숫자 이미지 배열

    Handler NaviOnScreen_Handler = new Handler();
    Handler gps_now_location_Handler = new Handler();
    Handler watch_data_Handler = new Handler();

    int testnum;

    int countmode = 0;
    int max; // 이미지 갯수 최대치 불필요시 삭제 예정
    int speed_count = 0;
    int speed;

    private int network_check;

    // 유저프리패쳐에 기록될 변수들.
    int main_chara_select; // 메인 액티비티에서 캐릭터 변경을 할때 쓰는 변수
    int main_screen_select;
    int main_bright_select;
    int main_hud_select;
    int main_navi_select;
    int main_car_select;
    int main_watch_select;

    int original_bright_status; // 기존 밝기 상태를 저장한다.
    int original_bright_auto; // 기존 자동 밝기모드가 켜져있는지 확인한다.


    //int character_apply_num; // 프래그먼트에서 확인 눌렀을때 넘어오는 변수값
    int mMin;

    //int tempmHour;
    //int tempmMin;

    // 시계 모드용
    int toDayyyyy;
    int toDayyyyy1000;
    int toDayyyyy100;
    int toDayyyyy10;
    int toDayyyyy1;


    int toDayMM;
    int toDayMM10;
    int toDayMM1;

    int toDaydd;
    int toDaydd10;
    int toDaydd1;

    int toDayHH;
    int toDayHH10;
    int toDayHH1;

    int toDayss;
    int toDayss10;
    int toDayss1;

    int toDaymin;
    int toDaymin10;
    int toDaymin1;

    int navi_transmission;


    int gps_check = 5;

    int api_speed;
    TextView speedText;
    TextView gps_speedText;

    int count_distance;


    int navi_distance_check;
    int new_navi_distance_check;

    double latitude;
    double longtitude;

    double gps_now_loacation_array_latitude;
    double gps_now_loacation_array_longtitude;

    double gps_distance_latitude[] = new double[3];
    double gps_distance_longtitude[] = new double[3];

    double navi_lat;
    double navi_lon;

    ////// 이부분은 navi 정보를 파싱하는데 쓰이는 부분이다.//
    double navi_latitude[];
    double navi_longtitude[];
    String totalDistance;
    int turnType[];
    String nameType[];
    String pointType[];
    /////////////////////////////////////////////////

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 몰입모드


        //기존 밝기 저장
        params = getWindow().getAttributes();
        brightness = params.screenBrightness;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면 켜짐 강제 유지
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //화면 켜짐 강제 종료 (onpause에 넣도록 한다.) 사실 안넣어도 앱종료되면 켜짐....
        //onresume() : 실행시
        //onpause() : 종료시

        hud_pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        hud_editor = hud_pref.edit();

        main_chara_select = hud_pref.getInt("main_chara_select", Constants.character_default);
        main_screen_select = hud_pref.getInt("main_screen_select", Constants.advance_screen_landscape);
        main_bright_select = hud_pref.getInt("main_bright_select", Constants.advance_bright_full);
        main_hud_select = hud_pref.getInt("main_hud_select", Constants.advance_HUD_mode);
        main_navi_select = hud_pref.getInt("main_navi_select", Constants.advance_navi_off);
        main_car_select = hud_pref.getInt("main_car_select", Constants.advance_car);
        main_watch_select = hud_pref.getInt("main_watch_select", Constants.advance_watch_off);

        imageInit();


        //권한 체크
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_DENIED || !(Settings.System.canWrite(this))) { //포그라운드 위치 권한 확인
            //위치 권한 요청
            Intent gps_permission_intent = new Intent(this, PermissionActivity.class);
            startActivity(gps_permission_intent);
            finish();
        }
        //프래그먼트 갱신
        fragmentManager = getSupportFragmentManager();
        CharacterFragment = new CharacterFragment();
        AdvanceFragment = new AdvanceFragment();
        NaviFragment = new NaviFragment();




        gps_now_location_check();


        networkcheck();
        network_check();
        // 이미지 배열 초기화 (유저 프리패쳐 사용시 저장된 값을 사용함)



        ///////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////

        max = typedArray.length(); //타입어레이 이미지가 최대수치일경우 (현재 안쓰이고 있으니 지워도 되고 사용해도 된다.)

        //로케이션 매니저 시작
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /* // 미사용 같음. 확실하게 미사용시 바로 삭제 요청함.
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String formatDate = sdf.format(new Date(lastKnownLocation.getTime()));
            //tvTime.setText(": " + formatDate);  //Time
        }
        // GPS 사용 가능 여부 확인
        boolean isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //tvGpsEnable.setText(": " + isEnable);  //GPS Enable

         */



        //인터넷 연결 시 사용하는 함수
        //gps_now_location();


        //현재 gps의 갱신은 1초로 되어있음.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        System.out.println("리퀘스트 요청 1");

        //프리코네 캐릭터 및 넘버 동작 함수
        HUD_run();


        button_set();


    } // 여기까지가 oncreate

    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();
            System.exit(0);

        }
    }


    public void gps_now_location_check() {

        Handler gps_now_location_check_Handler = new Handler();
        gps_now_location_check_Handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                //현재 위도와 경도 좌표 수신
                @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                try {
                    latitude = location.getLatitude();
                    longtitude = location.getLongitude();
                } catch (NullPointerException e) {
                    latitude = 0.0;
                    longtitude = 0.0;
                    System.out.println(" 설마 널값?");
                }
                System.out.println("위도와 경도 : " + latitude + ", " + longtitude); // 값이 비어있는경우 0.0 이 들어오는거 확인 완료.

                gps_now_location_check_Handler.postDelayed(this, 1000);


            }
        }, 1000);
    }


    private void gps_now_location() {

        System.out.println("반복 확인용 : " + testnum);
        testnum++;


        gps_now_location_Handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                if (latitude == 0 || longtitude == 0) { // 0값 원천 차단.
                    System.out.println("좌표가 0값 진입했음");
                    return;
                }


                // 현재 좌표와 신규 좌표의 값 설정

                if (gps_now_loacation_array_latitude == 0 && gps_now_loacation_array_longtitude == 0) { //초기값이 비어있는경우

                    //이건 좌표 거리 비교용 값
                    gps_now_loacation_array_latitude = latitude;
                    gps_now_loacation_array_longtitude = longtitude;


                } else { // 0일때 아예 이값으로 진입이 안되는 경우가생김
                    Location locationO = new Location("point O");
                    locationO.setLatitude(gps_now_loacation_array_latitude);
                    locationO.setLongitude(gps_now_loacation_array_longtitude);

                    Location locationN = new Location("point N");
                    locationN.setLatitude(latitude);
                    locationN.setLongitude(longtitude);


                    double distance = locationO.distanceTo(locationN);

                    System.out.println("좌표에 따른 거리 : " + distance);

                    /*
                    gps_distance_latitude[0]=35.237431207701;
                    gps_distance_longtitude[0]=126.87793387437;

                    gps_distance_latitude[1]=35.237856164051;
                    gps_distance_longtitude[1]=126.87819495169;

                    gps_distance_latitude[2]=35.238331114558;
                    gps_distance_longtitude[2]=126.87844491764;

                    tmap_api_run();

                    */

                    if (distance >= 100) {
                        System.out.println("거리 100m 초과");


                        //크래시 발생 위험있음. (배열 0 값 저장됨.)
                        // 머리가 안돌아가서 걍 하드코딩화 (배열 하나씩 미는 과정)
                        if (gps_distance_latitude[2] != 0 && gps_distance_longtitude[2] != 0) {

                            gps_distance_latitude[0] = gps_distance_latitude[1];
                            gps_distance_longtitude[0] = gps_distance_longtitude[1];

                            gps_distance_latitude[1] = gps_distance_latitude[2];
                            gps_distance_longtitude[1] = gps_distance_longtitude[2];

                            gps_distance_latitude[2] = latitude;
                            gps_distance_longtitude[2] = longtitude;


                            tmap_api_run();

                        } else { // 배열 3이 비어있으니 작업을 해야한다.
                            for (int i = 0; i <= 2; i++) {
                                if (gps_distance_latitude[i] == 0 && gps_distance_longtitude[i] == 0) {
                                    gps_distance_latitude[i] = latitude;
                                    gps_distance_longtitude[i] = longtitude;
                                    break;
                                }

                            }
                        }

                        //기존 좌표는 새로고침 해줘야 다시 계산 할 수 있다.
                        gps_now_loacation_array_latitude = latitude;
                        gps_now_loacation_array_longtitude = longtitude;

                        System.out.println("배열 0번 래티튜드 : " + gps_distance_latitude[0] + " , 배열 0번 롱티튜드 : " + gps_distance_longtitude[0]);
                        System.out.println("배열 1번 래티튜드 : " + gps_distance_latitude[1] + " , 배열 1번 롱티튜드 : " + gps_distance_longtitude[1]);
                        System.out.println("배열 2번 래티튜드 : " + gps_distance_latitude[2] + " , 배열 2번 롱티튜드 : " + gps_distance_longtitude[2]);

                    } else {
                        System.out.println("거리가 300m 보다 짧음");
                    }


                }

                // 거리 값 계산

                System.out.println("도로파악을 위한 이전 좌표 : " + gps_now_loacation_array_latitude + ", " + gps_now_loacation_array_longtitude);
                System.out.println("도로파악을 위한 현재 좌표 : " + latitude + ", " + longtitude);


                gps_now_location_Handler.postDelayed(this, 5000);


            }
        }, 5000);

    }

    private void tmap_api_run() {
        /*
        //혹시모를 비상 검증용
        for(int i=0 ; i<=2 ; i++){
            if(gps_distance_longtitude[i]==0 || gps_distance_latitude[i]==0){
                System.out.println("좌표중에 0 값이 존재해서 돌아가도록 했음.");
                gps_now_location();
            }
        }
        */

        new Thread(new Runnable() {
            @Override
            public void run() {

                //long , lat 순으로 적어야한다.
                //쉼표 변환은 %2C , | 변환은 %7C를 사용한다.
                String coordinate = (gps_distance_longtitude[0] + "%2C" + gps_distance_latitude[0] + "%7C" + gps_distance_longtitude[1] + "%2C" + gps_distance_latitude[1] + "%7C" + gps_distance_longtitude[2] + "%2C" + gps_distance_latitude[2]);
                System.out.println("coordinate : " + coordinate);
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "responseType=2&coords=" + coordinate);

                Request request = new Request.Builder()
                        .url("https://apis.openapi.sk.com/tmap/road/matchToRoads?version=1")
                        .post(body)
                        .addHeader("accept", "application/json")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("appKey", "l7xxc799eea58b0b44619f9d95ae52d6af2c")
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    System.out.println("리스폰스 값 자체가 null 화 되어버림1"); // 이게 진짜임
                    return;
                }

                String message = null;
                try {
                    message = response.body().string();
                } catch (IOException e) {
                    System.out.println("리스폰스 값 자체가 null 화 되어버림2");
                    return;
                }
                System.out.println(message);

                String finalMessage = message;


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //JSON 파싱 시작
                        try {
                            JSONParse(finalMessage);
                        } catch (JSONException e) {
                            System.out.println("파싱용 메세지가 오류발생함.");
                            return;
                        }


                    }
                });
            }
        }).start();
    }

    private void JSONParse(String finalMessage) throws JSONException {
        JSONObject original_JSON;
        try {
            original_JSON = new JSONObject(finalMessage); //전체 배열 가져오기 중복 없음!
        } catch (NullPointerException e) {
            System.out.println("original_JSON 에서 null 값 발생으로 인한 리턴");
            return;
        }

        JSONObject resultData_JSON = original_JSON.optJSONObject("resultData"); // 전체 배열 중 resultdata 아래 있는 데이터 가져오기) 중복 없음!

        if (resultData_JSON == null) { // null 값 검증
            System.out.println("resultData_JSON 0값 발생했음 ");
            return;
        }

        JSONArray matchedPoints_JSON = resultData_JSON.optJSONArray("matchedPoints"); // 전체 배열 다음 다음 배열

        if (matchedPoints_JSON == null) { //null 값 검증
            System.out.println("matchedPoints_JSON null 값 발생");
            return;
        }

        int JSON_speed;

        int speed_on[] = new int[matchedPoints_JSON.length()]; // 몇개 카운팅 되었는지 이용하여 배열 선언

        for (int i = 0; i < matchedPoints_JSON.length(); i++) { // 이 길이는 {} 를 기준으로 파악함.(어레이)
            System.out.println("matchedPoints_JSON 배열 길이 : " + matchedPoints_JSON.length());
            // int poarisng[] = new int[resultData_JSON.length()]; 렝스값을 이용해서 배열 선언하면 거기까지만 됨
            JSONObject jo = matchedPoints_JSON.optJSONObject(i); // for 문 안에서 jo 의 변수안에 배열중 하나 넣는 과정임 (for 0 1 2 3 .... ) 넣은후 아래에서 출력. (이부분의 변수를 배열로 지정하면 배열저장이 가능함.
            System.out.println("jo 의 배열 값 : " + jo + " 그리고 i 의 값 : " + i);
            System.out.println("jo 배열 길이 : " + jo.length());
            if (jo == null) {
                System.out.println("jo 값 마저 null 발생함.");
                return;
            }
            JSON_speed = jo.optInt("speed", 0);
            speed_on[i] = JSON_speed;
            System.out.println("speed_on[" + i + "] 의 값 : " + JSON_speed);


        }


        for (int i = 0; i < speed_on.length; i++) {
            if (api_speed != speed_on[i] && speed_on[i] != 0) {
                api_speed = speed_on[i];
            }

        }
        System.out.println("이 도로의 최종 스피드는 : " + api_speed);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (api_speed) {
                    case 30:
                        api_speedlock.setImageResource(R.drawable.api_30);
                        break;
                    case 50:
                        api_speedlock.setImageResource(R.drawable.api_50);
                        break;
                    case 60:
                        api_speedlock.setImageResource(R.drawable.api_60);
                        break;
                    case 80:
                        api_speedlock.setImageResource(R.drawable.api_80);
                        break;
                    case 100:
                        api_speedlock.setImageResource(R.drawable.api_100);
                        break;
                    case 110:
                        api_speedlock.setImageResource(R.drawable.api_110);
                        break;

                    default:
                        api_speedlock.setImageResource(R.drawable.api_what);
                        break;


                }
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {
        if (gps_check <= 1) {
            gps_check = 5;
        }

        double deltaTime = 0;

        //  getSpeed() 함수를 이용하여 속도를 계산
        // double getSpeed = Double.parseDouble(String.format("%.3f", location.getSpeed())) * 3.6;
        //tvGetSpeed.setText(": " + getSpeed);  //Get Speed
        //speed = Integer.parseInt(String.valueOf(Math.round(getSpeed)));


        // 위치 변경이 두번째로 변경된 경우 계산에 의해 속도 계산 (계산식의 오류가 생기면 이 주석을 풀어서 사용 할 수 있도록 함.)
        if (mLastlocation != null) {
            //시간 간격
            deltaTime = (location.getTime() - mLastlocation.getTime()) / 1000.0;
            //tvTimeDif.setText(": " + deltaTime + " sec");  // Time Difference
            //tvDistDif.setText(": " + mLastlocation.distanceTo(location) + " m");  // Time Difference
            // 속도 계산
            gps_speed = mLastlocation.distanceTo(location) / deltaTime;

            double calSpeed = Double.parseDouble(String.format("%.3f", gps_speed * 3.6));
            //tvCalSpeed.setText(": " + calSpeed);  //Cal Speed
            speed = Integer.parseInt(String.valueOf(Math.round(calSpeed)));
            gps_speedText.setText("calspeed " + calSpeed + " real speed : " + speed);
        }
        // 현재위치를 지난 위치로 변경
        mLastlocation = location;


    }

    /* // 불필요한게 맞다면 지우도록 한다.
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

     */

    /*
    @Override (gps 가 켜져있을경우에 대처 방안인걸로 보임)
    public void onProviderEnabled(String provider) {
        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("리퀘스트 요청 2");
            return;
        }
        // 위치정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        System.out.println("리퀘스트 요청 3");
    }

    // 불필요한게 맞다면 지우도록 한다. (gps 가 꺼져있을경우에 대처 방안인걸로 보임)
    @Override
    public void onProviderDisabled(String provider) {

    }

     */

    @Override
    protected void onResume() {
        super.onResume();
        //권한 체크 (위치)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //권한 체크(밝기)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                //Toast.makeText(this, "onResume: Granted", Toast.LENGTH_SHORT).show();
            }
        }

        // 위치정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 위치정보 가져오기 제거
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }

    }

    public void HUD_run() {

        System.out.println("hud_run 까지 넘어오나 테스트 ");
        //HUD 프리코네 캐릭터 동작 스레드
        new Thread(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HUDmode_chara();
                    }
                });
            }
        }).start();

        //HUD 넘버 동작 스레드
        new Thread(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HUDmode_num();
                    }
                });
            }
        }).start();
    }


    public void HUDmode_chara() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //시속 0~1일때는 멈춤
                //시속 2~29까진 걷기
                //시속 30~49은 걷는거에 이모티콘 추가
                //시속 50~80까지 뛰기
                //시속 80~99까지 뛰기에 이모티콘
                //100부턴 미미뚜기
                countmode++;
                if (countmode == 20) {
                    countmode = 0;
                }

                // 여기에 if 모드 해서 인터넷모드 or 아닌모드 체크해야할듯?


                switch (api_speed) {
                    case 30:
                        System.out.println("시속 30 이미지 변경 진입");
                        if (speed <= 1) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode, -1));
                        } else if (speed >= 2 && speed < 15) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 20, -1));
                        } else if (speed >= 15 && speed < 30) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 40, -1));
                        } else if (speed >= 30) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 60, -1));
                        }
                        break;
                    case 50:
                        System.out.println("시속 50 이미지 변경 진입");
                        if (speed <= 1) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode, -1));
                        } else if (speed >= 2 && speed < 30) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 20, -1));
                        } else if (speed >= 30 && speed < 50) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 40, -1));
                        } else if (speed >= 50) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 60, -1));
                        }
                        break;
                    case 60:
                        System.out.println("시속 60 이미지 변경 진입");
                        if (speed <= 1) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode, -1));
                        } else if (speed >= 2 && speed < 30) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 20, -1));
                        } else if (speed >= 30 && speed < 60) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 40, -1));
                        } else if (speed >= 60) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 60, -1));
                        }
                        break;
                    case 80:
                        System.out.println("시속 80 이미지 변경 진입");
                        if (speed <= 1) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode, -1));
                        } else if (speed >= 2 && speed < 50) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 20, -1));
                        } else if (speed >= 50 && speed < 80) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 40, -1));
                        } else if (speed >= 80) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 60, -1));
                        }
                        break;
                    case 100:
                        System.out.println("시속 100 이미지 변경 진입");
                        if (speed <= 1) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode, -1));
                        } else if (speed >= 2 && speed < 50) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 20, -1));
                        } else if (speed >= 50 && speed < 100) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 40, -1));
                        } else if (speed >= 100) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 60, -1));
                        }
                        break;
                    case 110:
                        System.out.println("시속 110 이미지 변경 진입");
                        if (speed <= 1) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode, -1));
                        } else if (speed >= 2 && speed < 50) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 20, -1));
                        } else if (speed >= 50 && speed < 100) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 40, -1));
                        } else if (speed >= 110) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 60, -1));
                        }
                        break;

                    default:
                        System.out.println("디폴트 진입");
                        if (speed <= 1) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode, -1));
                        } else if (speed >= 2 && speed < 50) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 20, -1));
                        } else if (speed >= 50 && speed < 100) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 40, -1));
                        } else if (speed >= 100) {
                            imagetest.setImageResource(typedArray.getResourceId(countmode + 60, -1));
                        }
                        break;


                }

                /* // 오프라인 모드일때 돌리는 값으로 지정
                if (speed <= 1) {
                    imagetest.setImageResource(typedArray.getResourceId(countmode, -1));
                } else if (speed >= 2 && speed < 50) {
                    imagetest.setImageResource(typedArray.getResourceId(countmode + 20, -1));
                } else if (speed >= 50 && speed < 100) {
                    imagetest.setImageResource(typedArray.getResourceId(countmode + 40, -1));
                } else if (speed >= 100) {
                    imagetest.setImageResource(typedArray.getResourceId(countmode + 60, -1));
                }

                 */

                speed_count = 60 - (speed / 2);//가속도 조정 테스트
                speedText.setText("speed lv : " + speed_count + " real speed : " + speed);


                handler.postDelayed(this, speed_count);

            }
        }, 1000);
    }

    public void HUDmode_num() {
        Handler handler_num = new Handler();
        handler_num.postDelayed(new Runnable() {
            @Override
            public void run() {

                gps_check--;
                if (gps_check <= 0) {
                    gps_speedText.setText("gps 수신 불량으로 인한 측정 불가");
                }

                image_gps_num_100.setImageResource(typedArray_num.getResourceId(speed / 100, -1));
                image_gps_num_10.setImageResource(typedArray_num.getResourceId((speed % 100) / 10, -1));
                image_gps_num_1.setImageResource(typedArray_num.getResourceId((speed % 100) % 10, -1));


                handler_num.postDelayed(this, 1000);


            }
        }, 1000);
    }

    /*
    // 다중상속용 인터페이스
    @Override
    public void dataTransmissionSet(int character_select_num) { //캐릭터 프래그먼트에서 선택한 값 임시 저장
        character_apply_num = character_select_num;
        System.out.println("character_select_num : " + character_select_num);
    }

     */



    public void watch_data(){ // 데이터 리딩



        watch_data_Handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                // 여기에 1초마다 핸들러 필요함.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Date nowDate = new Date();

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy", Locale.KOREAN);
                        toDayyyyy = Integer.parseInt(simpleDateFormat.format(nowDate));
                        num_1000y.setImageResource(typedArray_num.getResourceId(toDayyyyy/1000, -1));
                        num_100y.setImageResource(typedArray_num.getResourceId((toDayyyyy%1000)/100, -1));
                        num_10y.setImageResource(typedArray_num.getResourceId(((toDayyyyy%1000)%100)/10, -1));
                        num_1y.setImageResource(typedArray_num.getResourceId(((toDayyyyy%1000)%100)%10, -1));



                        simpleDateFormat = new SimpleDateFormat("MM", Locale.KOREAN);
                        toDayMM = Integer.parseInt(simpleDateFormat.format(nowDate));
                        num_10m.setImageResource(typedArray_num.getResourceId(toDayMM/10, -1));
                        num_1m.setImageResource(typedArray_num.getResourceId(toDayMM%10, -1));


                        simpleDateFormat = new SimpleDateFormat("dd", Locale.KOREAN);
                        toDaydd = Integer.parseInt(simpleDateFormat.format(nowDate));
                        num_10d.setImageResource(typedArray_num.getResourceId(toDaydd/100, -1));
                        num_1d.setImageResource(typedArray_num.getResourceId(toDaydd%10, -1));


                        simpleDateFormat = new SimpleDateFormat("HH", Locale.KOREAN);
                        toDayHH = Integer.parseInt(simpleDateFormat.format(nowDate));
                        num_10h.setImageResource(typedArray_num.getResourceId(toDayHH/10, -1));
                        num_1h.setImageResource(typedArray_num.getResourceId(toDayHH%10, -1));

                        simpleDateFormat = new SimpleDateFormat("mm", Locale.KOREAN);
                        toDayss = Integer.parseInt(simpleDateFormat.format(nowDate));
                        num_10min.setImageResource(typedArray_num.getResourceId(toDayss/10, -1));
                        num_1min.setImageResource(typedArray_num.getResourceId(toDayss%10, -1));


                        simpleDateFormat = new SimpleDateFormat("ss", Locale.KOREAN);
                        toDaymin = Integer.parseInt(simpleDateFormat.format(nowDate));
                        num_10s.setImageResource(typedArray_num.getResourceId(toDaymin/10, -1));
                        num_1s.setImageResource(typedArray_num.getResourceId(toDaymin%10, -1));
                    }
                });

                watch_data_Handler.postDelayed(this, 1000);


            }
        }, 1000);

























    }




    public void applyTramsmissionSet(int character_select_num_old_apply, int advance_screen_old_apply, int advance_bright_old_apply, int advance_hud_mode_old_apply, int advance_navi_old_apply, int advance_car_old_apply, int advance_watch_old_apply) { //적용을 위한 적용변수값을 임시저장변수값으로 교체
        navi_transmission = Constants.navi_transmission_no;


        //메인액티비티에 적용할 설정 변수들
        main_chara_select = character_select_num_old_apply;
        main_screen_select = advance_screen_old_apply;
        main_bright_select = advance_bright_old_apply;
        main_hud_select = advance_hud_mode_old_apply;
        main_navi_select = advance_navi_old_apply;
        main_car_select = advance_car_old_apply;
        main_watch_select = advance_watch_old_apply;

        //쉐어드 프리퍼런스에 값넣기
        hud_editor.putInt("main_chara_select", main_chara_select);
        hud_editor.commit();
        hud_editor.putInt("main_screen_select", main_screen_select);
        hud_editor.commit();
        hud_editor.putInt("main_bright_select", main_bright_select);
        hud_editor.commit();
        hud_editor.putInt("main_hud_select", main_hud_select);
        hud_editor.commit();
        hud_editor.putInt("main_navi_select", main_navi_select);
        hud_editor.commit();
        hud_editor.putInt("main_car_select", main_car_select);
        hud_editor.commit();
        hud_editor.putInt("main_watch_select", main_watch_select);
        hud_editor.commit();

        System.out.println("main_chara_select : " + main_chara_select);
        System.out.println("main_screen_select : " + main_screen_select);
        System.out.println("main_bright_select : " + main_bright_select);
        System.out.println("main_hud_select : " + main_hud_select);
        System.out.println("main_navi_select : " + main_navi_select);
        System.out.println("main_car_select : " + main_car_select);
        System.out.println("main_watch_select : " + main_watch_select);


        //실제 동작 변경 소스 넣으면된다.

        imageInit();


    }

    public void naviTransmissionSet(String activity_lat, String activity_lon,int character_select_num_old_apply,int advance_screen_old_apply, int advance_bright_old_apply, int advance_hud_mode_old_apply, int advance_navi_old_apply, int advance_car_old_apply, int advance_watch_old_apply) {
        navi_transmission = Constants.navi_transmission_yes;
        //중복실행 방지
        if (NaviOnScreen_Handler != null) {
            NaviOnScreen_Handler.removeMessages(0);
        }
        imageInit();

        main_chara_select = character_select_num_old_apply;
        main_screen_select = advance_screen_old_apply;
        main_bright_select = advance_bright_old_apply;
        main_hud_select = advance_hud_mode_old_apply;
        main_navi_select = advance_navi_old_apply;
        main_car_select = advance_car_old_apply;
        main_watch_select = advance_watch_old_apply;

        ImageView img_navi = (ImageView) findViewById(R.id.navi_img);
        TextView tx_navi = (TextView) findViewById(R.id.navi_tx);
        ImageButton btn_navi_end = (ImageButton) findViewById(R.id.navi_end);





        // 네비모드에서의 신규 추가용



        btn_navi_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageInit();
                if (NaviOnScreen_Handler != null) {
                    NaviOnScreen_Handler.removeMessages(0);
                }


            }
        });


        System.out.println(" 최종적으로 넘어왔는지 lat 확인 : " + activity_lat);
        System.out.println(" 최종적으로 넘어왔는지 lon 확인 : " + activity_lon);
        navi_lat = Double.parseDouble(activity_lat);
        navi_lon = Double.parseDouble(activity_lon);

        // 이게되면 네비 종료기능도 만들어봐야한다. (메인화면에 종료버튼 생기게하면됨)
        gps_navigate(img_navi, tx_navi);

    }

    public void navicalcleTransmissionSet(int character_select_num_old_apply,int advance_screen_old_apply, int advance_bright_old_apply, int advance_hud_mode_old_apply, int advance_navi_old_apply, int advance_car_old_apply, int advance_watch_old_apply) {
        hideNavigationBar();
        main_chara_select = character_select_num_old_apply;
        main_screen_select = advance_screen_old_apply;
        main_bright_select = advance_bright_old_apply;
        main_hud_select = advance_hud_mode_old_apply;
        main_navi_select = advance_navi_old_apply;
        main_car_select = advance_car_old_apply;
        main_watch_select = advance_watch_old_apply;
        navi_transmission = Constants.navi_transmission_no;
        imageInit();

    }

    public void findViewByIdSet() {
        setting_button = (ImageButton) findViewById(R.id.setting_fragment);
        setting_navi = (ImageButton) findViewById(R.id.navi_fragment);

        main_gps_layout = (LinearLayout) findViewById(R.id.gps_layout);
        title_bar = (LinearLayout) findViewById(R.id.title_bar);

        date_layout = (LinearLayout) findViewById(R.id.date_layout);
        watch_time = (LinearLayout) findViewById(R.id.watch_time);

        speedText = (TextView) findViewById(R.id.test_text);
        gps_speedText = (TextView) findViewById(R.id.test_real_text);

        imagetest = (ImageView) findViewById(R.id.what);
        image_gps_num_100 = (ImageView) findViewById(R.id.gps_num_100);
        image_gps_num_10 = (ImageView) findViewById(R.id.gps_num_10);
        image_gps_num_1 = (ImageView) findViewById(R.id.gps_num_1);

        api_speedlock = (ImageView) findViewById(R.id.api_speed);

        num_10h = (ImageView) findViewById(R.id.num_10h);
        num_1h = (ImageView) findViewById(R.id.num_1h);
        num_10min = (ImageView) findViewById(R.id.num_10min);
        num_1min = (ImageView) findViewById(R.id.num_1min);
        num_10s = (ImageView) findViewById(R.id.num_10s);
        num_1s = (ImageView) findViewById(R.id.num_1s);

        num_1000y = (ImageView) findViewById(R.id.num_1000y);
        num_100y = (ImageView) findViewById(R.id.num_10y);
        num_10y = (ImageView) findViewById(R.id.num_10y);
        num_1y = (ImageView) findViewById(R.id.num_1y);
        num_10m = (ImageView) findViewById(R.id.num_10m);
        num_1m = (ImageView) findViewById(R.id.num_1m);
        num_10d = (ImageView) findViewById(R.id.num_10d);
        num_1d = (ImageView) findViewById(R.id.num_1d);




    }

    public void button_set() {
        //버튼 설정
        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //액티비티에서 값 전송하기
                Bundle Characterbundle = new Bundle(7); // 파라미터의 숫자는 전달하려는 값의 갯수
                Characterbundle.putInt("main_chara_select", main_chara_select);
                Characterbundle.putInt("main_screen_select", main_screen_select);
                Characterbundle.putInt("main_bright_select", main_bright_select);
                Characterbundle.putInt("main_hud_select", main_hud_select);
                Characterbundle.putInt("main_navi_select", main_navi_select);
                Characterbundle.putInt("main_car_select", main_car_select);
                Characterbundle.putInt("main_watch_select", main_watch_select);

                CharacterFragment.setArguments(Characterbundle);

                transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
                transaction.replace(R.id.main_background, CharacterFragment).commitAllowingStateLoss();
            }
        });

        //버튼 설정
        setting_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle Searchbundle = new Bundle(9); // 파라미터의 숫자는 전달하려는 값의 갯수
                Searchbundle.putString("search_longtitude", String.valueOf(longtitude));
                Searchbundle.putString("search_lattitude", String.valueOf(latitude));

                Searchbundle.putInt("main_chara_select", main_chara_select);
                Searchbundle.putInt("main_screen_select", main_screen_select);
                Searchbundle.putInt("main_bright_select", main_bright_select);
                Searchbundle.putInt("main_hud_select", main_hud_select);
                Searchbundle.putInt("main_navi_select", main_navi_select);
                Searchbundle.putInt("main_car_select", main_car_select);
                Searchbundle.putInt("main_watch_select", main_watch_select);

                NaviFragment.setArguments(Searchbundle);


                transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
                transaction.replace(R.id.main_background, NaviFragment).commitAllowingStateLoss();

            }
        });

    }


    private void gps_navigate(ImageView img_navi, TextView tx_navi) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient navi_client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"tollgateFareOption\":16,\"roadType\":32,\"directionOption\":1,\"endX\":" + navi_lon + ",\"endY\":" + navi_lat + ",\"endRpFlag\":\"G\",\"reqCoordType\":\"WGS84GEO\",\"startX\":" + longtitude + ",\"startY\":" + latitude + ",\"speed\":10,\"uncetaintyP\":1,\"uncetaintyA\":1,\"uncetaintyAP\":1,\"carType\":0,\"detailPosFlag\":\"2\",\"resCoordType\":\"WGS84GEO\",\"sort\":\"index\"}");

                Request navi_request = new Request.Builder()
                        .url("https://apis.openapi.sk.com/tmap/routes?version=1&callback=function")
                        .post(body)
                        .addHeader("accept", "application/json")
                        .addHeader("content-type", "application/json")
                        .addHeader("appKey", "l7xx1317e6cad24d4f0d8048aa7336e5623b")
                        .build();

                Response response = null;
                try {
                    response = navi_client.newCall(navi_request).execute();
                } catch (IOException e) {
                    System.out.println("리스폰스 값 자체가 null 화 되어버림1"); // 이게 진짜임
                    return;
                }

                String message = null;
                try {
                    message = response.body().string();
                } catch (IOException e) {
                    System.out.println("리스폰스 값 자체가 null 화 되어버림2");
                    return;
                }
                System.out.println("네비 파싱전 메시지 : " + message);

                String navi_finalMessage = message;


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //JSON 파싱 시작
                        try {
                            JSONParse_Navi(navi_finalMessage, img_navi, tx_navi);
                        } catch (JSONException e) {
                            System.out.println("파싱용 메세지가 오류발생함.");
                            return;
                        }


                    }
                });


            }
        }).start();
    }

    private void JSONParse_Navi(String navi_finalMessage, ImageView img_navi, TextView tx_navi) throws JSONException {
        int coordpoint = 0; // 함수에서 초기화 해도 됨.
        totalDistance = null;
        JSONObject original_JSON;
        try {
            original_JSON = new JSONObject(navi_finalMessage); //전체 배열 가져오기 중복 없음!
            System.out.println("original_JSON 정리 : " + original_JSON);
        } catch (NullPointerException e) {
            System.out.println("original_JSON 에서 null 값 발생으로 인한 리턴");
            return;
        }

        JSONArray matchedPoints_JSON = original_JSON.optJSONArray("features"); // 전체 배열 다음 다음 배열

        for (int i = 0; i < matchedPoints_JSON.length(); i++) {
            System.out.println("matchedPoints_JSON 배열" + i + " 값 :  " + matchedPoints_JSON);
        }

        navi_latitude = new double[matchedPoints_JSON.length()];
        navi_longtitude = new double[matchedPoints_JSON.length()];
        turnType = new int[matchedPoints_JSON.length()];
        nameType = new String[matchedPoints_JSON.length()];
        pointType = new String[matchedPoints_JSON.length()];

        //배열 검증용?
        for (int i = 0; i < matchedPoints_JSON.length(); i++) {
            JSONObject array_json = matchedPoints_JSON.optJSONObject(i); // 전체 배열 다음 다음 배열

            /////////////////////////// 지오메트리 값 가져오기/////////////////////////////////////////////
            JSONObject geometry = array_json.optJSONObject("geometry"); // 전체 배열 중 resultdata 아래 있는 데이터 가져오기) 중복 없음!
            System.out.println("jo " + i + " 값 :  " + array_json); // 타입 포인트와 라인스트링 확인 가능
            System.out.println("je 값 : " + i + "  " + geometry); // 세부정보 리스트 확인가능

            System.out.println("매치 제이손 값 " + matchedPoints_JSON.length());

            String type = geometry.getString("type");

            if (type.equals("Point")) { // 포인터 타입
                String coordinate = geometry.getString("coordinates"); //좌표값 찾기
                coordinate = coordinate.replaceAll("\\[", ""); // [] 지우기
                coordinate = coordinate.replaceAll("\\]", "");
                System.out.println(coordinate);
                String cutArray[] = coordinate.split(","); // 쉼표로 위,경도 분해


                navi_longtitude[coordpoint] = Double.parseDouble(cutArray[0]);
                navi_latitude[coordpoint] = Double.parseDouble(cutArray[1]);

                System.out.println("coordinate 의 값 : " + cutArray[0]);
                System.out.println("coordinate 의 값 : " + cutArray[1]);

                /////////// 안내 내역 수정 좌표 properties ///////////////////////////////////////////////////////

                JSONObject properties = array_json.optJSONObject("properties"); // 전체 배열 중 resultdata 아래 있는 데이터 가져오기) 중복 없음!

                try {
                    totalDistance = properties.getString("totalDistance");
                    System.out.println("토탈 디스턴스 값 : " + totalDistance);
                } catch (JSONException e) {
                    System.out.println("토탈 디스턴스 값 없어서 에러났을껄?");
                }

                turnType[coordpoint] = Integer.parseInt(String.valueOf(properties.optInt("turnType", 999)));
                System.out.println("토탈 디스턴스 값 : " + totalDistance);

                nameType[coordpoint] = properties.optString("name", "직진");
                if (nameType[coordpoint].equals("")) {
                    nameType[coordpoint] = " ";
                }

                pointType[coordpoint] = properties.optString("pointType");

                ////////////////////////////////////////////////////////////////////////////////////


                coordpoint++; // 라인스트링 데이터값 피하기 위해 따로 변수 증가


            }
            System.out.println("jz 의 타입 라인 : " + type);
            /////////////////////////// 지오메트리 값 가져오기/////////////////////////////////////////////

        }
        for (int p = 0; p < matchedPoints_JSON.length(); p++) { // 배열 필터 후 크기 줄이기

            if (navi_latitude[p] == 0 && navi_longtitude[p] == 0) {
                navi_latitude = Arrays.copyOf(navi_latitude, p);
                navi_longtitude = Arrays.copyOf(navi_longtitude, p);
                turnType = Arrays.copyOf(turnType, p);
                pointType = Arrays.copyOf(pointType, p);
                nameType = Arrays.copyOf(nameType, p);
                break;
            }
        }

        for (int k = 0; k < navi_latitude.length; k++) { // 단순 검증용
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("navi_latitude 의 " + k + "배열 값 : " + navi_latitude[k]);
            System.out.println("navi_longtitude 의 " + k + "배열 값 : " + navi_longtitude[k]);
            System.out.println("턴 타입 값 " + k + " : " + turnType[k]);
            System.out.println("포인트 타입 값 " + k + " : " + pointType[k]);
            System.out.println("네임 타입 값 " + k + " : " + nameType[k]);
        }
        NaviOnScreen(img_navi, tx_navi); // 화면에 결과값 표시
    }

    private void NaviOnScreen(ImageView img_navi, TextView tx_navi) {

        count_distance = 1; // 거리를 20m 정도로 들어오면 이 값을 증가시켜서 처리한다.
        // 단, 이 값을 최대 범위이상으로 못올리도록 조정하는 소스코드가 같이 필요하다.

        navi_distance_check = 0;
        new_navi_distance_check = 0;


        NaviOnScreen_Handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Location navi_locationO = new Location("point N_O");
                        navi_locationO.setLatitude(navi_latitude[count_distance]);
                        navi_locationO.setLongitude(navi_longtitude[count_distance]);

                        Location navi_locationN = new Location("point N_N");
                        navi_locationN.setLatitude(latitude);
                        navi_locationN.setLongitude(longtitude);

                        int navi_distance = (int) navi_locationO.distanceTo(navi_locationN);

                        if (navi_distance_check == 0) { // 초기값 설정 안하면 꼬인다.
                            navi_distance_check = navi_distance;
                            System.out.println("초기값 정의중");
                        } else if (navi_distance_check - navi_distance < 0) {
                            System.out.println("정상적으로 진입 못함. 체크 시작");
                            if (navi_distance - navi_distance_check >= 100) { // 100m 이상 거리차가 발생했을 경우
                                System.out.println("확실하게 벗어났음");
                                //작업중지 다시 실행!

                                Handler error_handler = new Handler(Looper.getMainLooper());
                                error_handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "잘못된 진입을 하신것 같습니다. 경로를 재탐색 합니다.", Toast.LENGTH_LONG).show();
                                    }
                                }, 0);


                                if (NaviOnScreen_Handler != null) {
                                    String return_lat = Double.toString(navi_lat);
                                    String return_lon = Double.toString(navi_lon);
                                    NaviOnScreen_Handler.removeMessages(0);
                                    navi_distance_check = 0;
                                    gps_navigate(img_navi, tx_navi);
                                }
                            }
                            //일단 현재좌표 측정은 필수적으로있어야하는건 맞아.
                            // 이부분 계산하는건 계속해서 벌어질거야. 그걸 노려봐.
                        } else {
                            System.out.println("정상적으로 진입 하고 있음");
                            navi_distance_check = navi_distance;
                        }


                        if (navi_distance <= 30) {
                            if (pointType[count_distance].equals("E")) {
                                if (NaviOnScreen_Handler != null) {

                                    Handler end_handler = new Handler(Looper.getMainLooper());
                                    end_handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            count_distance = 0;
                                            System.out.println("멈추기위한 세팅에 진입하는지 확인");
                                            setContentView(R.layout.activity_main);
                                            findViewByIdSet(); // 이건 반드시 넣어야하고
                                            hideNavigationBar(); // 이건 가로모드 확인해서넣어
                                            button_set(); // 이건 네비모드일때 추가버튼 생기는거 고려해야하고
                                            if (NaviOnScreen_Handler != null) {
                                                System.out.println("멈추기위한 세팅, 핸들러 멈춤 동작하는지 확인");
                                                NaviOnScreen_Handler.removeMessages(0);
                                            }
                                        }
                                    }, 0);


                                }
                            }
                            //0처리하고 다시 실행하도록 함.
                            navi_distance_check = 0;
                            count_distance++;

                        }


                        runOnUiThread(new Runnable() { // 지금 배열의 최대 크기만큼 반복하고 있음. 원인찾아야함.
                            @Override
                            public void run() {
                                int lid = MainActivity.this.getResources().getIdentifier("turn_" + turnType[count_distance], "drawable", MainActivity.this.getPackageName());
                                //getString(getResources().getIdentifier("turn_"+turnType[0], "string", getPackageName())) 이렇게 해야 string 값을 제대로 가져 올 수 있음.
                                System.out.println("실제로 턴타입 제대로 가지고오고있는지 확인용 : " + getString(getResources().getIdentifier("turn_" + turnType[count_distance], "string", getPackageName())));
                                String TurnTypeName = getString(getResources().getIdentifier("turn_" + turnType[count_distance], "string", getPackageName()));
                                // 턴타입, 거리, 어디서 돌아야 할지
                                tx_navi.setText(navi_distance + "m 앞 " + nameType[count_distance] + " 에서 " + TurnTypeName);  //Time
                                // NaviOnScreen_Handler.removeMessages(0); 핸들러 정지 하는 방법
                                img_navi.setImageResource(lid);
                                tx_navi.setScaleX(-1f);
                                img_navi.setScaleX(-1f);

                            }
                        });
                    }
                }).start();

                NaviOnScreen_Handler.postDelayed(this, 1000);
            }
        }, 1000);

    }

    /*
    @Override
    public void fragmenttempData(int move_another_fragment, int character_select_fragment) { //캐릭터 설정 프래그먼트에서 세부설정 프래그먼트로 넘어갈때 쓰는 함수
        tempmHour = move_another_fragment;
        tempmMin = character_select_fragment;

        System.out.println("move_another_fragment : " + move_another_fragment);
        System.out.println("character_select_num : " + character_select_fragment);
    }

     */

    //프래그먼트가 바뀔때 작동하게끔 작성한 메소드
    public void onFragmentChange(int fragmentNum, int move_another_fragment, int character_select_fragment, int advance_screen_fragment, int advance_bright_fragment, int advance_hud_mode_fragment, int advance_navi_fragment, int advance_car_fragment, int advance_watch_fragment,
                                 int character_select_num_old_apply, int advance_screen_old_apply, int advance_bright_old_apply, int advance_hud_mode_old_apply, int advance_navi_old_apply, int advance_car_old_apply, int advance_watch_old_apply, int move_first_fragment) {
        //프래그먼트의 번호에 따라 다르게 작동하는 조건문
        if (fragmentNum == 1) {
            Bundle bundle_character = new Bundle(16); // 파라미터의 숫자는 전달하려는 값의 갯수
            fragment_now_data_get(bundle_character, move_another_fragment, move_first_fragment, character_select_fragment, advance_screen_fragment, advance_bright_fragment, advance_hud_mode_fragment, advance_navi_fragment, advance_car_fragment, advance_watch_fragment, character_select_num_old_apply, advance_screen_old_apply, advance_bright_old_apply, advance_hud_mode_old_apply, advance_navi_old_apply, advance_car_old_apply, advance_watch_old_apply);
            CharacterFragment.setArguments(bundle_character);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_background, CharacterFragment).commit();

        } else if (fragmentNum == 2) {
            Bundle bundle_advance = new Bundle(16); // 파라미터의 숫자는 전달하려는 값의 갯수
            fragment_now_data_get(bundle_advance, move_another_fragment, move_first_fragment, character_select_fragment, advance_screen_fragment, advance_bright_fragment, advance_hud_mode_fragment, advance_navi_fragment, advance_car_fragment, advance_watch_fragment, character_select_num_old_apply, advance_screen_old_apply, advance_bright_old_apply, advance_hud_mode_old_apply, advance_navi_old_apply, advance_car_old_apply, advance_watch_old_apply);
            AdvanceFragment.setArguments(bundle_advance);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_background, AdvanceFragment).commit();
        }
    }

    private void fragment_now_data_get(Bundle bundle_fragment, int move_another_fragment, int move_first_fragment, int character_select_fragment, int advance_screen_fragment, int advance_bright_fragment, int advance_hud_mode_fragment, int advance_navi_fragment, int advance_car_fragment, int advance_watch_fragment, int character_select_num_old_apply, int advance_screen_old_apply, int advance_bright_old_apply, int advance_hud_mode_old_apply, int advance_navi_old_apply, int advance_car_old_apply, int advance_watch_old_apply) {
        bundle_fragment.putInt("move_another_fragment", move_another_fragment); // 프래그먼트에서 프래그먼트인지 프래그먼트에서 액티비티 인지 확인하는 변수
        bundle_fragment.putInt("move_first_fragment", move_first_fragment); // 프래그먼트에서 처음으로 이동했을때 체크하기 위한 변수

        bundle_fragment.putInt("character_select_fragment", character_select_fragment);
        bundle_fragment.putInt("advance_screen_fragment", advance_screen_fragment); // 프래그먼트에서 프래그먼트인지 프래그먼트에서 액티비티 인지 확인하는 변수
        bundle_fragment.putInt("advance_bright_fragment", advance_bright_fragment);
        bundle_fragment.putInt("advance_hud_mode_fragment", advance_hud_mode_fragment); // 프래그먼트에서 프래그먼트인지 프래그먼트에서 액티비티 인지 확인하는 변수
        bundle_fragment.putInt("advance_navi_fragment", advance_navi_fragment);
        bundle_fragment.putInt("advance_car_fragment", advance_car_fragment); // 프래그먼트에서 프래그먼트인지 프래그먼트에서 액티비티 인지 확인하는 변수
        bundle_fragment.putInt("advance_watch_fragment", advance_watch_fragment);

        //기존값도 같이 옮겨야 초기 처리가 가능함.
        bundle_fragment.putInt("character_select_num_old_apply", character_select_num_old_apply);
        bundle_fragment.putInt("advance_screen_old_apply", advance_screen_old_apply); // 프래그먼트에서 프래그먼트인지 프래그먼트에서 액티비티 인지 확인하는 변수
        bundle_fragment.putInt("advance_bright_old_apply", advance_bright_old_apply);
        bundle_fragment.putInt("advance_hud_mode_old_apply", advance_hud_mode_old_apply); // 프래그먼트에서 프래그먼트인지 프래그먼트에서 액티비티 인지 확인하는 변수
        bundle_fragment.putInt("advance_navi_old_apply", advance_navi_old_apply);
        bundle_fragment.putInt("advance_car_old_apply", advance_car_old_apply); // 프래그먼트에서 프래그먼트인지 프래그먼트에서 액티비티 인지 확인하는 변수
        bundle_fragment.putInt("advance_watch_old_apply", advance_watch_old_apply);
    }


    public void original_bright_full() {

        // 최대 밝기로 설정
        params.screenBrightness = 1f;
        // 밝기 설정 적용
        getWindow().setAttributes(params);
    }

    public void original_bright_apply() {
        params.screenBrightness = brightness;
        getWindow().setAttributes(params);
    }

    public void imageInit() {

        //캐릭터 설정
        typedArray = getResources().obtainTypedArray(R.array.png_chara_00 + main_chara_select); //chara_select가 배열 번호가된다. //이부분 셀렉트번호 어레이 버그가발생했다.
        typedArray_num = getResources().obtainTypedArray(R.array.png_num_0); //chara_select가 배열 번호가된다.

        //세부 설정창

        if (main_screen_select == Constants.advance_screen_landscape) {
            if(navi_transmission == Constants.navi_transmission_no) {
                setContentView(R.layout.activity_main);
            }
            else if(navi_transmission == Constants.navi_transmission_yes){
                setContentView(R.layout.activity_navi);
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            findViewByIdSet(); // 이건 반드시 넣어야하고
            hideNavigationBar(); // 이건 가로모드 확인해서넣어
            button_set(); // 이건 네비모드일때 추가버튼 생기는거 고려해야하고

        } else {
            if(navi_transmission == Constants.navi_transmission_no) {
                setContentView(R.layout.activity_main_land);
            }
            else if(navi_transmission == Constants.navi_transmission_yes){
                setContentView(R.layout.activity_navi_land);
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            findViewByIdSet(); // 이건 반드시 넣어야하고
            hideNavigationBar(); // 이건 가로모드 확인해서넣어
            button_set(); // 이건 네비모드일때 추가버튼 생기는거 고려해야하고
        }

        if (main_bright_select == Constants.advance_bright_full) {
            original_bright_full(); //최대밝기 옵션도 같이 있음

        } else {
            original_bright_apply();
        }

        if (main_hud_select == Constants.advance_HUD_mode) {
            main_gps_layout.setScaleX(-1f); ////////////////// 좌우반전용 소스
            title_bar.setScaleX(-1f);
            date_layout.setScaleX(-1f);
            //imagetest.setScaleY(-1f); // 상하반전용 소스
        } else {
            main_gps_layout.setScaleX(1f); ////////////////// 정상용
            title_bar.setScaleX(1f);
            date_layout.setScaleX(1f);
        }

        if (main_navi_select == Constants.advance_navi_off) {
            //네비와 동작모드 삭제
            setting_navi.setVisibility(View.INVISIBLE);

            if(NaviOnScreen_Handler != null){

            NaviOnScreen_Handler.removeMessages(0);
            }

            if(NaviOnScreen_Handler != null) {

                gps_now_location_Handler.removeMessages(0);
            }

            offline_mode_check();
        } else {

                gps_now_location();

            setting_navi.setVisibility(View.VISIBLE);
            //네비와 동작모드 등장
        }


        if (main_watch_select == Constants.advance_watch_off) {
            //이건 시스템 구현해야 온오프됨

            if(watch_data_Handler != null) {

                watch_data_Handler.removeMessages(0);
            }

            date_layout.setVisibility(View.INVISIBLE);
            watch_time.setVisibility(View.INVISIBLE);

            //invisible 처리

        } else {
            watch_data();
            //visible 처리

            date_layout.setVisibility(View.VISIBLE);
            watch_time.setVisibility(View.VISIBLE);
        }




    }

    private void networkcheck() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            cm.registerNetworkCallback(
                    builder.build(),
                    new ConnectivityManager.NetworkCallback() {

                        @Override
                        public void onAvailable(@NonNull Network network) {
                            super.onAvailable(network);
                            network_check = 1;

                            int connectionState = -1;
                            Network nw = cm.getActiveNetwork();
                            if (nw != null) {
                                NetworkCapabilities actNw = cm.getNetworkCapabilities(nw);
                                if (actNw != null) {
                                    if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                        connectionState = NetworkCapabilities.TRANSPORT_WIFI;
                                    } else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                        connectionState = NetworkCapabilities.TRANSPORT_CELLULAR;
                                    } else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                                        connectionState = NetworkCapabilities.TRANSPORT_ETHERNET;
                                    } else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                                        connectionState = NetworkCapabilities.TRANSPORT_BLUETOOTH;
                                    } else {
                                        connectionState = 9;
                                    }
                                }
                            }

                            if (connectionState >= 0) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        // if 문넣어서 네비게이션 모드 설정에 따른 비지블 설정 추가
                                        if (main_navi_select == Constants.advance_navi_off) {
                                            //네비와 동작모드 삭제
                                            setting_navi.setVisibility(View.INVISIBLE);
                                            if (NaviOnScreen_Handler != null) {
                                                NaviOnScreen_Handler.removeMessages(0);
                                            }
                                            if(gps_now_location_Handler != null) {
                                                gps_now_location_Handler.removeMessages(0);
                                            }
                                            //여기에도 자동차/오토바이 모드가 하나 들어가야한다.
                                            offline_mode_check();
                                        } else {
                                            setting_navi.setVisibility(View.VISIBLE);
                                            //네비와 동작모드 등장

                                                    Toast.makeText(MainActivity.this, "나우 로케이션 진입", Toast.LENGTH_SHORT).show();
                                                    gps_now_location();



                                            //이부분 핸들러동작해도 100미터마다 거리계산해서 표지판 나타내는게 없어서 디폴트 설정을 따로해줘야함.
                                            api_speed = 100;
                                            api_speedlock.setImageResource(R.drawable.api_what); // 이미지 변경 요청


                                        }
                                       // Toast.makeText(MainActivity.this, "네트워크가 연결되었습니다. 실시간 도로 기능을 사용 할 수 있습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }


                        @Override
                        public void onLost(@NonNull Network network) {
                            super.onLost(network);
                            network_check = 0;
                            //네트워크 끊어짐
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "네트워크가 끊어졌습니다. 실시간 도로 기능을 사용 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                    setting_navi.setVisibility(View.INVISIBLE);
                                        NaviOnScreen_Handler.removeMessages(0);
                                        gps_now_location_Handler.removeMessages(0);

                                    //여기에 설정 되어있던 카 / 자전거 모드 사용하도록 세팅해야함.
                                    offline_mode_check();
                                }
                            });

                        }
                    }
            );
        }


    }

    private void network_check() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "");

                Request request = new Request.Builder()
                        .url("https://www.google.com")
                        .post(body)
                        .addHeader("accept", "application/json")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("appKey", "l7xxc799eea58b0b44619f9d95ae52d6af2c")
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    //네트워크 끊어짐
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "네트워크가 끊어졌습니다. 실시간 도로 기능을 사용 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            setting_navi.setVisibility(View.INVISIBLE);
                            if (NaviOnScreen_Handler != null) {
                                NaviOnScreen_Handler.removeMessages(0);
                            }

                            //여기에 설정 되어있던 카 / 자전거 모드 사용하도록 세팅해야함.
                            offline_mode_check();
                        }
                    });
                    return;
                }

                String message = null;
                try {
                    message = response.body().string();
                } catch (IOException e) {
                    System.out.println("메시지가 오류 남");
                    return;
                }
                System.out.println(message);
            }

        }).start();


    }

    private void offline_mode_check() {
        if (main_car_select == Constants.advance_car) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    api_speed = 100;
                    api_speedlock.setImageResource(R.drawable.api_car); // 이미지 변경 요청
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    api_speed = 30;
                    api_speedlock.setImageResource(R.drawable.api_bicycle); // 이미지 변경 요청
                }
            });

        }
    }


    // 여기선 네트워크 기능만 차단한다. 다이렉트로
    // 만약 연결시 세팅된 변수를 이용해서 조건부 기능 활성화를 시키도록 한다.

    // 프래그먼트에서는 활성화버튼만 차단시키면 된다.


    private void hideNavigationBar() {

        // 강제 가로모드 구현
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

    }


}