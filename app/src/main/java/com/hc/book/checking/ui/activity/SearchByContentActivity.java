package com.hc.book.checking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hc.book.checking.AppApplication;
import com.hc.book.checking.R;
import com.hc.book.checking.entity.BookInfo;
import com.hc.book.checking.entity.greenDao.BookInfoDao;
import com.hc.book.checking.ui.adapter.SearchContentAdapter;
import com.hc.book.checking.utils.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****
 * Created by hutao on 2019/4/16.
 * 内容搜索界面
 */
public class SearchByContentActivity extends AppCompatActivity {
    @BindView(R.id.content)
    EditText mEtContent;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private BookInfoDao dao;
    private SearchContentAdapter mAdapter;
    private List<BookInfo> mData = new ArrayList<>();//获取出来的数据

    private int page = 1;//页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_content);
        ButterKnife.bind(this);

        dao = AppApplication.getContext().getDaoSession().getBookInfoDao();

        initRecyclerView();
        initListener();
        getData();
    }

    private void initListener() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refresh -> {
            page = 1;
            getData();
        });

        //上拉加载更多
        refreshLayout.setOnLoadMoreListener(refresh -> {
            getData();
        });

        mAdapter.setOnItemClickListener(isbnCode -> {
            Intent intent = new Intent(this, BookInfoActivity.class);
            intent.putExtra("CODE", isbnCode);
            startActivity(intent);
        });
    }

    private void initRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchContentAdapter(this, mData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.actionBarLeftImage, R.id.search})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.actionBarLeftImage:
                finish();
                break;
            case R.id.search://搜索
                page = 1;
                refreshLayout.autoRefresh();
                break;
        }
    }

    private void getData() {
        List<BookInfo> list = dao.queryBuilder().offset((page - 1) * Constant.PAGE_SIZE).limit(Constant.PAGE_SIZE)
                .whereOr(BookInfoDao.Properties.BookAuthor.like("%" + mEtContent.getText().toString().trim() + "%"),
                        BookInfoDao.Properties.BookName.like("%" + mEtContent.getText().toString().trim() + "%"))
                .list();
        Log.i("------------", "页码：" + page + "加载了数据：" + list.size());

        if (page == 1) {
            mData.clear();
            mData.addAll(list);
            refreshLayout.finishRefresh();
            if (mData.size() > 1) {
                recyclerView.scrollToPosition(0);
            }
            if (list.size() < Constant.PAGE_SIZE) {
                refreshLayout.finishLoadMoreWithNoMoreData();//没有更多数据  向上拉的时候显示没有更多数据
            } else {
                refreshLayout.setEnableLoadMore(true);
            }
        } else {
            mData.addAll(list);
            refreshLayout.finishLoadMore();
            if (list.size() < Constant.PAGE_SIZE) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                refreshLayout.setEnableLoadMore(true);
            }
        }
        page++;
        mAdapter.notifyDataSetChanged();
    }
}
