package demo.layout.com.testapplicatiion.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

/**
 * 远程service，模拟跨进程通信
 */
public class BookLibraryService extends Service {
    private List<Book> mList;
    public BookLibraryService() {
        mList = new ArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    /**
     * 服务端BBinder的IBinder对象；
     * 普通应用没权限通过serviceManager去注册，然后客户端getService()获取
     * 所以通过Service去onBind()方法直接获取IBinder对象，提供给客户端去创建BpBinder对象；
     */
    IBookInterface.Stub mIBinder = new IBookInterface.Stub() {
        @Override
        public void addBook(Book book) throws RemoteException {
            //模拟通信，收到book，直接放到list中
            mList.add(book);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            //模拟通信，直接返回所有book列表
            return mList;
        }
    };
}