package com.hc.book.checking.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by hutao on 2019/4/15.
 * 书的基本信息
 */
@Entity
public class BookInfo {
    @Id(autoincrement = true)
    private Long id;
    private String bookName;//书名
    private String bookAuthor;//作者
    private String bookISBNCode;//ISBN号
    private String genreCode;//索书号  即  分类号
    private String pressName;//出版社名称
    private String purchaseDate;//购买日期  出版日期
    private String pageNumber;//页数
    private String bookCount;//书的数量  订数和册数
    private String money;//金额  单价
    private String totalMoney;//总金额
    private Long createDate;//创建此条数据的时间

    @Generated(hash = 1270076483)
    public BookInfo(Long id, String bookName, String bookAuthor,
                    String bookISBNCode, String genreCode, String pressName,
                    String purchaseDate, String pageNumber, String bookCount, String money,
                    String totalMoney, Long createDate) {
        this.id = id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookISBNCode = bookISBNCode;
        this.genreCode = genreCode;
        this.pressName = pressName;
        this.purchaseDate = purchaseDate;
        this.pageNumber = pageNumber;
        this.bookCount = bookCount;
        this.money = money;
        this.totalMoney = totalMoney;
        this.createDate = createDate;
    }

    @Generated(hash = 1952025412)
    public BookInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return this.bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return this.bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookISBNCode() {
        return this.bookISBNCode;
    }

    public void setBookISBNCode(String bookISBNCode) {
        this.bookISBNCode = bookISBNCode;
    }

    public String getGenreCode() {
        return this.genreCode;
    }

    public void setGenreCode(String genreCode) {
        this.genreCode = genreCode;
    }

    public String getPressName() {
        return this.pressName;
    }

    public void setPressName(String pressName) {
        this.pressName = pressName;
    }

    public String getPurchaseDate() {
        return this.purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPageNumber() {
        return this.pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getBookCount() {
        return this.bookCount;
    }

    public void setBookCount(String bookCount) {
        this.bookCount = bookCount;
    }

    public String getMoney() {
        return this.money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTotalMoney() {
        return this.totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Long getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

}
