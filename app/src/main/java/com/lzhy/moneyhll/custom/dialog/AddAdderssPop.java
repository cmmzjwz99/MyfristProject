package com.lzhy.moneyhll.custom.dialog;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.me.addressmanagement.model.CityModel;
import com.lzhy.moneyhll.me.addressmanagement.model.DistrictModel;
import com.lzhy.moneyhll.me.addressmanagement.model.ProvinceModel;
import com.lzhy.moneyhll.me.addressmanagement.OnWheelChangedListener;
import com.lzhy.moneyhll.me.addressmanagement.WheelView;
import com.lzhy.moneyhll.me.addressmanagement.adapters.ArrayWheelAdapter;
import com.lzhy.moneyhll.viewhelper.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Artist on 2016/11/5.
 * 省市区三级联动
 */
public class AddAdderssPop extends BasePopupWindow implements View.OnClickListener, OnWheelChangedListener {

    /**
     *
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    public String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    public String mCurrentCityName;
    /**
     * 当前区的名称
     */
    public String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    private WheelView provinces, city, county;
    private TextView quit;
    private Context mContext;
    private TextView text;

    public AddAdderssPop(Context _context) {
        super(_context, R.layout.pop_add_address);
        this.mContext = _context;
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        initProvinceDatas();
        initListView(view);
        setAnimationStyle();
    }

    public void setOnlistener(View.OnClickListener listener) {
        text.setOnClickListener(listener);
    }

    private void initListView(View view) {
        quit = (TextView) view.findViewById(R.id.close);
        provinces = (WheelView) view.findViewById(R.id.provinces);
        city = (WheelView) view.findViewById(R.id.city);
        county = (WheelView) view.findViewById(R.id.county);
        text = (TextView) view.findViewById(R.id.text);
        // 添加change事件
        provinces.addChangingListener(this);
        // 添加change事件
        city.addChangingListener(this);
        // 添加change事件
        county.addChangingListener(this);
        quit.setOnClickListener(this);

        provinces.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
        provinces.setVisibleItems(3);
        city.setVisibleItems(3);
        county.setVisibleItems(3);
        updateCities();
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = city.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        county.setViewAdapter(new ArrayWheelAdapter<String>(mContext, areas));
        mCurrentDistrictName = areas[0];
        county.setCurrentItem(0);

    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = provinces.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        city.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
        city.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 从底部部进入的动画效果，需要继续完善
     */
    private void setAnimationStyle() {

        popupWindow.setAnimationStyle(R.anim.slide_button);

    }

    /**
     *
     */
    public void showAtLocation(View parent) {
        showAtBottom(parent);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == provinces) {
            updateCities();
        } else if (wheel == city) {
            updateAreas();
        } else if (wheel == county) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = mContext.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

}
