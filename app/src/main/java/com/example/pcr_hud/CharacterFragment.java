package com.example.pcr_hud;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CharacterFragment extends Fragment {


    MainActivity mainActivity; //(액티비티에서 이동하기) 주 가되는 메인액티비티 선언

    private DataTransmissionListener dataTransmissionSetListener; // 값 전송용 변수 선언

    private ImageButton[] tv = new ImageButton[83]; // 캐릭터 버튼 리스트 선언

    int character_select_fragment; // 캐릭터 선택용 변수


    //변경해서 변한 값 저장 변수
    //액티비티에서 값 받아오는 변수
    //기존 값 저장 변수
    //이부분을 유저 프리패쳐로 설정해야 저장된 초기값을 불러 올 수 있다.
    private int character_select_num_old_apply;

    private int move_another_fragment; // 액티비티 -> 프래그먼트 or 프래그먼트 -> 프래그먼트 구별용 변수
    private int move_first_fragment = 0;

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

    private FrameLayout character_title;


    int getId; // R.id.블라블라.. 저장용 변수


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
            // 액티비티에서 프래그먼트로 값 받아오기
            Bundle character_bundle = getArguments();

            // 하나 더챙겨가야해.


            if (character_bundle != null) {

                get_activity_data(character_bundle);
                get_advance_fragment_data(character_bundle);

                if(move_first_fragment==2){
                    get_advance_fragment_old_data(character_bundle);
                }

            }
            System.out.println("액티비티에서 불러온 값 확인 : " + character_select_num_old_apply);
            System.out.println("액티비티에서 불러온 값 확인 : " + move_another_fragment);
            System.out.println("액티비티에서 불러온 값 확인 : " + character_select_num_old_apply);
        }


        //프래그먼트 이동 버튼
        View inflaterA = inflater.inflate(R.layout.fragment_character, container, false); // inflater 때문에 선언을 추가해야함


        /*
        // 좌우반전 모드 (hud 에 거치했을때 사용하기 용이하게 하는용도인데 쓸지말지 모르겠다.
        character_title = (FrameLayout)  inflaterA.findViewById(R.id.character_title);

        if(advance_hud_mode_old_apply==Constants.advance_HUD_mode){
            character_title.setScaleX(-1f);
        }
        else{
            character_title.setScaleX(11f);
        }\

         */

        ImageButton C_advance_move = (ImageButton) inflaterA.findViewById(R.id.btn_advance_fragment);

        C_advance_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //A 프래그먼트에서 임시로 설정한 temp 값을 메인 액티비티로 보냄
                move_another_fragment = Constants.fragment_to_fragment; // 프래그먼트에서 액티비티로이동을 증명하기 위한 변수
                if (move_first_fragment != 2) {
                    move_first_fragment = 1;
                }
                //dataTransmissionSetListener.fragmenttempData(move_another_fragment,character_select_fragment); // 여기에 변수를 넣어서 이동하는 과정을 쓸꺼야.

                mainActivity.onFragmentChange(Constants.advance_fragment, move_another_fragment, character_select_fragment, advance_screen_fragment, advance_bright_fragment, advance_hud_mode_fragment, advance_navi_fragment, advance_car_fragment, advance_watch_fragment,
                        character_select_num_old_apply, advance_screen_old_apply, advance_bright_old_apply, advance_hud_mode_old_apply, advance_navi_old_apply, advance_car_old_apply, advance_watch_old_apply, move_first_fragment); // (액티비티에서 이동하기) 숫자 1은 추후 변수처리

            }
        });


        ImageButton C_killexitbutton = (ImageButton) inflaterA.findViewById(R.id.cancel_exit_button);

        //프래그먼트 종료 버튼
        C_killexitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //move_another_fragment=Constants.activity_to_fragment; // 프래그먼트에서 액티비티로이동을 증명하기 위한 변수
                dataTransmissionSetListener.applyTramsmissionSet(character_select_num_old_apply, advance_screen_old_apply, advance_bright_old_apply, advance_hud_mode_old_apply, advance_navi_old_apply, advance_car_old_apply, advance_watch_old_apply); // 현재 적용값은 하나다.
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().remove(CharacterFragment.this).commit();
                manager.popBackStack();
            }
        });

        ImageButton C_okbutton = (ImageButton) inflaterA.findViewById(R.id.ok_exit_button);

        //프래그먼트 종료 버튼
        C_okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //move_another_fragment=Constants.activity_to_fragment; // 프래그먼트에서 액티비티로이동을 증명하기 위한 변수
                character_select_num_old_apply = character_select_fragment;
                if (move_first_fragment == 2) {


                advance_screen_old_apply = advance_screen_fragment; // 화면 모드 변수
                advance_bright_old_apply = advance_bright_fragment; // 밝기 조정 변수
                advance_hud_mode_old_apply = advance_hud_mode_fragment; // hud 모드 변수
                advance_navi_old_apply = advance_navi_fragment; // 네비 모드 변수
                advance_car_old_apply = advance_car_fragment; // 자가용 모드 변수
                advance_watch_old_apply = advance_watch_fragment; // 시계 모드 변수
                }

                dataTransmissionSetListener.applyTramsmissionSet(character_select_num_old_apply, advance_screen_old_apply, advance_bright_old_apply, advance_hud_mode_old_apply, advance_navi_old_apply, advance_car_old_apply, advance_watch_old_apply); // 현재 적용값은 하나다.
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().remove(CharacterFragment.this).commit();
                manager.popBackStack();
            }
        });

        //캐릭터 리스트 배열값으로 선언 및 id 연결 줄이기
        for (int i = 0; i < tv.length; i++) { // 여기서 i 는 배열 범위 초기화임
            getId = getResources().getIdentifier("character_" + i, "id", getActivity().getPackageName());
            tv[i] = inflaterA.findViewById(getId);
        }


        for (int i = 0; i < tv.length; i++) { // 변수값으로 클릭 리스너 줄이기
            tv[i].setOnClickListener(btnListener);
        }


        // 이 기능을 이용해서 프래그먼트로 온건지 아닌지를 변수로 체인지 시키자. 이게낫겠다.
        if (move_another_fragment == Constants.activity_to_fragment) {
            check_chara(); // 액티비티에서 프래그먼트로 진입하면서 설정값 불러오기
        } else {
            check_chara_fragment(); // 프래그먼트에서 프래그먼트로 진입하면서 설정값 불러오기 (구현해야함)
        }

        /*
        //플러스 버튼

        Button PlusButton = (Button) inflaterA.findViewById(R.id.UpButton);

        //+ 버튼
        PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus=1;
                minus=0;
                dataTransmissionSetListener.dataTransmissionSet(plus,minus);
            }
        });

        Button MinusButton = (Button) inflaterA.findViewById(R.id.DownButton);

        //- 버튼
        MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus=0;
                minus=1;
                dataTransmissionSetListener.dataTransmissionSet(plus,minus);
            }
        });

         */


        return inflaterA; // 버튼을 누르는 뷰를 리턴해야 현재버튼으로 처리가 됨.


    }

    //캐릭터 클릭 리스너 실행
    private View.OnClickListener btnListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            buttonclean();
            for (int i = 0; i < tv.length; i++) {
                if (v.getId() == tv[i].getId()) {
                    character_select_fragment = i; // i 의 값은 임시 데이터. ok 버튼을 눌렀을때 비로소 적용된다.
                    tv[i].setImageResource(R.drawable.button_p);
                    //dataTransmissionSetListener.dataTransmissionSet(character_select_num); 불필요
                }

            }
        }
    };

    private void get_activity_data(Bundle character_bundle) {
        //이미 적용된 데이터를 받아와야하는 장소 (쉐어드 프리퍼런스용)
        character_select_num_old_apply = character_bundle.getInt("main_chara_select", 0); // // 이건 액티비티에서 받아오는 변수고
        advance_screen_old_apply = character_bundle.getInt("main_screen_select", 0); // 이건 액티비티에서 받아오는 변수
        advance_bright_old_apply = character_bundle.getInt("main_bright_select", 0); // 이건 액티비티에서 받아오는 변수
        advance_hud_mode_old_apply = character_bundle.getInt("main_hud_select", 0); // 이건 액티비티에서 받아오는 변수
        advance_navi_old_apply = character_bundle.getInt("main_navi_select", 0); // 이건 액티비티에서 받아오는 변수
        advance_car_old_apply = character_bundle.getInt("main_car_select", 0); // 이건 액티비티에서 받아오는 변수
        advance_watch_old_apply = character_bundle.getInt("main_watch_select", 0); // 이건 액티비티에서 받아오는 변수
    }

    private void get_advance_fragment_data(Bundle character_bundle) {
        character_select_num_old_apply = character_bundle.getInt("character_select_num_old_apply", character_select_num_old_apply); // // 이건 액티비티에서 받아오는 변수고
        move_another_fragment = character_bundle.getInt("move_another_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        move_first_fragment = character_bundle.getInt("move_first_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        character_select_fragment = character_bundle.getInt("character_select_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_screen_fragment = character_bundle.getInt("advance_screen_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_bright_fragment = character_bundle.getInt("advance_bright_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_hud_mode_fragment = character_bundle.getInt("advance_hud_mode_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_navi_fragment = character_bundle.getInt("advance_navi_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_car_fragment = character_bundle.getInt("advance_car_fragment", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_watch_fragment = character_bundle.getInt("advance_watch_fragment", 0); // 이건 프래그먼트에서 받아오는 변수


    }

    private void get_advance_fragment_old_data(Bundle character_bundle){
        character_select_num_old_apply = character_bundle.getInt("character_select_num_old_apply", character_select_num_old_apply); // 이건 프래그먼트에서 받아오는 변수
        advance_screen_old_apply = character_bundle.getInt("advance_screen_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_bright_old_apply = character_bundle.getInt("advance_bright_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_hud_mode_old_apply = character_bundle.getInt("advance_hud_mode_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_navi_old_apply = character_bundle.getInt("advance_navi_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_car_old_apply = character_bundle.getInt("advance_car_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
        advance_watch_old_apply = character_bundle.getInt("advance_watch_old_apply", 0); // 이건 프래그먼트에서 받아오는 변수
    }

    public void check_chara_fragment() { // 프래그먼트 들어왔을때 적용용
        // 근데 프래그먼트 와리가리했을땐 어케하려는거냐
        tv[character_select_fragment].setImageResource(R.drawable.button_p);
    }


    public void check_chara() { // 액티비티 -> 프래그먼트 들어왔을때 적용용
        // 근데 프래그먼트 와리가리했을땐 어케하려는거냐
        tv[character_select_num_old_apply].setImageResource(R.drawable.button_p);
        character_select_fragment = character_select_num_old_apply; // 아무것도 안건드렸을때 보내는 값 없어서 오류생길수있음.
        // 추후 액티비티 -> 프래그먼트 넘어왔을때 다른것도 등록 시켜야한다.
    }

    public void buttonclean() { // 이전 클릭 버튼 삭제용
        tv[character_select_fragment].setImageResource(R.drawable.btn_character);
        tv[character_select_num_old_apply].setImageResource(R.drawable.btn_character);
    }
}