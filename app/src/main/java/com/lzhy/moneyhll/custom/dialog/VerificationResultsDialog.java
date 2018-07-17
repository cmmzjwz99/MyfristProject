package com.lzhy.moneyhll.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzhy.moneyhll.R;

/**
 * Created by Administrator on 2016/12/27 0027.
 * 数字核销结果弹窗
 */

public class VerificationResultsDialog extends Dialog {

    public VerificationResultsDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String text;
        private String text1;
        private int resId;
        private int resId_bg;

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public int getResId_bg() {
            return resId_bg;
        }

        public void setResId_bg(int resId_bg) {
            this.resId_bg = resId_bg;
        }


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getText1() {
            return text1;
        }

        public void setText1(String text1) {
            this.text1 = text1;
        }

        private DialogInterface.OnClickListener positiveButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public VerificationResultsDialog.Builder setPositiveButton(int positiveButtonText,
                                                                   DialogInterface.OnClickListener listener) {

            this.positiveButtonClickListener = listener;
            return this;
        }

        public VerificationResultsDialog.Builder setPositiveButton(String positiveButtonText,
                                                                   DialogInterface.OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public VerificationResultsDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final VerificationResultsDialog dialog = new VerificationResultsDialog(context,
                    R.style.Dialog);
            final View layout = inflater.inflate(R.layout.dialog_verification_results, null);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            ImageView image = (ImageView) layout.findViewById(R.id.image);
            Button mButton=(Button) layout.findViewById(R.id.button_sure);
            ((TextView) layout.findViewById(R.id.text)).setText(text);
            ((TextView) layout.findViewById(R.id.text1)).setText(text1);
            image.setImageResource(resId);
            mButton.setBackgroundResource(resId_bg);

            if (positiveButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.button_sure))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                positiveButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_POSITIVE);

                            }
                        });
            }

            dialog.setContentView(layout);
            return dialog;
        }
    }
}
