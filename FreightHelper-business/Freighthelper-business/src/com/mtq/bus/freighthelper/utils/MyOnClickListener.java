package com.mtq.bus.freighthelper.utils;

import android.view.View;

/**
 * 
 * @author weift
 *
 */
public abstract class MyOnClickListener implements View.OnClickListener {

    protected long clickTime = 0L;
    //设置按键的点击间隔为600毫秒
    public long delay = 600;

    public void onClick(View v){
//        v.setSoundEffectsEnabled(false);
        if(System.currentTimeMillis() - clickTime>delay){
            clickTime=System.currentTimeMillis();
            playSound();
            onMyClick(v);
        }
    }

    private void playSound(){
        try{

//            GameSound.Play_Sound_Pool("button");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public abstract void onMyClick(View v);
}
