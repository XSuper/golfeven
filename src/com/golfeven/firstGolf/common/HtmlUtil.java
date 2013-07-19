package com.golfeven.firstGolf.common;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlUtil {

	/**
	 * 改变一段html 代码里面 img的大小
	 * @param s
	 * @param mWidth 屏幕大小
	 * @return
	 */
public static String setImageSize(String s ,int width) {

		Pattern p = Pattern
				.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");// <img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");
		Matcher m = p.matcher(s);
		
		while (m.find()) {
			String all=m.group(0);
			String src=m.group(1);
			String change = changeWidthAndHeigth(all,width);
			s=s.replace(all,change) ;
			s=s.replaceAll(src,Constant.URL_IMG_BASE+src );
		}
		return s;

	}

	public static String  changeWidthAndHeigth(String s,int mwidth){
		Pattern p = Pattern
				.compile("[Ww][Ii][Dd][Tt][Hh]\\s*[=:]\\s*['\"]?(\\d+)([Pp][Xx])?['\"]?");
		Matcher m = p.matcher(s);
		
		while (m.find()) {
			int width = Integer.parseInt(m.group(1));
			//s.replaceAll("[Ww][Ii][Dd][Tt][Hh]\\s*=\\s*['\"](\\d+)([Pp][Xx])?['\"]","width="+mwidth+"px");
			s=s.replaceAll(m.group(),m.group().replaceAll(""+width, mwidth+""));
			double b = (double)width/(double)mwidth;
			Pattern p2 = Pattern
					.compile("[Hh][Ee][Ii][Gg][Hh][Tt]\\s*[=:]\\s*['\"]?(\\d+)([Pp][Xx])?['\"]?");
			Matcher m2 = p2.matcher(s);
			while (m2.find()) {
				int height = Integer.parseInt(m2.group(1));
				double sHeight =(double)height/b;
				s=s.replaceAll(m2.group(),m2.group().replaceAll(height+"", sHeight+""));
			}
		}
		return s.replace("<img", "<img width='"+mwidth+"px'");
	}

}
