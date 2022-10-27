package com.example.pcr_hud;

public interface DataTransmissionListener {
    //void dataTransmissionSet(int character_select_num);

    //void fragmenttempData(int move_another_fragment, int character_select_num);

    void applyTramsmissionSet(int character_select_num_old_apply, int advance_screen_old_apply, int advance_bright_old_apply, int advance_hud_mode_old_apply, int advance_navi_old_apply, int advance_car_old_apply, int advance_watch_old_apply);

    void naviTransmissionSet(String activity_lat, String activity_lon,int character_select_num_old_apply,int advance_screen_old_apply, int advance_bright_old_apply, int advance_hud_mode_old_apply, int advance_navi_old_apply, int advance_car_old_apply, int advance_watch_old_apply);

    void navicalcleTransmissionSet(int character_select_num_old_apply,int advance_screen_old_apply, int advance_bright_old_apply, int advance_hud_mode_old_apply, int advance_navi_old_apply, int advance_car_old_apply, int advance_watch_old_apply);

    void onFragmentChange(int fragmentNum,int move_another_fragment,int character_select_fragment, int advance_screen_fragment, int advance_bright_fragment, int advance_hud_mode_fragment, int advance_navi_fragment, int advance_car_fragment, int advance_watch_fragment,
                          int character_select_num_old_apply,int  advance_screen_old_apply,int  advance_bright_old_apply,int  advance_hud_mode_old_apply,int  advance_navi_old_apply,int  advance_car_old_apply,int  advance_watch_old_apply, int move_first_fragment);

}
