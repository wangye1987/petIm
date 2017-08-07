package com.weeho.petim.base;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.weeho.petim.lib.utils.WeakHandler;

import java.util.ArrayList;
import java.util.List;

import com.weeho.petim.R;
import com.weeho.petim.listener.OnBackListener;
import com.weeho.petim.util.ActivityManagerUtil;
import com.weeho.petim.view.LoadingView;


/**
 *
 *
 * Describe:ACTIVITY基础类
 *
 * Date:2015-9-21
 *
 * Author:liuzhouliang
 */
public abstract class BaseActivity extends FragmentActivity implements
		View.OnClickListener {
	protected Context baseContext;
	protected Activity baseActivity;
	protected static LayoutInflater baseLayoutInflater;
	private RelativeLayout rlReload;
	// 异常页面
//	protected View loadFialView, noDataView, noNetWorkView, noOrderDataView,
//			searchNodataView, noCouponDataView, businesscardNodataView;
	protected LinearLayout llParent;
	private TextView tvNoDataContent, tvNoOrderContent;
	protected ImageView ivBack;
//	protected ImageView ivBack_close;
	protected RelativeLayout rlTitlePrent;
	protected TextView tvTitleName;
//	protected TextView menu_shoppingnum;
//	protected ImageView base_tel_main;
	// 标题栏上右侧三个区域的图标控件ivTitleMiddle
	protected ImageView ivTitleLeft, ivTitleMiddle, ivTitleRight;
	//menu显示箭头
//	protected ImageView button_hidded,iv_order,imageView_one;
	// 右侧文本
	protected TextView tvRight;
	public List<OnBackListener> onBackListenerList = new ArrayList<OnBackListener>();
	private LoadingView loadingDialog;
	private ImageView ivTitledownIcon;
	protected LinearLayout linear_back, tvTitleName_linear;
	protected ImageView ivTitleArrow;
//	protected ImageView base_shopping_mian;
	// 空地址视图
//	protected View viewNoAddress, viewNoGroupBuy, viewNoSalon, viewNoSalonMy,
//			 viewNoDrinksDemand;
	private TextView tvAddAddress, tvSalonMySee, tvAddClent;
	private TextView busAddCard;
	private Button btDrinksDemandAdd;
	Animation animation,animationbutton;
	private FatherHandler handler = new FatherHandler(this);
//	protected LinearLayout linear_menu,button_hidded_linear;
	private ObjectAnimator objectAnimator;
	protected TextView tv_left_title;

	/**
	 *
	 * Describe:初始化容器
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected abstract void onCreate();

	/**
	 *
	 * Describe:控制标题栏显示
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected abstract boolean hasTitle();

	/**
	 *
	 * Describe:加载控件
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected abstract void loadChildView();

	/**
	 *
	 * Describe:获取网络数据
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected abstract void getNetData();

	/**
	 *
	 * Describe:重新加载页面的回调
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected abstract void reloadCallback();

	/**
	 *
	 * Describe:子视图监听
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */

	protected abstract void setChildViewListener();

	/**
	 *
	 * Describe:设置标题名称
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected abstract String setTitleName();

	/**
	 *
	 * Describe:设置右侧文本
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected abstract String setRightText();

	/**
	 *
	 * Describe:设置左侧的视图资源
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected abstract int setLeftImageResource();

	/**
	 *
	 * Describe:设置中间控件视图资源
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected abstract int setMiddleImageResource();

	/**
	 *
	 * Describe:设置右侧控件视图资源
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected abstract int setRightImageResource();

	/**
	 *
	 * Describe:设置登录失效
	 *
	 * Date:2016-5-16
	 *
	 * Author:wangkui
	 */
//	protected abstract boolean setLoginFailure();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();

	}

	/**
	 *
	 * Describe:初始化
	 *
	 * Date:2015-9-21
	 *
	 * Author:liuzhouliang
	 */
	private void init() {
		ActivityManagerUtil.getActivityManager().addActivity(this);
		baseLayoutInflater = getLayoutInflater();
		baseContext = this;
		baseActivity = this;
		onCreate();
	}


	/**
	 *
	 * Describe:设置布局内容
	 *
	 * Date:2015-9-21
	 *
	 * Author:liuzhouliang
	 */
	protected void setBaseContentView(int layoutResID) {
		super.setContentView(R.layout.base_activity);
		// 获取父类外层容器
		llParent = (LinearLayout) findViewById(R.id.base_activity_rootviews);
		// 加载子类控件资源
		View childMainView = baseLayoutInflater.inflate(layoutResID, null);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		llParent.addView(childMainView, 0, ll);
		initParentView();
		setParentViewListener();
		setParentViewData();
		/**
		 * 子视图控制
		 */
		loadChildView();
		getNetData();
		setChildViewListener();
//		ActivityManager.addActivity(this);
	}




	public class FatherHandler extends WeakHandler<BaseActivity> {

		public FatherHandler(BaseActivity reference) {
			super(reference);
			// TODO Auto-generated constructor stub
		}

	}
	/**
	 *
	 * Describe:初始化父类视图
	 *
	 * Date:2015-9-21
	 *
	 * Author:liuzhouliang
	 */
	private void initParentView() {
		// TODO Auto-generated method stub
		btDrinksDemandAdd = (Button) findViewById(R.id.base_activity_no_drink_demand_add);
		tvAddClent = (TextView) findViewById(R.id.base_activity_no_client_add);
		tvSalonMySee = (TextView) findViewById(R.id.base_activity_no_salon_see);

		ivBack = (ImageView) findViewById(R.id.base_activity_title_backicon);
		rlTitlePrent = (RelativeLayout) findViewById(R.id.base_activity_title_parent);
		tvTitleName_linear = (LinearLayout) findViewById(R.id.linear_titlename);
		tvTitleName = (TextView) findViewById(R.id.base_activity_title_titlename);
		ivTitleLeft = (ImageView) findViewById(R.id.base_activity_title_right_lefticon);
		ivTitledownIcon = (ImageView) findViewById(R.id.base_activity_title_downicon);
		ivTitleMiddle = (ImageView) findViewById(R.id.base_activity_title_right_middleicon);
		ivTitleArrow = (ImageView) findViewById(R.id.base_activity_title_titleIcon);
		ivTitleRight = (ImageView) findViewById(R.id.base_activity_title_right_righticon);
		tvRight = (TextView) findViewById(R.id.base_activity_title_right_righttv);
		rlReload = (RelativeLayout) findViewById(R.id.base_activity_load_fail_reload);
		linear_back = (LinearLayout) findViewById(R.id.linear_back);
//		base_menu = (RelativeLayout) findViewById(R.id.base_menu);
		tvNoDataContent = (TextView) findViewById(R.id.base_activity_no_data_content);
		tv_left_title = (TextView) findViewById(R.id.base_activity_title_left);
//		base_tel_main = (ImageView) findViewById(R.id.base_tal_main);
//		button_hidded = (ImageView) findViewById(R.id.button_hidded);
//		iv_order = (ImageView) findViewById(R.id.iv_order);
//		imageView_one = (ImageView) findViewById(R.id.imageView_one);
//		base_shopping_mian = (ImageView) findViewById(R.id.base_shopping_mian);
//		menu_shoppingnum = (TextView)findViewById(R.id.menu_shoppingNum);
//		linear_menu = (LinearLayout) findViewById(R.id.linear_menu);
//		button_hidded_linear = (LinearLayout) findViewById(R.id.button_hidded_linear);
		/**
		 * 异常页面控件
		 */
		// 添加地址
		tvAddAddress = (TextView) findViewById(R.id.base_activity_no_address_add);
//		viewNoDrinksDemand = findViewById(R.id.base_activity_no_drink_demand);
//		NoMenu();
//		NoTelMenu();

//		objectAnimator = ObjectAnimator.ofFloat(base_tel_main,"rotation",-30,0,30);
//		objectAnimator.setInterpolator(new LinearInterpolator());
//		objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
//		objectAnimator.setDuration(1000);
//		objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
//		objectAnimator.start();
	}



	/**
	 *
	 * Describe:绑定控件资源
	 *
	 * Date:2015-9-21
	 *
	 * Author:liuzhouliang
	 */
	private void setParentViewData() {
		// TODO Auto-generated method stub
		if (setLeftImageResource() != 0) {
			ivTitleLeft.setVisibility(View.VISIBLE);
			ivTitleLeft.setImageResource(setLeftImageResource());
		}
		if (setMiddleImageResource() != 0) {
			ivTitleMiddle.setVisibility(View.VISIBLE);
			ivTitleMiddle.setImageResource(setMiddleImageResource());
		}
		if (setRightImageResource() != 0) {
			ivTitleRight.setVisibility(View.VISIBLE);
			ivTitleRight.setImageResource(setRightImageResource());
		}
		if (hasTitle()) {
			rlTitlePrent.setVisibility(View.VISIBLE);
		} else {
			rlTitlePrent.setVisibility(View.GONE);
		}
		tvTitleName.setText(setTitleName());
		if (rlTitlePrent.getBackground() != null) {
			rlTitlePrent.getBackground().setAlpha(230);
		}
		if (setRightText() != null) {
			tvRight.setVisibility(View.VISIBLE);
			tvRight.setText(setRightText());
		}
	}

	protected void setBackIcon(int id) {
		ivBack.setImageResource(id);

	}

	/**
	 *
	 * Describe:设置控件监听
	 *
	 * Date:2015-9-21
	 *
	 * Author:liuzhouliang
	 */
	private void setParentViewListener() {
		// TODO Auto-generated method stub
		ivBack.setOnClickListener(this);
		linear_back.setOnClickListener(this);
		tv_left_title.setOnClickListener(this);
		tvRight.setOnClickListener(this);
	}
	/**
	 *
	 * 事件响应
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.linear_back:
				// 返回键处理
				onBackPressed();
				break;
			case R.id.base_activity_title_backicon:
				// 返回键处理
				onBackPressed();
				break;
			case R.id.base_activity_title_left:
				// 返回键处理
				onBackPressed();
				break;

			case R.id.base_activity_load_fail_reload:
				/**
				 * 数据加载失败重新加载数据
				 */
				showReloadView();
				reloadCallback();
				break;

			case R.id.base_activity_no_data:
				/**
				 * 网络异常页面事件
				 */
				showReloadView();
				reloadCallback();
				break;

			case R.id.base_activity_no_network:
				/**
				 * 网络不给力，点击屏幕刷新
				 */
				showReloadView();
				reloadCallback();
				break;
		}
	}

	/**
	 *
	 * Describe:重新加载页面，设置内容视图
	 *
	 * Date:2015-9-21
	 *
	 * Author:liuzhouliang
	 */
	protected void setReloadContent(int layoutResID) {
		super.setContentView(R.layout.base_activity);
		// 获取父类外层容器
		llParent = (LinearLayout) findViewById(R.id.base_activity_rootviews);
		// 加载子类控件资源
		View childMainView = baseLayoutInflater.inflate(layoutResID, null);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		llParent.addView(childMainView, 1, ll);
		initParentView();
		setParentViewListener();
		setParentViewData();
		/**
		 * 子视图控制
		 */
		loadChildView();
		setChildViewListener();
	}

	/**
	 *
	 * Describe:设置加载失败页面
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected void showLoadFailView() {
		// if (llParent != null) {
		// llParent.removeViewAt(1);
		// }
		llParent.setVisibility(View.GONE);
//		loadFialView.setVisibility(View.VISIBLE);
	}

	protected void showReloadView() {
		llParent.setVisibility(View.VISIBLE);
//		loadFialView.setVisibility(View.GONE);
	}

	/**
	 *
	 * Describe:设置数据为空页面
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected void showNoDataView(String content) {
		// if (llParent != null) {
		// llParent.removeViewAt(1);
		// }
		llParent.setVisibility(View.GONE);
		tvNoDataContent.setText(content);
//		noDataView.setVisibility(View.VISIBLE);
	}

	/**
	 *
	 * Describe:无订单视图
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected void showNoOrderView(String content, int resId) {
		// if (llParent != null) {
		// llParent.removeViewAt(1);
		// }
		llParent.setVisibility(View.GONE);
		tvNoOrderContent.setText(content);
		((ImageView) findViewById(R.id.no_order_iv)).setImageResource(resId);
//		noOrderDataView.setVisibility(View.VISIBLE);
	}

	/**
	 *
	 * Describe:设置没有网络页面
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected void showNoNetWork() {

		// if (llParent != null) {
		// llParent.removeViewAt(1);
		// }
		llParent.setVisibility(View.GONE);
//		noNetWorkView.setVisibility(View.VISIBLE);
	}

	/**
	 *
	 * Describe:显示搜索没有数据页面
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected void showSearchNoData() {
		// if (llParent != null) {
		// llParent.removeViewAt(1);
		// }
		llParent.setVisibility(View.GONE);
//		searchNodataView.setVisibility(View.VISIBLE);
	}

	/**
	 *
	 * Describe:显示名片没有数据页面
	 *
	 * Date:2015-11-16
	 *
	 * Author:wk
	 */
	protected void showBusinessNoData() {
		// if (llParent != null) {
		// llParent.removeViewAt(1);
		// }
		llParent.setVisibility(View.GONE);
//		businesscardNodataView.setVisibility(View.VISIBLE);
	}




	/**
	 *
	 * Describe:隐藏返回键
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected void hideBack() {
		ivBack.setVisibility(View.GONE);
	}

	/**
	 *
	 * Describe:显示返回键
	 *
	 * Date:2015-11-2
	 *
	 * Author:liuzhouliang
	 */
	protected void showBack() {
		ivBack.setVisibility(View.VISIBLE);
	}

	/**
	 * 处理返回界面
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		InputMethodManager imm0 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm0.hideSoftInputFromWindow(ivBack.getWindowToken(), 0);
		super.onBackPressed();

		ActivityManagerUtil.getActivityManager().finishActivity(this);
		this.overridePendingTransition(R.anim.back_left_into,
				R.anim.back_right_out);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 结束Activity从堆栈中移除
		ActivityManagerUtil.getActivityManager().finishActivity(this);
//		if(objectAnimator != null) {
//			objectAnimator.cancel();
//			objectAnimator = null;
//		}
	}

	/**
	 *
	 * Describe:显示对话框
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected void showLoadingDialog() {
		if (!loadingDialog.isShowing() && baseActivity!=null) {
			loadingDialog = new LoadingView(baseActivity);
			loadingDialog.show();
		}
	}

	/**
	 *
	 * Describe:取消对话框
	 *
	 * Date:2015-9-22
	 *
	 * Author:liuzhouliang
	 */
	protected void cancleLoadingDialog() {
		if (loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	}
	/**
	 *
	 * Describe:重新设置标题
	 *
	 * Date:2016-3-11
	 *
	 * Author:wangkui
	 */
	protected void ResetTitle(String text) {
		tvTitleName.setText(text);
	}
	/**
	 *
	 * Describe:隐藏标题
	 *
	 * Date:2016-4-27
	 *
	 * Author:wangkui
	 */
	protected void Hindtitle() {
		rlTitlePrent.setVisibility(View.GONE);
	}

	//关闭当前dialog
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		if(PushReceiver.isAlert && PushReceiver.alertDialog !=null){
//			PushReceiver.alertDialog.dismiss();
//		}
	}
	private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
//	public void ShowDialog(String notice, String title) {
//		CommonDialog.makeText(baseActivity, title, notice, new CommonDialog.OnDialogListener() {
//			@Override
//			public void onResult(int result, CommonDialog commonDialog,
//								 String tel) {
//				// TODO Auto-generated method stub
//				if (CommonDialog.OnDialogListener.LEFT == result ) {
//					if(Build.VERSION.SDK_INT >= 23){
//						if (ContextCompat.checkSelfPermission(baseActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
//						{
//							ActivityCompat.requestPermissions(baseActivity,new String[]{Manifest.permission.CALL_PHONE},
//									MY_PERMISSIONS_REQUEST_CALL_PHONE);
//						} else{
//							callPhone();
//						}
//					}else{
//						callPhone();
//					}
//					//6.0权限处理
//					CommonDialog.Dissmess();
//				}  else {
//					CommonDialog.Dissmess();
//				}
//			}
//		}).showDialog();
//	}
//	private void callPhone() {
//		// TODO Auto-generated method stub
//		Uri data = Uri.parse("tel:" + MyApplication.getInstance().getServiceline());
//		Intent intents = new Intent(Intent.ACTION_CALL, data);
//		startActivity(intents);
//	}
	/**
	 * 跳转设置手机
	 *
	 * @param context 全局信息接口
	 */
	public static void gotoLocServiceSettings(Context context) {
		final Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra("packageName","com.weeho.petim");
		try {
			context.startActivity(intent);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
//	@Override
//	public void onRequestPermissionsResult(int requestCode,String[] permissions,  int[] grantResults) {
//		if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
//		{
//			if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0)
//			{
//				callPhone();
//			} else
//			{
//				// Permission Denied
//				ToastUtil.showToast(baseActivity, "请去设置里面开启拨打电话权限");
//			}
////			return;
//		}
////		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//	}

//	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		super.onWindowFocusChanged(hasFocus);
//		if (hasFocus && Build.VERSION.SDK_INT>=21) {
//			/**
//			 * 4.4版本才有效果
//			 * */
//			View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//		}
//	}
}
