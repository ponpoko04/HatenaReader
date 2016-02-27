package com.takayoshi.android.hatenareader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.takayoshi.android.hatenareader.R;
import com.takayoshi.android.hatenareader.models.HatenaRss;

import java.util.List;

/**
 * ホッテントリ一覧のListView用Adaptor
 * @author takayoshi uchida
 */
public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int itemLayoutId;
    private List<HatenaRss> hotentries;

    private class HotentryViewHolder {
        TextView textTitle;
        TextView textLink;
        TextView textDescription;
    }

    public ListViewAdapter(Context context, int itemLayoutId, List<HatenaRss> hotentries) {
        super();
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.hotentries = hotentries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HotentryViewHolder holder;
        // 最初だけ View を inflate して、それを再利用する
        if (convertView == null) {
            convertView = inflater.inflate(itemLayoutId, parent, false);
            // ViewHolder を生成
            holder = new HotentryViewHolder();
            holder.textTitle = (TextView)convertView.findViewById(R.id.textTitle);
            holder.textLink = (TextView)convertView.findViewById(R.id.textLink);
            holder.textDescription = (TextView)convertView.findViewById(R.id.textDescription);
            convertView.setTag(holder);
        }
        // holder を使って再利用
        else {
            holder = (HotentryViewHolder)convertView.getTag();
        }

        holder.textTitle.setText(hotentries.get(position).title);
        holder.textLink.setText(hotentries.get(position).link);
        holder.textDescription.setText(hotentries.get(position).description);

        return  convertView;
    }

    @Override
    public int getCount() {
        // items の全要素数を返す
        return hotentries.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
