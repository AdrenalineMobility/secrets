package io.adrenaline.secrets.models;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.views.GroupListEntryRelativeLayout;

/**
 * Created by stang6 on 11/1/13.
 */
public class SecretGroupAdapter extends BaseAdapter {
    private ArrayList<SecretGroupModel> mSecretGroups;

    public SecretGroupAdapter() {
        // FIXME
        mSecretGroups = new ArrayList<SecretGroupModel>();
        for (int i = 0; i < 5; ++i) {
            mSecretGroups.add(SecretGroupModel.create());
        }
    }

    @Override
    public int getCount() {
        return mSecretGroups.size();
    }

    @Override
    public SecretGroupModel getItem(int position) {
        return mSecretGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupListEntryRelativeLayout groupEntry;
        if (convertView != null && convertView instanceof GroupListEntryRelativeLayout) {
            groupEntry = (GroupListEntryRelativeLayout) convertView;
        } else {
            LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
            groupEntry = (GroupListEntryRelativeLayout) inflater.inflate(R.layout.group_list_entry, parent, false);
        }

        groupEntry.update(getItem(position));
        return groupEntry;
    }
}
