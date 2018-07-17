package com.lzhy.moneyhll.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lzhy.moneyhll.R;

/**
 * Created by Administrator on 2016/10/31 0031.、
 * 退货申请
 */

public class ReturnedGoodsDialog extends Dialog {

    public ReturnedGoodsDialog(Context context) {
        super(context);
    }

    public ReturnedGoodsDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private String title;
        private Context context;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String reason;

        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
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

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public ReturnedGoodsDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ReturnedGoodsDialog dialog = new ReturnedGoodsDialog(context,
                    R.style.Dialog);
            final View layout = inflater.inflate(R.layout.dialog_returned_goods, null);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.sure_return))
                        .setText(positiveButtonText);

                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.sure_return))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    EditText mEditText = (EditText) layout.findViewById(R.id.return_reason);
                                    setReason(mEditText.getText().toString());
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);

                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.sure_return).setVisibility(
                        View.GONE);
            }

            dialog.setContentView(layout);
            return dialog;
        }

        public String getTitle() {
            return title;
        }

    }
}
