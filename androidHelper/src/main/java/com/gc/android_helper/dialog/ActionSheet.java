package com.gc.android_helper.dialog;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.android_helper.core.BasePopupWindow;
import com.gc.android_helper.util.LengthUtil;
import com.gc.androidhelper.R;

import static com.gc.androidhelper.R.id.action_listview;

/**
 * @author 郭灿
 * @ClassName:
 * @Description: 仿IOS底部弹出选择器 基于Popwindow
 * @date 2017/11/30
 */
public class ActionSheet extends BasePopupWindow implements View.OnClickListener {

    private static ActionSheet sheet = null;

    private View actionView = null;

    private TextView cancelView;

    private LinearLayout buttonsView;

    private Context context;

    private ActionSheet(Window window) {
        super(window);
        this.context = window.getContext();
        this.actionView = View.inflate(context, R.layout.action_sheet, null);
        cancelView = (TextView) actionView.findViewById(R.id.tv_cancel);
        buttonsView = (LinearLayout) actionView.findViewById(action_listview);
    }

    @Override
    protected View getContentView() {
        return actionView;
    }

    // 获取单实例
    public static ActionSheet getInstance(Window window) {
        if (sheet == null) {
            sheet = new ActionSheet(window);
        }
        return sheet;
    }

    public void show(String[] buttons, View parentView, OnItemClickListener itemClickListener) {
        cancelView.setText("取消");
        cancelView.setOnClickListener(this);
        buttonsView.removeAllViews();
        // 动态添加按钮
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        for (int i = 1; i <= buttons.length; i++) {
            TextView textView = new TextView(this.context);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(LengthUtil.dip2px(context, 13), LengthUtil.dip2px(context, 13), LengthUtil.dip2px(context, 13), LengthUtil.dip2px(context, 13));
            textView.setText(buttons[i - 1]);
            textView.setTextColor(context.getResources().getColor(R.color.ios_text_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.font_normal));
            textView.setOnClickListener(new ButtonsOnClickListener(i, itemClickListener));
            buttonsView.addView(textView, textViewParams);
            if (i==1) {
                View line = new View(context);
                line.setBackgroundColor(context.getResources().getColor(R.color.dialog_line));
                buttonsView.addView(line, viewParams);
                textView.setBackgroundResource(R.drawable.actionsheet_btn_topcircle_bg);
            } else if(i==buttons.length){
                textView.setBackgroundResource(R.drawable.actionsheet_btn_botcircle_bg);
            }else{
                View line = new View(context);
                line.setBackgroundColor(context.getResources().getColor(R.color.dialog_line));
                buttonsView.addView(line, viewParams);
                textView.setBackgroundResource(R.drawable.actionsheet_btn_normal_bg);
            }
        }
        showPopupWindow(parentView);
    }

    // 取消按钮被点击
    @Override
    public void onClick(View v) {
        hide();
    }

    // 用户点击回调
    public interface OnItemClickListener {
        public void onClick(int position);
    }

    private class ButtonsOnClickListener implements View.OnClickListener {
        private int position;

        private OnItemClickListener itemClickListener;

        public ButtonsOnClickListener(int position, OnItemClickListener itemClickListener) {
            this.position = position;
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            hide();
            this.itemClickListener.onClick(position);
        }
    }
}
