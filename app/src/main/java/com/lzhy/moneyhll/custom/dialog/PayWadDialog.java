package com.lzhy.moneyhll.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.custom.PasswordView;

/**
 * Created by Administrator on 2016/10/31 0031.
 * 密码弹窗
 */

public class PayWadDialog extends Dialog {

    public PayWadDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private String title;
        private Context context;
        private PasswordView pad_grid;
        private TextWatcher watcher;

        public TextWatcher getWatcher() {
            return watcher;
        }

        public void setWatcher(TextWatcher watcher) {
            this.watcher = watcher;
        }


        public PasswordView getPad_grid() {
            return pad_grid;
        }

        public void setPad_grid(PasswordView pad_grid) {
            this.pad_grid = pad_grid;
        }


        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public PayWadDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final PayWadDialog dialog = new PayWadDialog(context,
                    R.style.Dialog);
            final View layout = inflater.inflate(R.layout.dialog_input_pwd, null);
            pad_grid = (PasswordView) layout.findViewById(R.id.pswView);

            if (watcher != null) {
                pad_grid.addTextChangedListener(watcher);
            }

            pad_grid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                }
            });

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


            dialog.setContentView(layout);
            return dialog;
        }

        public String getTitle() {
            return title;
        }

    }
}
