package com.zhketech.client.app.sip.page;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhketech.client.app.sip.R;
import com.zhketech.client.app.sip.basepage.BaseActivity;
import com.zhketech.client.app.sip.beans.SipBean;
import com.zhketech.client.app.sip.beans.SipClient;
import com.zhketech.client.app.sip.callbacks.RequestSipSourcesThread;
import com.zhketech.client.app.sip.global.AppConfig;
import com.zhketech.client.app.sip.utils.Logutils;
import com.zhketech.client.app.sip.utils.SharedPreferencesUtils;
import com.zhketech.client.app.sip.utils.SipHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Root on 2018/6/21.
 */

public class SipInfor extends BaseActivity {

    @BindView(R.id.gridview)
    public GridView gridview;


    Context mContext;
    List<SipClient> mList = new ArrayList<>();
    List<SipBean> sipListResources = new ArrayList<>();
    List<SipClient> adapterList = new ArrayList<>();
    MyAdapter ada = null;
    int selected = -1;

    @Override
    public int intiLayout() {
        return R.layout.activity_sip_infor2;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mContext = this;

    }

    @Override
    public void initData() {

        int group_id = getIntent().getIntExtra("group_id", 0);
        if (group_id != 0) {
            RequestSipSourcesThread requestSipSourcesThread = new RequestSipSourcesThread(mContext, group_id + "", new RequestSipSourcesThread.SipListern() {
                @Override
                public void getDataListern(List<SipBean> sipList) {
                    if (sipList != null && sipList.size() > 0) {
                        sipListResources = sipList;

                        Logutils.i("sipListResources:"+sipListResources.size());
                        getHttpdata();
                    }
                }
            });
            requestSipSourcesThread.start();
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastShort("未获取到sip组的id");
                }
            });
        }
        //定时器每三秒刷新一下数据
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                getHttpdata();
            }
        };
        timer.schedule(timerTask, 0, 3000);
    }


    public void getHttpdata() {
        if (mList != null && mList.size() > 0) {
            mList.clear();
        }

        SipHttpUtils sipHttpUtils = new SipHttpUtils(AppConfig.sipServerDataUrl, new SipHttpUtils.GetHttpData() {
            @Override
            public void httpData(String result) {
                if (!TextUtils.isEmpty(result)) {
                    if (!result.contains("Execption") && !result.contains("code != 200")) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String username = jsonObject.getString("usrname");
                                String description = jsonObject.getString("description");
                                String dispname = jsonObject.getString("dispname");
                                String addr = jsonObject.getString("addr");
                                String state = jsonObject.getString("state");
                                String userAgent = jsonObject.getString("userAgent");
                                SipClient sipClient = new SipClient(username, description, dispname, addr, state, userAgent);
                                mList.add(sipClient);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (adapterList != null && adapterList.size() > 0) {
                                        adapterList.clear();
                                    }

                                    for (int i = 0; i < mList.size(); i++) {
                                        for (int j = 0; j < sipListResources.size(); j++) {
                                            if (mList.get(i).getUsrname().equals(sipListResources.get(j).getNumber())) {
                                                SipClient sipClient = new SipClient();
                                                sipClient.setState(mList.get(i).getState());
                                                sipClient.setUsrname(mList.get(i).getUsrname());
                                                adapterList.add(sipClient);
                                            }
                                        }
                                    }
                                    if (adapterList != null && adapterList.size() > 0) {

                                        if (ada != null) {
                                            ada = null;
                                        }
                                        ada = new MyAdapter(mContext);
                                        gridview.setAdapter(ada);
                                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                ada.setSeclection(position);
                                                ada.notifyDataSetChanged();
                                                Logutils.i("Position:" + position);
//
//                                                if (mList.get(position).getState().equals("1")) {
//                                                    Logutils.i("Position////:" + position);
                                                selected = position;
//                                                    Logutils.i("selected:" + selected);
//                                                }

                                            }
                                        });
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        sipHttpUtils.start();
    }


    class MyAdapter extends BaseAdapter {
        private int clickTemp = -1;
        private LayoutInflater layoutInflater;

        public MyAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return adapterList.size();
        }

        @Override
        public Object getItem(int position) {
            return adapterList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setSeclection(int position) {
            clickTemp = position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.sipstatus_item, null);
                viewHolder.item_name = (TextView) convertView.findViewById(R.id.item_name);
                viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
                viewHolder.main_layout = convertView.findViewById(R.id.sipstatus_main_layout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String native_name = AppConfig.native_sip_name;
            if (!TextUtils.isEmpty(native_name)) {
                if (adapterList.get(position).getUsrname().equals(native_name)) {
                    viewHolder.item_name.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                    viewHolder.item_name.setTextColor(0xffDC143C);
                }
            }
            if (adapterList.get(position).getState().equals("0")) {
                viewHolder.item_name.setText("哨位名:" + adapterList.get(position).getUsrname());
                viewHolder.linearLayout.setBackgroundResource(R.mipmap.btn_lixian);
            } else if (adapterList.get(position).getState().equals("1")) {
                viewHolder.item_name.setText("哨位名:" + adapterList.get(position).getUsrname());
                viewHolder.linearLayout.setBackgroundResource(R.drawable.sip_call_select_bg);
            } else if (adapterList.get(position).getState().equals("2")) {
                viewHolder.item_name.setText("哨位名:" + adapterList.get(position).getUsrname());
                viewHolder.linearLayout.setBackgroundResource(R.mipmap.btn_zhenling);
            } else if (adapterList.get(position).getState().equals("3")) {
                viewHolder.item_name.setText("哨位名:" + adapterList.get(position).getUsrname());
                viewHolder.linearLayout.setBackgroundResource(R.mipmap.btn_tonghua);
            }

            if (clickTemp == position) {
                if (adapterList.get(position).getState().equals("1") && !adapterList.get(position).getUsrname().equals(native_name))
                    viewHolder.main_layout.setBackgroundResource(R.drawable.sip_selected_bg);
            } else {
                viewHolder.main_layout.setBackgroundColor(Color.TRANSPARENT);
            }

            return convertView;
        }

        class ViewHolder {
            TextView item_name;
            LinearLayout main_layout;
            LinearLayout linearLayout;
        }
    }
}
