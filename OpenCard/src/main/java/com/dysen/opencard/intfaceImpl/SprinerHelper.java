package com.dysen.opencard.intfaceImpl;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dysen.commom_library.utils.LogUtils;
import com.dysen.opencard.itfcace.SprinerInterface;

/**
 * 封装通用Spinner  create by hutian 2018/03/13
 */

public class SprinerHelper  {
	private  static SprinerInterface mSprinerInterface;

	public static void setSprinerCallBack(SprinerInterface sprinerInterface){
		mSprinerInterface=sprinerInterface;

	}
	public static void showCommonSpinner(final Context aty, final String[] type, final Spinner spinner, final SprinerInterface onItemClick) {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(aty, android.R.layout
				.simple_spinner_item, type);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
			                           int pos, long id) {
				//LogUtils.i("spinnerID="+spinner.getId()+"父ID"+parent.getId()+"=子ID="+view.getId()+parent.getTag());
				onItemClick.itemSelectClick(spinner,type[pos],pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});
	}
}
