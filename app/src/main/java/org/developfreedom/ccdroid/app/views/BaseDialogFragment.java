package org.developfreedom.ccdroid.app.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;

/**
 * @author Shubham Chaudhary on 10/03/16
 */
public class BaseDialogFragment extends DialogFragment {

    @CallSuper @Override public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @CallSuper @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, getTheme());
    }
}
