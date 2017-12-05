package com.example.lenovo.liveapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemProgramAdapter extends BaseAdapter {

    private Context mContext;//数据集

    private String[] mDataList = new String[]{
            "CCTV-1综合","CCTV-2财经","CCTV-3综艺","CCTV-5体育","CCTV-5+体育赛事","CCTV-6电影","CCTV-7军事农业",
            "CCTV-8电视剧","CCTV-9纪录","CCTV-10科教","CCTV-12社会与法","CCTV-14少儿","CCTV-15音乐",
            "北京卫视","天津卫视","山东卫视","江苏卫视","江西卫视","浙江卫视","湖南卫视","湖北卫视",
            "安徽卫视","重庆卫视","东方卫视","深圳卫视","上海纪实","河北卫视","广东卫视","辽宁卫视",
            "黑龙江卫视","北京卫视纪实","香港频道 凤凰中文","香港频道 凤凰卫视资讯台-HD1","香港频道 凤凰卫视资讯台-HD2"
    };
    private String[] mUrlList = new String[]{
            "http://183.252.176.11//PLTV/88888888/224/3221225922/index.m3u8",
            "http://183.252.176.47//PLTV/88888888/224/3221225923/index.m3u8",
            "http://183.252.176.20//PLTV/88888888/224/3221225924/index.m3u8",
            "http://183.252.176.44//PLTV/88888888/224/3221225925/index.m3u8",
            "http://183.252.176.41//PLTV/88888888/224/3221225939/index.m3u8",
            "http://183.251.61.207/PLTV/88888888/224/3221225926/index.m3u8",
            "http://183.252.176.59//PLTV/88888888/224/3221225927/index.m3u8",
            "http://183.252.176.65//PLTV/88888888/224/3221225928/index.m3u8",
            "http://183.252.176.35//PLTV/88888888/224/3221225929/index.m3u8",
            "http://183.252.176.14//PLTV/88888888/224/3221225931/index.m3u8",
            "http://183.252.176.18//PLTV/88888888/224/3221225932/index.m3u8",
            "http://183.252.176.24//PLTV/88888888/224/3221225933/index.m3u8",
            "http://183.251.61.222//PLTV/88888888/224/3221225818/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225937/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225941/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225943/index.m3u8",
            "http://183.252.176.35//PLTV/88888888/224/3221225930/index.m3u8",
            "http://218.207.213.137//PLTV/88888888/224/3221225868/index.m3u8",
            "http://183.252.176.35//PLTV/88888888/224/3221225934/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225935/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225948/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225945/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225949/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225936/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225938/index.m3u8",
            "http://183.251.61.207/PLTV/88888888/224/3221225946/index.m3u8",
            "http://weblive.hebtv.com/live/hbws_bq/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225942/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225947/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225940/index.m3u8",
            "http://183.252.176.49//PLTV/88888888/224/3221225944/index.m3u8",
            "http://111.13.42.8/PLTV/88888888/224/3221225942/index.m3u8",
            "http://183.252.176.10/PLTV/88888888/224/3221225901/index.m3u8",
            "http://111.13.42.8/PLTV/88888888/224/3221225941/index.m3u8"
    };
    @Override
    public int getCount() {
        return mDataList.length;
    }
    public ItemProgramAdapter(Context context){
        mContext = context;
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertview == null){
            convertview = LayoutInflater.from(mContext).inflate(R.layout.item_program_adapter,null);
            holder = new ViewHolder(convertview);
            convertview.setTag(holder);
        }else{
            holder = (ViewHolder) convertview.getTag();
        }
        holder.textView.setText(mDataList[position]);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,LiveActivity.class);
                intent.putExtra("url",mUrlList[position]);
                intent.putExtra("title",mDataList[position]);
                mContext.startActivity(intent);
            }
        });
        return convertview;
    }

    private class ViewHolder {
        TextView textView;
        public ViewHolder(View view){
            textView = (TextView)view.findViewById(R.id.tv_item_program_title);
        }
    }
}
