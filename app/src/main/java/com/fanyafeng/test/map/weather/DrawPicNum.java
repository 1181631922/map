package com.fanyafeng.test.map.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.fanyafeng.test.map.R;

public class DrawPicNum {

	private Context context;
	private Bitmap mBack;
	Bitmap alterBitemp;
	private Bitmap mTemp;

	public DrawPicNum(Context context) {
		this.context = context;
		mBack = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.number_bg);
		mTemp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.cityselector_gridview_card);
		alterBitemp = Bitmap.createBitmap(mBack.getWidth(), mBack.getHeight(),
				mBack.getConfig());
	}

	public Bitmap getStringPic(String str) {

		int width = 0;
		if (str.contains(".")) {
			if (str.length() > 4) {
				width = 0;
			} else {
				width = 27;
			}
		} else {
			if (str.length() > 3) {
				width = 0;
			} else {
				width = 27;
			}
		}

		alterBitemp.eraseColor(0); // 清空位图
		Canvas canvas = new Canvas(alterBitemp); // 创建画布
		canvas.drawBitmap(mBack, new Matrix(), null); // 在画布上绘图
		for (int i = 0; i < str.length(); i++) {
			Bitmap bitmap = getNumBitmap(str.charAt(i));
			canvas.drawBitmap(bitmap, width, 2, null); // 在画布指定位置绘�?
			width += bitmap.getWidth();
		}
		canvas.drawBitmap(mTemp, width, 10, null);
		return alterBitemp;
	}

	public Bitmap getIntPic(int num) {
		int width = 0;
		String strNum = Integer.toString(num);
		if (strNum.length() > 3) {
			width = 0;
		} else {
			width = 27;
		}
		alterBitemp.eraseColor(0); // 清空位图
		Canvas canvas = new Canvas(alterBitemp); // 创建画布
		canvas.drawBitmap(mBack, new Matrix(), null); // 在画布上绘图
		for (int i = 0; i < strNum.length(); i++) {
			Bitmap bitmap = getNumBitmap(strNum.charAt(i));
			canvas.drawBitmap(bitmap, width, 2, null); // 在画布指定位置绘�?
			width += bitmap.getWidth();
		}
		return alterBitemp;
	}

	/**
	 * ��ȡʹ��ͼƬ��ʾ��ʱ��
	 * 
	 * @return
	 */
	public Bitmap getFloatPic(float num) {
		int width = 0;
		String strNum = String.format("%.1f", num); // ������ֻҪһ��С���
		if (strNum.length() > 4) {
			width = 0;
		} else {
			width = 27;
		}
		alterBitemp.eraseColor(0); // ���λͼ
		Canvas canvas = new Canvas(alterBitemp); // ��������
		canvas.drawBitmap(mBack, new Matrix(), null); // �ڻ����ϻ�ͼ
		for (int i = 0; i < strNum.length(); i++) {
			Bitmap bitmap = getNumBitmap(strNum.charAt(i));
			canvas.drawBitmap(bitmap, width, 2, null); // �ڻ���ָ��λ�û�ͼ
			width += bitmap.getWidth();
			System.out.println("char = " + strNum.charAt(i) + " width = "
					+ width);
		}
		return alterBitemp;
	}

	/**
	 * ��ȡ��Ӧ������ͼƬ
	 * 
	 * @param number
	 * @return
	 */
	private Bitmap getNumBitmap(char ch) {
		Bitmap bitmap = null;
		switch (ch) {
		case '0':
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.cityselector_locate_centigrade_0);
			break;
		case '1':
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.cityselector_locate_centigrade_1);
			break;
		case '2':
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.cityselector_locate_centigrade_2);
			break;
		case '3':
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.cityselector_locate_centigrade_3);
			break;
		case '4':
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.cityselector_locate_centigrade_4);
			break;
		case '5':
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.cityselector_locate_centigrade_5);
			break;

		case '6':
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.cityselector_locate_centigrade_6);
			break;
		case '7':
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.cityselector_locate_centigrade_7);
			break;
		case '8':
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.cityselector_locate_centigrade_8);
			break;
		case '9':
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.cityselector_locate_centigrade_9);
			break;
		default:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.cityselector_locate_centigrade_0);
			break;
		}
		return bitmap;
	}
}
