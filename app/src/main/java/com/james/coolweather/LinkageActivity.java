package com.james.coolweather;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.james.coolweather.db.City;
import com.james.coolweather.db.County;
import com.james.coolweather.db.Province;
import com.james.coolweather.util.HttpUtil;
import com.james.coolweather.util.LogUtil;
import com.james.coolweather.util.ToastUtil;
import com.james.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LinkageActivity extends AppCompatActivity {

    private ListView leftListView;
    private ListView middleListView;
    private ListView rightListView;

    private ArrayAdapter<String> leftAdapter;
    private ArrayAdapter<String> middleAdapter;
    private ArrayAdapter<String> rightAdapter;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private List<String> leftDataList = new ArrayList<>();
    private List<String> middleDataList = new ArrayList<>();
    private List<String> rightDataList = new ArrayList<>();

    private ProgressDialog progressDialog;

    private Province selectedProvince;

    private City selectedCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkage);

        leftListView = (ListView) findViewById(R.id.left_list_view);
        middleListView = (ListView) findViewById(R.id.middle_list_view);
        rightListView = (ListView) findViewById(R.id.right_list_view);

        leftAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,leftDataList);
        middleAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,middleDataList);
        rightAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,rightDataList);


        leftListView.setAdapter(leftAdapter);
        middleListView.setAdapter(middleAdapter);
        rightListView.setAdapter(rightAdapter);

        //TODO:初始化数据
        initData();

        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedProvince = provinceList.get(position);
                queryCities();
                //下面代码放放在这里会出问题，异步线程，想一想
//                selectedCity = cityList.get(0);
//                queryCounties();
            }
        });

        middleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedCity = cityList.get(position);
                queryCounties();
            }
        });
    }



    private void queryFromServer(String address, final String type) {

        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        ToastUtil.toastShort("加载失败!");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                LogUtil.d("---chooseAreaFragment---","responseText======"+responseText);
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)){
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }


    private void queryProvinces() {
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            leftDataList.clear();
            for (Province province : provinceList) {
                leftDataList.add(province.getProvinceName());
            }
            leftAdapter.notifyDataSetChanged();
            leftListView.setSelection(0);
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    private void queryCities() {
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            middleDataList.clear();
            for (City city : cityList) {
                middleDataList.add(city.getCityName());
            }
            middleAdapter.notifyDataSetChanged();
            middleListView.setSelection(0);

            //同时保证县级联动
            selectedCity = cityList.get(0);
            queryCounties();
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    private void queryCounties() {
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            rightDataList.clear();
            for (County county : countyList) {
                rightDataList.add(county.getCountyName());
            }
            rightAdapter.notifyDataSetChanged();
            rightListView.setSelection(0);
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" +cityCode;
            queryFromServer(address, "county");
        }
    }


    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if (progressDialog==null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if (progressDialog!=null) {
            progressDialog.dismiss();
        }
    }


    private void initData(){
        queryProvinces();
        selectedProvince = provinceList.get(0);
        queryCities();
        selectedCity = cityList.get(0);
        queryCounties();
    }
}
