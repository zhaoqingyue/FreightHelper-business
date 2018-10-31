package com.mtq.bus.freighthelper.audio;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

public class MtqRecorderUPlayer {

	private final String TAG = MtqRecorderUPlayer.class.getName();
	private MediaPlayer mediaPlayer;

	private static MtqRecorderUPlayer sInstance;

	private MtqRecorderUPlayer() {
		mediaPlayer = new MediaPlayer();
	}

	public static MtqRecorderUPlayer getInstance() {
		if (sInstance == null) {
			synchronized (MtqRecorderUPlayer.class) {
				if (sInstance == null) {
					sInstance = new MtqRecorderUPlayer();
				}
			}
		}
		return sInstance;
	}

	public void startPlay(Context context, String path) {

		if (mediaPlayer == null) {
			// 播放内存卡中音频文件
			mediaPlayer = new MediaPlayer();
		} else if (mediaPlayer.isPlaying()) {
			// 如果是正在播放的 那么就停止
			mediaPlayer.pause();
			mediaPlayer.release();
			mediaPlayer = null;
			return;
		}
		stopPlay();// 解决flush()的bug
		if (mediaPlayer == null) {
			// 播放内存卡中音频文件
			mediaPlayer = new MediaPlayer();
		}
		// 设置类型
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// 设置音源
		try {
			mediaPlayer.setDataSource(context, Uri.parse(path));
			// 准备一下(内存卡)
			mediaPlayer.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.start();
	}

	public void stopPlay() {
		if (mediaPlayer == null) {
			return;
		} else {
			try {
				// 捕获异常是为了在5.0机器上出现bug,捕获异常清理一下就行了
				mediaPlayer.stop();
				mediaPlayer.reset();
				mediaPlayer.release();
				mediaPlayer = null;

			} catch (RuntimeException e) {
				mediaPlayer.reset();
				mediaPlayer.release();
				mediaPlayer = null;
			}
		}
	}
}
