package com.example.pcr_hud;

import static android.content.Context.INPUT_METHOD_SERVICE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NaviFragment extends Fragment {

    MainActivity mainActivity; //(액티비티에서 이동하기) 주 가되는 메인액티비티 선언

    private DataTransmissionListener dataTransmissionSetListener; // 값 전송용 변수 선언

    RecyclerView mRecyclerView = null;
    RecyclerViewAdapter mAdapter = null;
    ArrayList<RecyclerViewItem> mList;

    String poi_address = null;


    private int character_select_num_old_apply;
    private int advance_screen_old_apply;
    private int advance_bright_old_apply;
    private int advance_hud_mode_old_apply;
    private int advance_navi_old_apply;
    private int advance_car_old_apply;
    private int advance_watch_old_apply;

    private String mMainText;
    private String mSubText;
    private String mselNum;
    private String mlat;
    private String mlon;

    private String search_longtitude;
    private String search_lattitude;


    static String activity_lat = null;
    static String activity_lon = null;

    //화면이 붙을때 작동하는 메서드
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


        mainActivity = (MainActivity) getActivity(); //(액티비티에서 이동하기) 현재 소속된 액티비티를 메인 액티비티로 한다.

        if (context instanceof DataTransmissionListener) {
            dataTransmissionSetListener = (DataTransmissionListener) context; // context 처리해서 값 불러오기
        } else {
            throw new RuntimeException(context.toString() + "must implement dataTransmissionSetListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dataTransmissionSetListener = null; // null 로 반환처리
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflaterNavi = inflater.inflate(R.layout.fragment_navi, container, false); // inflater 때문에 선언을 추가해야함
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final EditText tx_navi_address = (EditText) inflaterNavi.findViewById(R.id.navi_address);
        ImageButton btn_address_push = (ImageButton) inflaterNavi.findViewById(R.id.address_push);

        ImageButton btn_navi_ok = (ImageButton) inflaterNavi.findViewById(R.id.btn_navi_ok);
        ImageButton btn_navi_cancle = (ImageButton) inflaterNavi.findViewById(R.id.btn_navi_cancle);

        ImageButton btn_navi_lock = (ImageButton) inflaterNavi.findViewById(R.id.btn_navi_lock);

        // 액티비티에서 프래그먼트로 값 받아오기
        Bundle Searchbundle = getArguments();

        if (Searchbundle != null) {
            search_longtitude = Searchbundle.getString("search_longtitude"); // character_num 변수 이름 변경 요청
            search_lattitude = Searchbundle.getString("search_lattitude"); // character_num 변수 이름 변경 요청

            character_select_num_old_apply = Searchbundle.getInt("main_chara_select", 0); // // 이건 액티비티에서 받아오는 변수고
            advance_screen_old_apply = Searchbundle.getInt("main_screen_select", 0); // 이건 액티비티에서 받아오는 변수
            advance_bright_old_apply = Searchbundle.getInt("main_bright_select", 0); // 이건 액티비티에서 받아오는 변수
            advance_hud_mode_old_apply = Searchbundle.getInt("main_hud_select", 0); // 이건 액티비티에서 받아오는 변수
            advance_navi_old_apply = Searchbundle.getInt("main_navi_select", 0); // 이건 액티비티에서 받아오는 변수
            advance_car_old_apply = Searchbundle.getInt("main_car_select", 0); // 이건 액티비티에서 받아오는 변수
            advance_watch_old_apply = Searchbundle.getInt("main_watch_select", 0); // 이건 액티비티에서 받아오는 변수



            System.out.println("좌표 넘어왔는지 확인 : " + search_longtitude);
            System.out.println("좌표 넘어왔는지 확인 : " + search_lattitude);
        }


        tx_navi_address.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        hide_keyboard();
                        address_data_push(tx_navi_address, inflaterNavi, btn_navi_lock);
                        tx_navi_address.setText(null);
                        break;
                }
                return true;
            }
        });

        btn_address_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hide_keyboard();

                address_data_push(tx_navi_address, inflaterNavi, btn_navi_lock);
                tx_navi_address.setText(null);


            }
        });

        btn_navi_ok.setOnClickListener(new View.OnClickListener() { // 주소 정하고 네비 시작할때
            @Override
            public void onClick(View v) {

                btn_navi_lock.setVisibility(View.VISIBLE);

                dataTransmissionSetListener.naviTransmissionSet(activity_lat, activity_lon, character_select_num_old_apply, advance_screen_old_apply, advance_bright_old_apply, advance_hud_mode_old_apply, advance_navi_old_apply, advance_car_old_apply, advance_watch_old_apply);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().remove(NaviFragment.this).commit();
                manager.popBackStack();

                tx_navi_address.setText(null);

            }
        });

        btn_navi_cancle.setOnClickListener(new View.OnClickListener() { // 주소 정하든말든 네비 안할때
            @Override
            public void onClick(View v) {

                btn_navi_lock.setVisibility(View.VISIBLE);


                dataTransmissionSetListener.navicalcleTransmissionSet(character_select_num_old_apply, advance_screen_old_apply, advance_bright_old_apply, advance_hud_mode_old_apply, advance_navi_old_apply, advance_car_old_apply, advance_watch_old_apply);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().remove(NaviFragment.this).commit();
                manager.popBackStack();
                tx_navi_address.setText(null);
            }
        });


        return inflaterNavi; // 버튼을 누르는 뷰를 리턴해야 현재버튼으로 처리가 됨.
    }


    private void addItem(String mainText, String subText, String selectNum, String lat, String lon) {
        RecyclerViewItem item = new RecyclerViewItem();

        item.setMainTitle(mainText);
        item.setSubTitle(subText);
        item.setlat(lat);
        item.setlon(lon);

        mList.add(item);
    }

    public static void select_addressnumber(String address_number, String lat, String lon) {
        System.out.println("값이 넘어왔는지 확인용 : " + address_number);

        activity_lat = lat;
        activity_lon = lon;
        System.out.println("값이 넘어왔는지 확인용 : " + activity_lat);
        System.out.println("값이 넘어왔는지 확인용 : " + activity_lon);


    }


    private void hide_keyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void JSONParse_POI(String finalMessage) throws JSONException {
        JSONObject original_JSON;


        try {
            original_JSON = new JSONObject(finalMessage); //전체 배열 가져오기 중복 없음!
            System.out.println("original_JSON 정리 : " + original_JSON);
        } catch (NullPointerException e) {
            System.out.println("original_JSON 에서 null 값 발생으로 인한 리턴");
            return;
        }

        JSONObject searchPoiInfo = original_JSON.optJSONObject("searchPoiInfo"); // 전체 배열 중 resultdata 아래 있는 데이터 가져오기) 중복 없음!

        if (original_JSON == null) { // null 값 검증
            System.out.println("original_JSON 0값 발생했음 ");
            return;
        }

        JSONObject pois = searchPoiInfo.optJSONObject("pois"); // 전체 배열 중 resultdata 아래 있는 데이터 가져오기) 중복 없음!

        if (searchPoiInfo == null) { // null 값 검증
            System.out.println("searchPoiInfo 0값 발생했음 ");
            return;
        }

        JSONArray poi = pois.optJSONArray("poi"); // 전체 배열 다음 다음 배열

        if (poi == null) { // null 값 검증
            System.out.println("poi 0값 발생했음 ");
            return;
        }


        String name[] = new String[poi.length()];
        String upperAddrName[] = new String[poi.length()];
        String middleAddrName[] = new String[poi.length()];
        String lowerAddrName[] = new String[poi.length()];
        String totalAddrName[] = new String[poi.length()];
        String frontLat[] = new String[poi.length()];
        String frontLon[] = new String[poi.length()];
        String check_poi[] = new String[poi.length()];

        for (int i = 0; i < poi.length(); i++) {
            System.out.println(i + " 배열의 값 : " + poi);
            JSONObject array_json = poi.optJSONObject(i); // 배열로했던 장소중 I 의 배열을 참조
            name[i] = array_json.getString("name"); //좌표값 찾기
            upperAddrName[i] = array_json.optString("upperAddrName", "."); //좌표값 찾기
            middleAddrName[i] = array_json.optString("middleAddrName", "."); //좌표값 찾기
            lowerAddrName[i] = array_json.optString("lowerAddrName", "."); //좌표값 찾기
            frontLat[i] = array_json.optString("frontLat", "0");
            frontLon[i] = array_json.optString("frontLon", "0");
            check_poi[i] = Integer.toString(i);

            totalAddrName[i] = (upperAddrName[i] + " " + middleAddrName[i] + " " + lowerAddrName[i]);

            System.out.println("체크값 표시 : " + check_poi[i]);
            System.out.println("중요 주소만 표시 : " + name[i]);
            System.out.println("모든 주소만 표시 : " + totalAddrName[i]);
            System.out.println("해당 좌표 : " + frontLat[i] + " " + frontLon[i]);

            //이부분에서 파싱한데이터를 기반으로 변수처리해주면된다.
            mMainText = name[i];
            mSubText = totalAddrName[i];
            mselNum = check_poi[i];
            mlat = frontLat[i];
            mlon = frontLon[i];
            addItem(mMainText, mSubText, mselNum, mlat, mlon);


        }
        mAdapter.notifyDataSetChanged();

    }

    public void address_data_push(EditText tx_navi_address, View inflaterNavi, ImageView btn_navi_lock) {
        if (tx_navi_address.getText().toString().equals("") || tx_navi_address.getText().toString().equals(null)) {
            return;
        }

        btn_navi_lock.setVisibility(View.INVISIBLE);

        poi_address = null;
        try {
            poi_address = URLEncoder.encode(tx_navi_address.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("주소 입력값 확인 : " + poi_address);

        //리사이클러 뷰 생성
        mRecyclerView = inflaterNavi.findViewById(R.id.navi_recycler);
        mList = new ArrayList<>();

        mAdapter = new RecyclerViewAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        // 여기에 주표 파싱용 필요함.
        new Thread(new Runnable() {
            @Override
            public void run() {

                //long , lat 순으로 적어야한다.
                //쉼표 변환은 %2C , | 변환은 %7C를 사용한다.

                OkHttpClient navi_client = new OkHttpClient();
                Request poi_request = new Request.Builder()
                        .url("https://apis.openapi.sk.com/tmap/pois?version=1&searchKeyword=" + poi_address + "&searchType=all&searchtypCd=R&centerLon=" + search_longtitude + "&centerLat=" + search_lattitude + "&reqCoordType=WGS84GEO&resCoordType=WGS84GEO&radius=0&page=1&count=20&multiPoint=N&poiGroupYn=N")
                        .get()
                        .addHeader("Accept", "application/json")
                        .addHeader("appKey", "l7xx1317e6cad24d4f0d8048aa7336e5623b")
                        .build();

                Response poi_response = null;
                try {
                    poi_response = navi_client.newCall(poi_request).execute();
                } catch (IOException e) {
                    System.out.println("리스폰스 값 자체가 null 화 되어버림1"); // 이게 진짜임
                    return;
                }

                String poi_message = null;
                try {
                    poi_message = poi_response.body().string();
                } catch (IOException e) {
                    System.out.println("리스폰스 값 자체가 null 화 되어버림2");
                    return;
                }
                System.out.println("네비 파싱전 메시지 : " + poi_message);

                String poi_finalMessage = poi_message;


                //fragment 에서 runOnUiThread 방법
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONParse_POI(poi_finalMessage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                });


            }
        }).start();
    }


}