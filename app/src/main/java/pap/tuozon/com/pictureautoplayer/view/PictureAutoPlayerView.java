package pap.tuozon.com.pictureautoplayer.view;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pap.tuozon.com.pictureautoplayer.R;

/**
 * Created by Jun on 2015/2/3.
 */
public class PictureAutoPlayerView extends RelativeLayout implements View.OnClickListener {


    private static final String TAG = "PictureAutoPlayerView";

    // 自动播放时间间隔
    private static final long TIME_GAP = 5000;

    private ViewPager viewPager;

    private boolean isFinishInflate = false;
    private boolean isInit = false;

    private List<View> mViews = null;

    private OnPageClickListener mOnPageClickListener = null;

//    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public PictureAutoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "#PictureAutoPlayerView(Context context, AttributeSet attrs)");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "#onFinishInflate");
        isFinishInflate = true;
        if (mViews != null && !isInit) {
            init();
        }
    }

//    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
//        mOnPageChangeListener = onPageChangeListener;
//    }

    private void init() {

        if (mViews == null) {
            return;
        }

        if (isInit) {
            return;
        }

        isInit = true;
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyPageAdapter(mViews));
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                RadioGroup tipsLayout = (RadioGroup) findViewById(R.id.page_cusor_radioGroup);
                for (int i = tipsLayout.getChildCount() - 1; i >= 0; i--) {
                    RadioButton rb = (RadioButton)tipsLayout.getChildAt(i);
                    rb.setChecked(false);
                }
                RadioButton rb = (RadioButton)tipsLayout.getChildAt(position);
                rb.setChecked(true);
            }
        });
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        mHandler.removeMessages(0);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        mHandler.sendEmptyMessageDelayed(0, TIME_GAP);
                        break;
                    default:
                        mHandler.sendEmptyMessageDelayed(0, TIME_GAP);
                        break;
                }
                return false;
            }
        });

//        // 如果添加页面点击监听器，则页面点击监听器替代视图原有的点击监听器（如果有）
//        if (mOnPageClickListener != null) {
//            for (View view:mViews) {
//                view.setOnClickListener(this);
//            }
//        }

        RadioGroup tipsLayout = (RadioGroup) findViewById(R.id.page_cusor_radioGroup);
        tipsLayout.removeAllViews();

        for (int i = 0, size = viewPager.getAdapter().getCount(); i < size; i++) {
            RadioButton rb = new RadioButton(getContext());
            rb.setBackgroundResource(R.drawable.page_cusor);
            rb.setButtonDrawable(R.drawable.page_cusor);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(18, 18);
            params.setMargins(9, 9, 9, 9);
            rb.setLayoutParams(params);

            tipsLayout.addView(rb);

        }
    }

    public void setImageResources(int[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        if (mViews == null) {
            mViews = new ArrayList<View>();
        } else {
            mViews.clear();
        }

        for (int i = ids.length - 1; i >= 0; i--) {
            ImageView iv = new ImageView(getContext());
            iv.setImageResource(ids[i]);
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mViews.add(iv);
        }

        if (isFinishInflate && !isInit) {
            init();
        }
    }

    public void setImageResources(String[] urls) {

        if (urls == null || urls.length == 0) {
            return;
        }

        for (int i = urls.length - 1; i >= 0; i--) {
            ImageView iv = new ImageView(getContext());
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Picasso.with(getContext()).load(R.drawable.default_image).into(iv);

            String url = urls[i];
            if (url != null) {
                Uri uri = Uri.parse(url);
                Picasso.with(getContext()).load(uri).into(iv);
            }

            mViews.add(iv);
        }

        if (isFinishInflate && !isInit) {
            init();
        }
    }

//    public void setImageFiles(String[] paths) {
//
//    }

    public void setViews(List<View> views) {

        Log.i(TAG, "#setViews isFinishInflate = " + isFinishInflate + " and isInit = " + isInit);
        mViews = views;
        if (isFinishInflate && !isInit) {
            init();
        }

    }

    public void start() {
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, TIME_GAP);
    }

    public void stop() {
        mHandler.removeMessages(0);
    }

    @Override
    public void onClick(View v) {
        for (int i = mViews.size() - 1; i >= 0; i--) {
            View view = mViews.get(i);
            if (v == view) {
                if(mOnPageClickListener != null) {
                    mOnPageClickListener.onPageClick(i);
                }
                break;
            }
        }
    }

    // 指引页面数据适配器
    class MyPageAdapter extends PagerAdapter {

        private List<View> pageViews = null;

        public MyPageAdapter(List<View> views) {
            pageViews = views;

        }

        @Override
        public int getCount() {
            if (pageViews != null) {
                return pageViews.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            // TODO Auto-generated method stub
            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            // TODO Auto-generated method stub
            View view = pageViews.get(arg1);
            ((ViewPager) arg0).addView(view);
            return view;
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    int index = viewPager.getCurrentItem() + 1;
                    index = index % viewPager.getAdapter().getCount();
                    viewPager.setCurrentItem(index, true);
                    mHandler.removeMessages(0);
                    sendEmptyMessageDelayed(0, TIME_GAP);
                    break;
            }
        }
    };

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        Log.i(TAG, "onWindowVisibilityChanged visibility = " + visibility);
        if (visibility == VISIBLE) {
            start();
        } else {
            stop();
        }
    }

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        mOnPageClickListener = onPageClickListener;

        // 如果添加页面点击监听器，则页面点击监听器替代视图原有的点击监听器（如果有）
        if (mOnPageClickListener != null) {
            for (View view:mViews) {
                view.setOnClickListener(this);
            }
        }
    }

    public interface OnPageClickListener {
        public void onPageClick(int position);
    }
}
