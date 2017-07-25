package com.xdja.popwindowtext.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdja.popwindowtext.R;

import java.util.List;

/**
 * <p>Summary:</p>
 * <p>Description:</p>
 * <p>Package:com.xdja.HDSafeEMailClient.adapter</p>
 * <p>Author:yusenkui</p>
 * <p>Date:2016/12/12</p>
 * <p>Time:16:21</p>
 */

public class MailAutoCompleteAdapter extends BaseAdapter {
    private List<String> mails;
    private LayoutInflater layoutInflater;

    public MailAutoCompleteAdapter(List<String> mails, Context context) {
        this.mails = mails;
        layoutInflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public int getCount() {
        return mails != null ? mails.size() : 0;
    }

    @Override
    public String getItem(int position) {
        return mails != null ? mails.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmailEditTextAutoCompleteHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_email_auto_text, null);
            holder = new EmailEditTextAutoCompleteHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (EmailEditTextAutoCompleteHolder) convertView.getTag();
        }
        holder.update(mails.get(position));
//        holder.email.setText(mails.get(position));
        return convertView;

    }
    private class EmailEditTextAutoCompleteHolder {
        private TextView email;

        private EmailEditTextAutoCompleteHolder(View convertView) {
            this.email = (TextView) convertView.
                    findViewById(R.id.tv);
        }

        public void update(String s) {
            email.setText(s);
        }
    }
    /**
     * 更新列表数据
     *
     * @param list 要更新的数据集合
     */
    public void updateListData(List<String> list) {
        this.mails.clear();
        this.mails.addAll(list);
        notifyDataSetChanged();
    }
}
