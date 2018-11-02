package com.mtq.bus.freighthelper.utils;

public class TextUtil {
	/**
	 * 去除文本输入框中输入的表情包
	 * 
	 * @Title: isEmojiCharacter
	 * @Description: TODO
	 * @param codePoint
	 * @return
	 * @return: boolean
	 */
	public static boolean isEmojiCharacter(char codePoint) {
		return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}
}
