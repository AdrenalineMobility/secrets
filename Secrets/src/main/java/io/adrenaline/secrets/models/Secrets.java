package io.adrenaline.secrets.models;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;

import io.adrenaline.secrets.R;
import io.adrenaline.secrets.views.GroupListEntryRelativeLayout;

/**
 * Created by stang6 on 11/4/13.
 */
public class Secrets {

    private Secrets() {

    }

    /*package*/ static void generateRandomData() {
        for (int i = 0; i < 5; ++i) {
            Secrets.addSecretGroup(SecretGroupModel.createRandomData());
        }
    }

    public static interface SecretsListener {
        public void onSecretsChanged();
    }

    private static ArrayList<SecretGroupModel> sSecrets = new ArrayList<SecretGroupModel>();

    private static SecretsListener sListener;

    public static void setListener(SecretsListener l) {
        sListener = l;
    }

    public static void setSecretGroups(Collection<SecretGroupModel> collection) {
        sSecrets = new ArrayList<SecretGroupModel>(collection);
        if (sListener != null) {
            sListener.onSecretsChanged();
        }
    }

    public static void addSecretGroup(SecretGroupModel group) {
        sSecrets.add(group);
        if (sListener != null) {
            sListener.onSecretsChanged();
        }
    }

    public static SecretGroupModel getSecretGroup(int index) {
        if (index < sSecrets.size()) {
            return sSecrets.get(index);
        }
        return null;
    }

    public static int indexOfSecretGroup(SecretGroupModel group) {
        return sSecrets.indexOf(group);
    }

    public static int numOfGroups() {
        return sSecrets.size();
    }

    private static SecretsAdapter sAdapter;
    public static BaseAdapter getAdapter() {
        if (sAdapter == null) {
            sAdapter = new SecretsAdapter();
            Secrets.setListener(sAdapter);
        }

        return sAdapter;
    }

    private static class SecretsAdapter extends BaseAdapter implements Secrets.SecretsListener {

        private SecretsAdapter() {
            if (Secrets.numOfGroups() == 0) {
                Secrets.generateRandomData();
            }
        }

        @Override
        public int getCount() {
            return Secrets.numOfGroups();
        }

        @Override
        public SecretGroupModel getItem(int position) {
            return Secrets.getSecretGroup(position);
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

        @Override
        public void onSecretsChanged() {
            notifyDataSetChanged();
        }
    }
}
