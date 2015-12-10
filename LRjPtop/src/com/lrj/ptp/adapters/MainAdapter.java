package com.lrj.ptp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lrj.ptp.R;
import com.lrj.ptp.db.domain.FileInfo;
import com.lrj.ptp.db.domain.PhotographType;

import java.util.List;

/**
 * 包名：com.lrj.ptp.adapters
 * 描述：
 * User 张伟
 * QQ:326093926
 * Date 2015/12/7 0007.
 * Time 下午 2:42.
 * 修改日期：
 * 修改内容：
 */
public class MainAdapter extends BaseAdapter {
    private List<PhotographType> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public MainAdapter(Context context, List<PhotographType> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderDown viewHolderDown = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.activity_main_item, parent, false);
            viewHolderDown = new ViewHolderDown(convertView);
            convertView.setTag(viewHolderDown);
        } else {
            viewHolderDown = (ViewHolderDown) convertView.getTag();
        }
        PhotographType entity = list.get(position);
        if (entity != null) {
            viewHolderDown.textView.setText(entity.typename);
            if (entity.istrue) {
                viewHolderDown.ll_item_bg.setBackgroundResource(R.color.reds);
            } else {
                viewHolderDown.ll_item_bg.setBackgroundResource(R.color.main_yellow);
            }
        }

        return convertView;
    }

    class ViewHolderDown {
        private TextView textView;
        private LinearLayout ll_item_bg;
        public ViewHolderDown(View view) {
            textView = (TextView) view.findViewById(R.id.tv_title);
            ll_item_bg = (LinearLayout) view.findViewById(R.id.ll_item_bg);
        }
    }

}