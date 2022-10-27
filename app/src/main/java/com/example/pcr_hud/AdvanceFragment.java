package com.example.pcr_hud;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdvanceFragment extends Fragment {

    private int move_another_fragment;
    private int move_first_fragment;

    private int character_select_fragment;
    private int character_select_num_old_apply;

    private int network_check;

    // 액티비티에 적용할 데이터
    private int advance_screen_old_apply;
    private int advance_bright_old_apply;
    private int advance_hud_mode_old_apply;
    private int advance_navi_old_apply;
    private int advance_car_old_apply;
    private int advance_watch_old_apply;

    //프래그먼트에서 실시간 적용중인 데이터 (프래그먼트끼리 이동중일때 사용)
    private int advance_screen_fragment;
    private int advance_bright_fragment;
    private int advance_hud_mode_fragment;
    private int advance_navi_fragment;
    private int advance_car_fragment;
    private int advance_watch_fragment;


    private ImageButton C_screen_landscape;
    private ImageButton C_screen_portrait;
    private ImageButton C_always_bright;
    private ImageButton C_system_bright;
    private ImageButton C_HUD_mode;
    private ImageButton C_normal_mode;
    private ImageButton C_navi_on;
    private ImageButton C_navi_off;
    private ImageButton C_car;
    private ImageButton C_bicycle;
    private ImageButton C_watch_on;
    private ImageButton C_watch_off;

    private LinearLayout C_network_lock1;
    private LinearLayout C_network_lock2;

    private LinearLayout C_car_lock;
    private LinearLayout C_bicycle_lock;

    private FrameLayout advance_title;


    MainActivity mainActivity; //(액티비티에서 이동하기) 주 가되는 메인액티비티 선언

    private DataTransmissionListener dataTransmissionSetListener; // 값 전송용 변수 선언

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

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (getArguments() != null) {
            //액티비티에서 프래그먼트로 값 받아오기
            Bundle advance_bundle = getArguments();

            if (advance_bundle != null) {
                //advance_screen_old_apply


                get_character_fragment_data(advance_bundle);

            }
            System.out.println("액티비티에서 불러온 값 확인 : " + move_another_fragment);
            System.out.println("액티비티에서 불러온 값 확인 : " + character_select_fragment);
        }




        //프래그먼트 이동 버튼
        View inflaterB = inflater.inflate(R.layout.fragment_advance, container, false); // inflater 때문에 선언을 추가해야함


        // 가로모드 세로모드 버튼
         C_screen_landscape = (ImageButton) inflaterB.findViewById(R.id.btn_screen_landscape);
         C_screen_portrait = (ImageButton) inflaterB.findViewById(R.id.btn_screen_portrait);
         C_always_bright = (ImageButton) inflaterB.findViewById(R.id.btn_always_bright);
         C_system_bright = (ImageButton) inflaterB.findViewById(R.id.btn_system_bright);
         C_HUD_mode = (ImageButton) inflaterB.findViewById(R.id.btn_HUD_mode);
         C_normal_mode = (ImageButton) inflaterB.findViewById(R.id.btn_normal_mode);
         C_navi_on = (ImageButton) inflaterB.findViewById(R.id.btn_navi_on);
         C_navi_off = (ImageButton) inflaterB.findViewById(R.id.btn_navi_off);
         C_car = (ImageButton) inflaterB.findViewById(R.id.btn_car);
         C_bicycle = (ImageButton) inflaterB.findViewById(R.id.btn_bicycle);
         C_watch_on = (ImageButton) inflaterB.findViewById(R.id.btn_watch_on);
         C_watch_off = (ImageButton) inflaterB.findViewById(R.id.btn_watch_off);
        C_network_lock1 = (LinearLayout) inflaterB.findViewById(R.id.btn_navi_lock1);
        C_network_lock2 = (LinearLayout) inflaterB.findViewById(R.id.btn_navi_lock2);
        C_car_lock =  (LinearLayout) inflaterB.findViewById(R.id.btn_car_lock);
        C_bicycle_lock =  (LinearLayout) inflaterB.findViewById(R.id.btn_bicycle_lock);

        advance_title = (FrameLayout) inflaterB.findViewById(R.id.advance_title);


        C_screen_landscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_screen_landscape.setImageResource(R.drawable.button_p);
                C_screen_portrait.setImageResource(R.drawable.button_non);

                advance_screen_fragment = Constants.advance_screen_landscape;

            }
        });


        C_screen_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_screen_landscape.setImageResource(R.drawable.button_non);
                C_screen_portrait.setImageResource(R.drawable.button_p);

                advance_screen_fragment = Constants.advance_Screen_portrait;
            }
        });


        // 화면밝기 버튼
        C_always_bright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_always_bright.setImageResource(R.drawable.button_p);
                C_system_bright.setImageResource(R.drawable.button_non);

                advance_bright_fragment = Constants.advance_bright_full;


            }
        });


        C_system_bright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_always_bright.setImageResource(R.drawable.button_non);
                C_system_bright.setImageResource(R.drawable.button_p);

                advance_bright_fragment = Constants.advance_bright_system;
            }
        });


        // 좌우반전 버튼
        C_HUD_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_HUD_mode.setImageResource(R.drawable.button_p);
                C_normal_mode.setImageResource(R.drawable.button_non);

                advance_hud_mode_fragment = Constants.advance_HUD_mode;
            }
        });


        C_normal_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_HUD_mode.setImageResource(R.drawable.button_non);
                C_normal_mode.setImageResource(R.drawable.button_p);

                advance_hud_mode_fragment = Constants.advance_normal_mode;
            }
        });


        // 실시간 도로 주행 안내 모드
        C_navi_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_navi_on.setImageResource(R.drawable.button_p);
                C_navi_off.setImageResource(R.drawable.button_non);

                advance_navi_fragment = Constants.advance_navi_on;

                C_car_lock.setVisibility(View.VISIBLE);
                C_bicycle_lock.setVisibility(View.VISIBLE);
            }
        });


        C_navi_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_navi_on.setImageResource(R.drawable.button_non);
                C_navi_off.setImageResource(R.drawable.button_p);

                C_car_lock.setVisibility(View.INVISIBLE);
                C_bicycle_lock.setVisibility(View.INVISIBLE);



                advance_navi_fragment = Constants.advance_navi_off;
            }
        });


        // 실시간 도로 주행 안내 모드


        C_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_car.setImageResource(R.drawable.button_p);
                C_bicycle.setImageResource(R.drawable.button_non);

                advance_car_fragment = Constants.advance_car;
            }
        });


        C_bicycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_car.setImageResource(R.drawable.button_non);
                C_bicycle.setImageResource(R.drawable.button_p);

                advance_car_fragment = Constants.advance_bicyle;
            }
        });


        // 실시간 도로 주행 안내 모드


        C_watch_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_watch_on.setImageResource(R.drawable.button_p);
                C_watch_off.setImageResource(R.drawable.button_non);

                advance_watch_fragment = Constants.advance_watch_on;
            }
        });


        C_watch_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 입력
                C_watch_on.setImageResource(R.drawable.button_non);
                C_watch_off.setImageResource(R.drawable.button_p);

                advance_watch_fragment = Constants.advance_watch_off;
            }
        });


        ImageButton A_character_move = (ImageButton) inflaterB.findViewById(R.id.btn_character_fragment);

        A_character_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //A 프래그먼트에서 임시로 설정한 temp 값을 메인 액티비티로 보냄
                move_another_fragment = Constants.fragment_to_fragment; // 프래그먼트에서 액티비티로이동을 증명하기 위한 변수
                //dataTransmissionSetListener.fragmenttempData(move_another_fragment,character_select_fragment); // 여기에 변수를 넣어서 이동하는 과정을 쓸꺼야.

                mainActivity.onFragmentChange(Constants.character_fragment, move_another_fragment, character_select_fragment, advance_screen_fragment, advance_bright_fragment, advance_hud_mode_fragment, advance_navi_fragment, advance_car_fragment, advance_watch_fragment,
                        character_select_num_old_apply, advance_screen_old_apply, advance_bright_old_apply, advance_hud_mode_old_apply, advance_navi_old_apply, advance_car_old_apply, advance_watch_old_apply, move_first_fragment); // (액티비티에서 이동하기) 숫자 1은 추후 변수처리
            }
        });


        ImageButton A_killexitbutton = (ImageButton) inflaterB.findViewById(R.id.cancel_exit_button);

        //프래그먼트 종료 버튼
        A_killexitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //move_another_fragment = Constants.activity_to_fragment; // 프래그먼트에서 액티비티로이동을 증명하기 위한 변수
                dataTransmissionSetListener.applyTramsmissionSet(character_select_num_old_apply, advance_screen_old_apply, advance_bright_old_apply, advance_hud_mode_old_apply, advance_navi_old_apply, advance_car_old_apply, advance_watch_old_apply); // 현재 적용값은 하나다.
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().remove(AdvanceFragment.this).commit();
                manager.popBackStack();
            }
        });

        ImageButton A_okbutton = (ImageButton) inflaterB.findViewById(R.id.ok_exit_button);

        //프래그먼트 종료 버튼
        A_okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_another_fragment = Constants.activity_to_fragment; // 프래그먼트에서 액티비티로이동을 증명하기 위한 변수
                character_select_num_old_apply = character_select_fragment; // 캐릭터 선택 변수 (캐릭터 프래그먼트용)

                advance_screen_old_apply = advance_screen_fragment; // 화면 모드 변수
                advance_bright_old_apply = advance_bright_fragment; // 밝기 조정 변수
                advance_hud_mode_old_apply = advance_hud_mode_fragment; // hud 모드 변수
                advance_navi_old_apply = advance_navi_fragment; // 네비 모드 변수
                advance_car_old_apply = advance_car_fragment; // 자가용 모드 변수
                advance_watch_old_apply = advance_watch_fragment; // 시계 모드 변수


                dataTransmissionSetListener.applyTramsmissionSet(character_select_num_old_apply, advance_screen_old_apply, advance_bright_old_apply, advance_hud_mode_old_apply, advance_navi_old_apply, advance_car_old_apply, advance_watch_old_apply); // 현재 적용값은 하나다.
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().remove(AdvanceFragment.this).commit();
                manager.popBackStack();
            }
        });

        check_init(C_screen_landscape, C_screen_portrait, C_always_bright, C_system_bright, C_HUD_mode, C_normal_mode, C_navi_on, C_navi_off, C_car, C_bicycle, C_watch_on, C_watch_off);

        networkcheck();
        network_check();
        network_check_run();


        System.out.println();
        return inflaterB; // 버튼을 누르는 뷰를 리턴해야 현재버튼으로 처리가 됨.
    }

    private void get_character_fragment_data(Bundle advance_bundle) {
        move_another_fragment = advance_bundle.getInt("move_another_fragment", 0); // 프래그먼트 or 액티비티 이동 구분 변수
        move_first_fragment = advance_bundle.getInt("move_first_fragment", 0); // 프래그먼트 처음 입장했는지 확인하는 변수
        character_select_fragment = advance_bundle.getInt("character_select_fragment", 0); // 캐릭터 선택 변수 (캐릭터 프래그먼트용)

        //이미 적용된 데이터를 받아와야하는 장소
        character_select_num_old_apply = advance_bundle.getInt("character_select_num_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_screen_old_apply = advance_bundle.getInt("advance_screen_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_bright_old_apply = advance_bundle.getInt("advance_bright_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_hud_mode_old_apply = advance_bundle.getInt("advance_hud_mode_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_navi_old_apply = advance_bundle.getInt("advance_navi_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_car_old_apply = advance_bundle.getInt("advance_car_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_watch_old_apply = advance_bundle.getInt("advance_watch_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수

        advance_screen_fragment = advance_bundle.getInt("advance_screen_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_bright_fragment = advance_bundle.getInt("advance_bright_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_hud_mode_fragment = advance_bundle.getInt("advance_hud_mode_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_navi_fragment = advance_bundle.getInt("advance_navi_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_car_fragment = advance_bundle.getInt("advance_car_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_watch_fragment = advance_bundle.getInt("advance_watch_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
    }


    private void check_init(ImageButton C_screen_landscape, ImageButton C_screen_portrait, ImageButton C_always_bright, ImageButton C_system_bright, ImageButton C_HUD_mode, ImageButton C_normal_mode, ImageButton C_navi_on, ImageButton C_navi_off, ImageButton C_car, ImageButton C_bicycle, ImageButton C_watch_on, ImageButton C_watch_off) {
        if (move_another_fragment == Constants.activity_to_fragment || move_first_fragment == 1) { // 1의 뜻은 처음 액티비티에서 프래그먼트로 진입한경우
            // 액티비티에서 이동함

            mainActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (advance_screen_old_apply == Constants.advance_screen_landscape) {
                        C_screen_landscape.setImageResource(R.drawable.button_p);
                        //advance_title.setScaleX(-1f);
                        // 좌우반전 모드 (hud 에 거치했을때 사용하기 용이하게 하는용도인데 쓸지말지 모르겠다.
                    } else {
                        C_screen_portrait.setImageResource(R.drawable.button_p);
                        //advance_title.setScaleX(1f);
                        // 좌우반전 모드 (hud 에 거치했을때 사용하기 용이하게 하는용도인데 쓸지말지 모르겠다.
                    }
                    advance_screen_fragment=advance_screen_old_apply;

                    if (advance_bright_old_apply == Constants.advance_bright_full) {
                        C_always_bright.setImageResource(R.drawable.button_p);
                    } else {
                        C_system_bright.setImageResource(R.drawable.button_p);
                    }
                    advance_bright_fragment = advance_screen_old_apply;

                    if (advance_hud_mode_old_apply == Constants.advance_HUD_mode) {
                        C_HUD_mode.setImageResource(R.drawable.button_p);
                    } else {
                        C_normal_mode.setImageResource(R.drawable.button_p);
                    }
                    advance_hud_mode_fragment = advance_hud_mode_old_apply;

                    if (advance_navi_old_apply == Constants.advance_navi_off) {
                        C_navi_off.setImageResource(R.drawable.button_p);
                    } else {
                        C_navi_on.setImageResource(R.drawable.button_p);
                    }
                    advance_navi_fragment = advance_navi_old_apply;

                    if (advance_car_old_apply == Constants.advance_car) {
                        C_car.setImageResource(R.drawable.button_p);
                    } else {
                        C_bicycle.setImageResource(R.drawable.button_p);
                    }
                    advance_car_fragment = advance_car_old_apply;

                    if (advance_watch_old_apply == Constants.advance_watch_off) {
                        C_watch_off.setImageResource(R.drawable.button_p);
                    } else {
                        C_watch_on.setImageResource(R.drawable.button_p);
                    }
                    advance_watch_fragment = advance_watch_old_apply;

                }
            });

            move_first_fragment = 2;
        } else {
            //프래그먼트에서 이동함

            mainActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (advance_screen_fragment == Constants.advance_screen_landscape) {
                        C_screen_landscape.setImageResource(R.drawable.button_p);
                    } else {
                        C_screen_portrait.setImageResource(R.drawable.button_p);
                    }

                    if (advance_bright_fragment == Constants.advance_bright_full) {
                        C_always_bright.setImageResource(R.drawable.button_p);
                    } else {
                        C_system_bright.setImageResource(R.drawable.button_p);
                    }

                    if (advance_hud_mode_fragment == Constants.advance_HUD_mode) {
                        C_HUD_mode.setImageResource(R.drawable.button_p);
                    } else {
                        C_normal_mode.setImageResource(R.drawable.button_p);
                    }

                    if (advance_navi_fragment == Constants.advance_navi_off) {
                        C_navi_off.setImageResource(R.drawable.button_p);
                    } else {
                        C_navi_on.setImageResource(R.drawable.button_p);
                    }

                    if (advance_car_fragment == Constants.advance_car) {
                        C_car.setImageResource(R.drawable.button_p);
                    } else {
                        C_bicycle.setImageResource(R.drawable.button_p);
                    }

                    if (advance_watch_fragment == Constants.advance_watch_off) {
                        C_watch_off.setImageResource(R.drawable.button_p);
                    } else {
                        C_watch_on.setImageResource(R.drawable.button_p);
                    }


                }
            });
        }

    }

    private void networkcheck(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final ConnectivityManager cm = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            cm.registerNetworkCallback(
                    builder.build(),
                    new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(Network network) {
                            int connectionState = -1;
                            Network nw = cm.getActiveNetwork();
                            if(nw != null) {
                                NetworkCapabilities actNw = cm.getNetworkCapabilities(nw);
                                if(actNw != null) {
                                    if(actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                        connectionState = NetworkCapabilities.TRANSPORT_WIFI;
                                    } else if(actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                        connectionState = NetworkCapabilities.TRANSPORT_CELLULAR;
                                    } else if(actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                                        connectionState = NetworkCapabilities.TRANSPORT_ETHERNET;
                                    } else if(actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                                        connectionState = NetworkCapabilities.TRANSPORT_BLUETOOTH;
                                    } else {
                                        connectionState = 9;
                                    }
                                }
                            }

                            if(connectionState >= 0) {
                                network_check=1;
                                network_check_run();
                            }
                        }

                        @Override
                        public void onLost(Network network) {
                            //네트워크 끊어짐
                            network_check=0;
                            network_check_run();


                        }

                    }
            );
        }

    }

    private void network_check_run(){
        if(network_check == 1)
        {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    C_network_lock1.setVisibility(View.INVISIBLE);
                    C_network_lock2.setVisibility(View.INVISIBLE);

                    if(advance_navi_old_apply==Constants.advance_navi_on) {
                        C_car_lock.setVisibility(View.VISIBLE);
                        C_bicycle_lock.setVisibility(View.VISIBLE);
                    }
                    //연결되었으니

                    //Toast.makeText(getActivity(), "네트워크가 연결되었습니다. 실시간 도로 기능을 사용 할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getActivity(), "네트워크가 끊어졌습니다. 실시간 도로 기능을 사용 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    C_network_lock1.setVisibility(View.VISIBLE);
                    C_network_lock2.setVisibility(View.VISIBLE);

                    C_car_lock.setVisibility(View.INVISIBLE);
                    C_bicycle_lock.setVisibility(View.INVISIBLE);
                }
            });
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
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            network_check=0;
                            network_check_run();

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


}