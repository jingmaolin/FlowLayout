package mao.fun;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyViewGroup extends ViewGroup {
    private static final String TAG = "maoTest";
    private List<List<View>> mAllView;//所有控件，子元素为每一行上的控件
    private List<View> mLineView;//每一行上的控件
    private List<Integer> mHeight;//每一行的高度

    public MyViewGroup(Context context) {
        this(context, null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        mAllView = new ArrayList<>();
        mLineView = new ArrayList<>();
        mHeight = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mAllView.clear();
        mLineView.clear();
        mHeight.clear();

        //布局的测量大小与模式
        int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int parentHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        Log.d(TAG, "onMeasure: " + "parentWidth =" + parentWidthSize + ";" + "parentHeight=" + parentHeightSize);

        //所有子view最终占据的宽度与高度
        int layoutLineWidth = 0;
        int layoutLineHeight = 0;

        //每一行子view的宽度与高度
        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            if (childWidth + lp.leftMargin + lp.rightMargin > parentWidthSize - getPaddingRight() - getPaddingLeft()) {
                if (child.getClass() == TextView.class) {
                    Log.d("maolin", "onMeasure: " + "father and children");
                    ((TextView) child).setMaxWidth(parentWidthSize - getPaddingRight() - getPaddingLeft() - lp.leftMargin - lp.rightMargin);
                } else {
                    continue;
                }
            }

            //需要换行时
            if (lineWidth + childWidth + lp.leftMargin + lp.rightMargin > parentWidthSize - getPaddingLeft() - getPaddingRight()) {
                mAllView.add(mLineView);
                mHeight.add(lineHeight);

                layoutLineWidth = Math.max(layoutLineWidth, lineWidth);
                layoutLineHeight += lineHeight;

                mLineView = new ArrayList<>();
                lineWidth = 0;
                lineHeight = 0;
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            mLineView.add(child);
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);

            //最后一行
            if (i == childCount - 1) {
                mAllView.add(mLineView);
                mHeight.add(lineHeight);

                layoutLineHeight += lineHeight;
                layoutLineWidth = Math.max(layoutLineWidth, lineWidth);
            }
        }

        parentWidthSize = parentWidthMode == MeasureSpec.EXACTLY ? parentWidthSize : layoutLineWidth + getPaddingRight() + getPaddingLeft();
        parentHeightSize = parentHeightMode == MeasureSpec.EXACTLY ? parentHeightSize : layoutLineHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(parentWidthSize, parentHeightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lineSize = mAllView.size();
        //当前的横纵位置
        int currentX = getPaddingLeft();
        int currentY = getPaddingTop();

        for (int i = 0; i < lineSize; i++) {
            List<View> lineView = mAllView.get(i);

            int childSize = lineView.size();
            for (int j = 0; j < childSize; j++) {
                View child = lineView.get(j);
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int left = currentX + lp.leftMargin;
                int right = left + child.getMeasuredWidth();
                int top = currentY + lp.topMargin;
                int bottom = top + child.getMeasuredHeight();
                child.layout(left, top, right, bottom);

                currentX += lp.leftMargin + lp.rightMargin + child.getMeasuredWidth();
            }
            currentY += mHeight.get(i);
            currentX = getPaddingLeft();
        }
    }

    /**
     * 调用在这个布局中的子元素对象的getLayoutParams()方法，会得到一个MarginLayoutParams对象
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
