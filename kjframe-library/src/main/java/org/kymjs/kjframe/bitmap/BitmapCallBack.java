package org.kymjs.kjframe.bitmap;

import android.graphics.Bitmap;

/**
 * BitmapLibrary中的回调方法
 * 
 * @author kymjs (https://github.com/kymjs)
 * 
 */
public abstract class BitmapCallBack {
    /** 载入前回调 */
    public void onPreLoad() {}

    /** bitmap载入完成将回调 */
    public void onSuccess(final Bitmap bitmap) {}

    /** bitmap载入失败将回调 */
    public void onFailure(final Exception e) {}

    /** bitmap载入完成不管成功失败 */
    public void onFinish() {}

    /** bitmap开始加载网络图片 */
    public void onDoHttp() {}
}
