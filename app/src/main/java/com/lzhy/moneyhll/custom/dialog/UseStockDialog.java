package com.lzhy.moneyhll.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.lzhy.moneyhll.R;

/**
 * Created by Administrator on 2016/10/31 0031.
 * 使用房车劵弹窗
 */

public class UseStockDialog extends Dialog {

    public UseStockDialog(Context context) {
        super(context);
    }

    public UseStockDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private String title;
        private Context context;
        private String positiveButtonText;
        private String negativeButtonText;
        private ListView mListView;

        public ListView getListView() {
            return mListView;
        }

        public void setListView(ListView listView) {
            mListView = listView;
        }

        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public UseStockDialog.Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public UseStockDialog.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public UseStockDialog.Builder setPositiveButton(int positiveButtonText,
                                                        DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public UseStockDialog.Builder setPositiveButton(String positiveButtonText,
                                                        DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public UseStockDialog.Builder setNegativeButton(int negativeButtonText,
                                                        DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public UseStockDialog.Builder setNegativeButton(String negativeButtonText,
                                                        DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public UseStockDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final UseStockDialog dialog = new UseStockDialog(context,
                    R.style.Dialog);
            final View layout = inflater.inflate(R.layout.dialog_use_stock, null);

            mListView = (ListView) layout.findViewById(R.id.listview);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.sure))
                        .setText(positiveButtonText);

                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.sure))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.sure_return).setVisibility(
                        View.GONE);
            }// set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.cancel))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.cancel))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.cancel).setVisibility(
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
