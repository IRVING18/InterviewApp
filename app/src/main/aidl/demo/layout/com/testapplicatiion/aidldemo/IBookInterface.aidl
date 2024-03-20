package demo.layout.com.testapplicatiion.aidldemo;

import demo.layout.com.testapplicatiion.aidldemo.Book;
/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/3/20 20:09
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
interface IBookInterface{
    void addBook(in Book book);
    //这个Book需要有Book.aidl parcelable Book来导入，并且包名要一致；
    List<Book> getBookList();
}