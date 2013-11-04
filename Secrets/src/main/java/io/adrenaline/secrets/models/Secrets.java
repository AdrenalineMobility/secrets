package io.adrenaline.secrets.models;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by stang6 on 11/4/13.
 */
public class Secrets {

    /*package*/ static void generateRandomData() {
        for (int i = 0; i < 5; ++i) {
            Secrets.addSecretGroup(SecretGroupModel.create());
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

    public static int numOfGroups() {
        return sSecrets.size();
    }
}
