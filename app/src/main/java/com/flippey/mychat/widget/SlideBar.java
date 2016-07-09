package com.flippey.mychat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.adapter.ContactAdapter;
import com.hyphenate.util.DensityUtil;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/8 20:52
 */
public class SlideBar extends View {

    private Paint mPaint;
    private int mMeasuredHeight;
    private int mMeasuredWidth;

    private static final  String[] sections = {
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S",
            "T","U","V","W","X","Y","Z"
    };
    private int mHeight;
    private TextView mFloaToast;
    private ListView mLv;

    public SlideBar(Context context) {
        this(context,null);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.parseColor("#9c9c9c"));
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 10));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredHeight = getMeasuredHeight()-getPaddingBottom();
        mMeasuredWidth = getMeasuredWidth();
        mHeight = (int)((mMeasuredHeight+0.5f) / sections.length);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < sections.length; i++) {
            canvas.drawText(sections[i], mMeasuredWidth / 2, mHeight * (i + 1), mPaint);
        }
    }

    //添加触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //设置背景
                setBackgroundResource(R.drawable.slide_bar_bg);
                //定位lv到指定到指定到位置
            case MotionEvent.ACTION_MOVE:
                //显示tv，break穿透
                showHeaderAndScroll(event);
                //不断定位
                break;
            case MotionEvent.ACTION_UP:
                //修改背景,设置背景透明
                setBackgroundColor(Color.TRANSPARENT);
                //tv隐藏
                if (mFloaToast != null) {
                    mFloaToast.setVisibility(GONE);
                }
                break;
        }
        return true;
    }

    private void showHeaderAndScroll(MotionEvent event) {
        //获取父容器
        if (mFloaToast == null) {
            ViewGroup parent = (ViewGroup) getParent();
            mFloaToast = (TextView) parent.findViewById(R.id.contact_tv);
            mLv = (ListView) parent.findViewById(R.id.contact_lv);
        }
        mFloaToast.setVisibility(VISIBLE);
        String floatHeader = getFloatHeader(event);
        mFloaToast.setText(floatHeader);
        //定位listview
        ListAdapter adapter = mLv.getAdapter();
        if (!(adapter instanceof SectionIndexer)) {
            return;
        }
        ContactAdapter contactAdapter = (ContactAdapter) adapter;
        String[] sections = contactAdapter.getSections();
        int sectionIndex = -1;
        for (int i = 0; i < sections.length; i++) {
            if (floatHeader.equals(sections[i])) {
                sectionIndex = i;
                break;
            }
        }
        if (sectionIndex != -1) {
            int positionForSection = contactAdapter.getPositionForSection(sectionIndex);
            mLv.setSelection(positionForSection);
        }
    }

    private String getFloatHeader(MotionEvent event) {
        float y = event.getY();
        int index = (int) (y / mHeight);
        if (index < 0) {
            index = 0;
        }else if(index > sections.length -1){
            index = sections.length - 1;
        }
        return sections[index];
    }

    private int getPosition(MotionEvent event) {
        int currentHeight = (int) event.getY();
        int position = currentHeight / mHeight;
        if (position < 0) {
            position = 0;
        }else if (position > sections.length-1){
            position = sections.length - 1;
        }
        return position;
    }
}
