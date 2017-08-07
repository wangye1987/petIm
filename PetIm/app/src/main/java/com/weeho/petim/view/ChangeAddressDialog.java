package com.weeho.petim.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.weeho.petim.R;
import com.weeho.petim.adapter.AbstractWheelTextAdapter;
import com.weeho.petim.modle.BaseAddressBean;
import com.weeho.petim.modle.BaseAddressBean.ResultBean;
import com.weeho.petim.modle.BaseAddressBean.ResultBean.DataBean;

/**
 * 更改封面对话框
 * 
 * @author
 *
 */
public class ChangeAddressDialog extends Dialog implements View.OnClickListener {

	private WheelView wvProvince;
	private WheelView wvCitys;
	private View lyChangeAddress;
	private View lyChangeAddressChild;
	private TextView btnSure;
	private TextView btnCancel;

	private Context context;
	private JSONObject mJsonObj;
	private String[] mProvinceDatas;
//	private List<Map<String ,id>> mProvinceDatas;
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

	private ArrayList<String> arrProvinces = new ArrayList<String>();
	private ArrayList<String> arrCitys = new ArrayList<String>();
	private AddressTextAdapter provinceAdapter;
	private AddressTextAdapter cityAdapter;

	private String strProvince = "北京市";
	private String strCity = "市辖区";
	private OnAddressCListener onAddressCListener;

	private int maxsize = 24;
	private int minsize = 14;
   private BaseAddressBean addressBean;
    private List<DataBean> listbean;

    public ChangeAddressDialog(Context context, BaseAddressBean addressBean) {
		super(context, R.style.ShareDialog);
		this.context = context;
		this.addressBean = addressBean;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changeaddress);

		wvProvince = (WheelView) findViewById(R.id.wv_address_province);
		wvCitys = (WheelView) findViewById(R.id.wv_address_city);
		lyChangeAddress = findViewById(R.id.ly_myinfo_changeaddress);
		lyChangeAddressChild = findViewById(R.id.ly_myinfo_changeaddress_child);
		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		lyChangeAddress.setOnClickListener(this);
		lyChangeAddressChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

//		initJsonData();
//		initDatas();
        JsonAddress();
		initProvinces();
		provinceAdapter = new AddressTextAdapter(context, arrProvinces, getProvinceItem(strProvince), maxsize, minsize);
		wvProvince.setVisibleItems(5);
		wvProvince.setViewAdapter(provinceAdapter);
		wvProvince.setCurrentItem(getProvinceItem(strProvince));

		initCitys(mCitisDatasMap.get(strProvince));
		cityAdapter = new AddressTextAdapter(context, arrCitys, getCityItem(strCity), maxsize, minsize);
		wvCitys.setVisibleItems(5);
		wvCitys.setViewAdapter(cityAdapter);
		wvCitys.setCurrentItem(getCityItem(strCity));

		wvProvince.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
				strProvince = currentText;
				setTextviewSize(currentText, provinceAdapter);
				String[] citys = mCitisDatasMap.get(currentText);
				initCitys(citys);
				cityAdapter = new AddressTextAdapter(context, arrCitys, 0, maxsize, minsize);
				wvCitys.setVisibleItems(5);
				wvCitys.setViewAdapter(cityAdapter);
				wvCitys.setCurrentItem(0);
			}
		});

		wvProvince.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
//				String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
//				setTextviewSize(currentText, provinceAdapter);


				String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, provinceAdapter);

				provinceAdapter = new AddressTextAdapter(context, arrProvinces, getProvinceItem(currentText), maxsize, minsize);
				wvProvince.setVisibleItems(5);
				wvProvince.setViewAdapter(provinceAdapter);
				wvProvince.setCurrentItem(getProvinceItem(currentText));
			}
		});

		wvCitys.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
				strCity = currentText;
				setTextviewSize(currentText, cityAdapter);
			}
		});

		wvCitys.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
//				String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
//				setTextviewSize(currentText, cityAdapter);


				String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, cityAdapter);

				cityAdapter = new AddressTextAdapter(context, arrCitys, getCityItem(currentText), maxsize, minsize);
				wvCitys.setVisibleItems(5);
				wvCitys.setViewAdapter(cityAdapter);
				wvCitys.setCurrentItem(getCityItem(currentText));
			}
		});
	}

	private class AddressTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected AddressTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			String text = "";
			if(list.get(index).contains("-")){
				text = list.get(index).split("-")[0];
			}else{
				text = list.get(index);
			}
			return text + "";
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(24);
			} else {
				textvew.setTextSize(14);
			}
		}
	}

	public void setAddresskListener(OnAddressCListener onAddressCListener) {
		this.onAddressCListener = onAddressCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onAddressCListener != null) {
				onAddressCListener.onClick(strProvince, strCity);
			}
		} else if (v == btnCancel) {

		} else if (v == lyChangeAddressChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();
	}

	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 *
	 */
	public interface OnAddressCListener {
		public void onClick(String province, String city);
	}

	/**
	 * 从文件中读取地址数据
	 */
	private void initJsonData() {
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is = context.getAssets().open("city.json");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1) {
				sb.append(new String(buf, 0, len, "gbk"));
			}
			is.close();
			mJsonObj = new JSONObject(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
     * 解析地址数据
     * */
	private void JsonAddress(){
            if(addressBean !=  null){
                listbean = addressBean.getResult().getData();
                int size = listbean.size();
				mProvinceDatas = new String[size];
                for(int i = 0;i < size;i++){
                    mProvinceDatas[i] = listbean.get(i).getName();
                    String  province = mProvinceDatas[i];
                    List<DataBean.CityBean> cityList =  listbean.get(i).getCity();
                    int citySize = cityList.size();
                    String[] mCitiesDatas = new String[citySize];
                    for(int j = 0;j < citySize;j++){
                        mCitiesDatas[j] = cityList.get(j).getName()+"-"+cityList.get(j).getId();
                    }
                    mCitisDatasMap.put(province, mCitiesDatas);
                }
            }
    }
	/**
	 * 解析数据
	 */
	private void initDatas() {
		try {
			JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonP = jsonArray.getJSONObject(i);
				String province = jsonP.getString("p");

				mProvinceDatas[i] = province;

				JSONArray jsonCs = null;
				try {
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("c");
				} catch (Exception e1) {
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++) {
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("n");
					mCitiesDatas[j] = city;
					JSONArray jsonAreas = null;
					try {
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("a");
					} catch (Exception e) {
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];
					for (int k = 0; k < jsonAreas.length(); k++) {
						String area = jsonAreas.getJSONObject(k).getString("s");
						mAreasDatas[k] = area;
					}
				}
				mCitisDatasMap.put(province, mCitiesDatas);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		mJsonObj = null;
	}

	/**
	 * 初始化省会
	 */
	public void initProvinces() {
		int length = mProvinceDatas.length;
		for (int i = 0; i < length; i++) {
			arrProvinces.add(mProvinceDatas[i]);
		}
	}

	/**
	 * 根据省会，生成该省会的所有城市
	 * 
	 * @param citys
	 */
	public void initCitys(String[] citys) {
		if (citys != null) {
			arrCitys.clear();
			int length = citys.length;
			for (int i = 0; i < length; i++) {
				arrCitys.add(citys[i]);
			}
		} else {
			String[] city = mCitisDatasMap.get("北京市");
			arrCitys.clear();
			int length = city.length;
			for (int i = 0; i < length; i++) {
				arrCitys.add(city[i]);
			}
		}
		if (arrCitys != null && arrCitys.size() > 0&& !arrCitys.contains(strCity)) {
			strCity = arrCitys.get(0);
		}
	}

	/**
	 * 初始化地点
	 * 
	 * @param province
	 * @param city
	 */
	public void setAddress(String province, String city) {
		if (province != null && province.length() > 0) {
			this.strProvince = province;
		}
		if (city != null && city.length() > 0) {
			this.strCity = city;
		}
	}

	/**
	 * 返回省会索引，没有就返回默认“北京”
	 * 
	 * @param province
	 * @return
	 */
	public int getProvinceItem(String province) {
		int size = arrProvinces.size();
		int provinceIndex = 0;
		boolean noprovince = true;
		for (int i = 0; i < size; i++) {
			if (province.equals(arrProvinces.get(i))) {
				noprovince = false;
				return provinceIndex;
			} else {
				provinceIndex++;
			}
		}
		if (noprovince) {
			strProvince = "北京市";
			return 22;
		}
		return provinceIndex;
	}

	/**
	 * 得到城市索引，没有返回默认“丰台”
	 * 
	 * @param city
	 * @return
	 */
	public int getCityItem(String city) {
		int size = arrCitys.size();
		int cityIndex = 0;
        String currtCity="" ;
		boolean nocity = true;
		for (int i = 0; i < size; i++) {
			System.out.println(arrCitys.get(i));
            if(arrCitys.get(i).contains("-"))
                 currtCity = arrCitys.get(i).split("-")[0];
			if (city.equals(currtCity)) {
				nocity = false;
				return cityIndex;
			} else {
				cityIndex++;
			}
		}
		if (nocity) {
			strCity = "东城区";
			return 0;
		}
		return cityIndex;
	}

}