package com.example.easyaddresspicker.data;

import com.example.easyaddresspicker.model.CityModel;
import com.example.easyaddresspicker.model.DistrictModel;
import com.example.easyaddresspicker.model.ProvinceModel;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public interface AddressPickedListener {
    void onAddressPicked(ProvinceModel province, CityModel city, DistrictModel district);
}