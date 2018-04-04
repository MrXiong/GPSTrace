package com.jgw.gpstrace.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jgw.gpstrace.R;


public class ToolBarActivity extends BaseActivity {

    public static final String LOG_TAG = ToolBarActivity.class.getSimpleName();

    public static final int TITLE_MODE_LEFT = -1;
    public static final int TITLE_MODE_CENTER = -2;
    public static final int TITLE_MODE_NONE = -3;

    private int titleMode = TITLE_MODE_CENTER;

    protected TextView tvTitle;
    protected Toolbar toolbar;
    protected FrameLayout toolbarLayout;
    private LinearLayout contentView;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initToolBar();
        initContentView();
    }


    private void initToolBar() {
        toolbarLayout = new FrameLayout(this);
        LayoutInflater.from(this).inflate(R.layout.layout_toobar, toolbarLayout,
                true);
        tvTitle = (TextView) toolbarLayout.findViewById(R.id.tv_title);
        toolbar = (Toolbar) toolbarLayout.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(
                R.color.primary_text_default_material_dark));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeAsUpIndicator(R.mipmap.icon_back);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void initContentView() {
        contentView = new LinearLayout(this);
        contentView.setOrientation(LinearLayout.VERTICAL);
    }


    @Override
    public void setContentView(int layoutResID) {
        // 添加toolbar布局
        contentView.addView(toolbarLayout, new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        LayoutInflater.from(this).inflate(layoutResID, contentView, true);

        super.setContentView(contentView, new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));


    }

    @Override
    public void setContentView(View view) {
        setNewContentView(view, view.getLayoutParams());
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        setNewContentView(view, params);
    }

    private void setNewContentView(View view, LayoutParams params) {
        // 添加toolbar布局
        contentView.addView(toolbarLayout, new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        // content FrameLayout
        FrameLayout contentLayout = new FrameLayout(this);
        if (params == null) {
            params = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
        }
        contentLayout.addView(view, params);

        // 添加content布局
        contentView.addView(contentLayout, new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        super.setContentView(contentView, new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }

    protected void setEmptyToolbar() {
        if (toolbarLayout != null) {
            toolbarLayout.setVisibility(View.GONE);
        }
    }

    public void setTitle(CharSequence title, int mode) {
        this.titleMode = mode;
        setTitle(title);
    }

    public void setTitleMode(int mode) {
        this.titleMode = mode;
        setTitle(getTitle());
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (titleMode == TITLE_MODE_NONE) {
            if (toolbar != null) {
                toolbar.setTitle("");
            }
            if (tvTitle != null) {
                tvTitle.setText("");
            }
        } else if (titleMode == TITLE_MODE_LEFT) {
            if (toolbar != null) {
                toolbar.setTitle(title);
            }
            if (tvTitle != null) {
                tvTitle.setText("");
            }
        } else if (titleMode == TITLE_MODE_CENTER) {
            if (toolbar != null) {
                toolbar.setTitle("");
            }
            if (tvTitle != null) {
                tvTitle.setText(title);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            back();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void back() {
        super.onBackPressed();
    }


    protected void setNavigationIcon(int resId) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(resId);
        }
    }

    protected void setNavigationIcon(Drawable icon) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(icon);
        }
    }


}
