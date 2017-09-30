package com.example.administrator.test3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.easyaddresspicker.data.AddressPickedListener;
import com.example.easyaddresspicker.dialog.PickLocateDialog;
import com.example.easyaddresspicker.model.CityModel;
import com.example.easyaddresspicker.model.DistrictModel;
import com.example.easyaddresspicker.model.ProvinceModel;

public class MainActivity extends AppCompatActivity {

    TextView locate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locate = (TextView)findViewById(R.id.tv_address);
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PickLocateDialog(MainActivity.this, new AddressPickedListener() {
                    @Override
                    public void onAddressPicked(ProvinceModel province, CityModel city, DistrictModel district) {
                        locate.setText(province.getName() +"-" + city.getName() + "-" + district.getName());
                    }
                });
            }
        });
    }


}