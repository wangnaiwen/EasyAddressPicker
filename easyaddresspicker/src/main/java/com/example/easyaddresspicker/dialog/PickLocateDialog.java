package com.example.easyaddresspicker.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.example.easyaddresspicker.R;
import com.example.easyaddresspicker.data.AddressPickedListener;
import com.example.easyaddresspicker.data.ResourceManager;
import com.example.easyaddresspicker.model.CityModel;
import com.example.easyaddresspicker.model.DistrictModel;
import com.example.easyaddresspicker.model.ProvinceModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/29 0029.
 */


public class PickLocateDialog {

    Activity mContext;
    NumberPicker mProvincePicker;
    NumberPicker mCityPicker;
    NumberPicker mDistrictPicker;
    List<ProvinceModel> mProvinceList;

    private AlertDialog ad;

    /**
     * 所有省集合
     */
    protected String[] mProvinceData;
    /**
     * 所有市的集合
     * key - 省 value - 市
     */
    protected Map<String, Object> mCitisDataMap = new HashMap<>();

    protected Map<String, Object> mDistrictMap = new HashMap<>();
    /**
     * 当前省的名称
     */
    protected String mCurrentProvinceName;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;

    AddressPickedListener mListener;

    public PickLocateDialog(Activity mContext, AddressPickedListener listener) {
        this.mContext = mContext;
        this.mListener = listener;
        initProvinceData();    //初始化省数据
    }

    /**
     * 初始化省市数据
     */
    private void initProvinceData() {
        new ResourceManager(mContext, new ResourceManager.Callable() {
            @Override
            public void onCall(List<ProvinceModel> list) {
                mProvinceList = list;
                // 初始化默认选中的省、市
                if (mProvinceList != null && !mProvinceList.isEmpty()) {
                    mCurrentProvinceName = mProvinceList.get(0).getName();
                    List<CityModel> cityList = mProvinceList.get(0).getChild();
                    if (cityList != null && !cityList.isEmpty()) {
                        mCurrentCityName = cityList.get(0).getName();
                    }
                }
                mProvinceData = new String[mProvinceList.size()];
                for (int i = 0; i < mProvinceList.size(); i++) {
                    // 遍历所有省的数据
                    mProvinceData[i] = mProvinceList.get(i).getName();
                    List<CityModel> cityList = mProvinceList.get(i).getChild();
                    String[] cityNames = new String[cityList.size()];
                    for (int j = 0; j < cityList.size(); j++) {
                        // 遍历省下面的所有市的数据
                        cityNames[j] = cityList.get(j).getName();

                        List<DistrictModel> districtList = cityList.get(j).getChild();
                        if (districtList != null && districtList.size() > 0) {
                            String[] districtNames = new String[districtList.size()];

                            for (int d = 0; d < districtList.size(); d++) {
                                districtNames[d] = districtList.get(d).getName();
                            }
                            mDistrictMap.put(cityList.get(j).getName(), districtNames);
                        }

                    }
                    // 省-市的数据，保存到mCitisDataMap
                    mCitisDataMap.put(mProvinceList.get(i).getName(), cityNames);
                }
                handler.sendMessage(new Message());  //初始化数据完成
            }
        });
    }

    /**
     * 加载对话框
     * */
    public AlertDialog locatePickDialog() {
        FrameLayout locatePickLayout = (FrameLayout) mContext.getLayoutInflater().inflate(R.layout.dialog_pick_locate, null);
        mProvincePicker = (NumberPicker) locatePickLayout.findViewById(R.id.province_picker);
        mCityPicker = (NumberPicker) locatePickLayout.findViewById(R.id.city_picker);
        mDistrictPicker = (NumberPicker) locatePickLayout.findViewById(R.id.district_picker);

        init();

        ad = new AlertDialog.Builder(mContext).setView(locatePickLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProvinceModel provinceModel = mProvinceList.get(mProvincePicker.getValue());
                CityModel cityModel = provinceModel.getChild().get(mCityPicker.getValue());
                DistrictModel districtModel = null;
                if (cityModel.getChild() != null) {
                    districtModel = cityModel.getChild().get(mDistrictPicker.getValue());
                }
                mListener.onAddressPicked(provinceModel, cityModel, districtModel);   //将选择的省市县地址进行回调
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();

        return ad;
    }

    /**
     * 初始化
     * */
    private void init() {
        mProvincePicker.setWrapSelectorWheel(false);
        mProvincePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                mCityPicker.setDisplayedValues(null);
                mDistrictPicker.setDisplayedValues(null);
                setCityPickerTextSize((String[]) mCitisDataMap.get(mProvinceData[newVal]));
                setDistrictPickerTextSize((String[]) (mDistrictMap.get(((String[]) mCitisDataMap.get(mProvinceData[newVal]))[0])));
                mCityPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        mDistrictPicker.setDisplayedValues(null);
                        setDistrictPickerTextSize((String[]) mDistrictMap.get(mCityPicker.getDisplayedValues()[i1]));
                    }
                });
                mCityPicker.setWrapSelectorWheel(false);
            }
        });
        mCityPicker.setWrapSelectorWheel(false);
        mCityPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mDistrictPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setCityPickerTextSize((String[]) mCitisDataMap.get(mProvinceData[0]));
        setDistrictPickerTextSize((String[]) mDistrictMap.get(((String[]) mCitisDataMap.get(mProvinceData[0]))[0]));
    }

    /**
     * 设置城市
     * */
    private void setCityPickerTextSize(String[] cities) {
        if (null != mCityPicker) {
            mCityPicker.setMinValue(0);
            mCityPicker.setMaxValue(cities.length - 1);
            mCityPicker.setDisplayedValues(cities);

            mProvincePicker.setMinValue(0);
            mProvincePicker.setMaxValue(mProvinceData.length - 1);
            mProvincePicker.setDisplayedValues(mProvinceData);
        }
    }

    /**
     * 设置区
     * */
    private void setDistrictPickerTextSize(String[] districts) {
        if (mDistrictPicker != null) {
            if (districts != null && districts.length > 0) {
                mDistrictPicker.setMinValue(0);
                mDistrictPicker.setMaxValue(districts.length > 0 ? districts.length - 1 : 0);
                mDistrictPicker.setWrapSelectorWheel(false);
                mDistrictPicker.setDisplayedValues(districts);
            } else {
                mDistrictPicker.setMinValue(0);
                mDistrictPicker.setMaxValue(0);
                mDistrictPicker.setWrapSelectorWheel(false);
                mDistrictPicker.setDisplayedValues(new String[]{"无"});
            }
        }
    }

    /**
     * 用于监听初始化省市县数据的完成，然后弹出Dialog
     * */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            locatePickDialog();
        }
    };

}
