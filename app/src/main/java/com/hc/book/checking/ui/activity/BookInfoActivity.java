package com.hc.book.checking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hc.book.checking.AppApplication;
import com.hc.book.checking.R;
import com.hc.book.checking.entity.BookInfo;
import com.hc.book.checking.entity.greenDao.BookInfoDao;
import com.hc.book.checking.ui.custom.BookInfoItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**********
 * Create by  hutao on 2019/04/17
 * 书本的详情界面
 */
public class BookInfoActivity extends AppCompatActivity {
    @BindView(R.id.nothing)
    TextView nothing;//没有数据
    @BindView(R.id.thing)
    LinearLayout thing;
    @BindView(R.id.book_name)
    BookInfoItem mTvBookName;//书名
    @BindView(R.id.book_author)
    BookInfoItem mTvBookAuthor;//作者
    @BindView(R.id.book_isbn)
    BookInfoItem mTvBookIsbn;//isbn号
    @BindView(R.id.book_genre)
    BookInfoItem mTvBookGenre;//索书号
    @BindView(R.id.press_name)
    BookInfoItem mTvPressName;//出版社
    @BindView(R.id.page_num)
    BookInfoItem mTvPageNum;//页码
    @BindView(R.id.book_num)
    BookInfoItem mTvBookNum;//数量
    @BindView(R.id.book_prise)
    BookInfoItem mTvBookPrise;//单价
    @BindView(R.id.import_date)
    BookInfoItem mTvImportDate;//导入数据时间

    private String isbnCode;//上一个界面传递来的数据   处理扫描二维码之后的跳转操作
    private BookInfoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        isbnCode = intent.getStringExtra("CODE");

        dao = AppApplication.getContext().getDaoSession().getBookInfoDao();
        List<BookInfo> list = dao.queryBuilder().where(BookInfoDao.Properties.BookISBNCode.eq(isbnCode)).list();
        if (list == null || list.size() <= 0) {
            nothing.setVisibility(View.VISIBLE);
            thing.setVisibility(View.GONE);
        } else {
            nothing.setVisibility(View.GONE);
            thing.setVisibility(View.VISIBLE);
            BookInfo bookInfo = list.get(0);
            setUI(bookInfo);
            Gson gson = new Gson();
            Log.i("-----------", gson.toJson(bookInfo));
        }
    }

    private void setUI(BookInfo info) {
        mTvBookName.setRightContent(info.getBookName());
        mTvBookAuthor.setRightContent(info.getBookAuthor());
        mTvBookIsbn.setRightContent(info.getBookISBNCode());
        mTvBookGenre.setRightContent(info.getGenreCode());
        mTvPressName.setRightContent(info.getPressName());
        mTvPageNum.setRightContent(info.getPageNumber());
        mTvBookNum.setRightContent(info.getBookCount());
        mTvBookPrise.setRightContent(info.getMoney());
        mTvImportDate.setRightContent(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(info.getCreateDate())));
    }

    @OnClick({R.id.actionBarLeftImage})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.actionBarLeftImage:
                finish();
                break;
        }
    }
}
