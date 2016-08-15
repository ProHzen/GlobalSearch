package com.bbk.open.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by Administrator on 2016/8/12.
 */

public class ToolUtil {

    /**
     * 根据album_id获取专辑图片路径
     * videoId =cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)));
     *
     */
    public static String getVideoPreviewPath(int videoId,Context mContext) {

        String path = null;

        Uri albumURI = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;

        ContentResolver cr = mContext.getContentResolver();
        Cursor albumCursor = cr.query(albumURI, null, null, null, null);
        if (albumCursor == null) {
            return null;
        }
        albumCursor.moveToFirst();
        while (!albumCursor.isAfterLast()) {
            //设置路径
            int tempId=albumCursor.getInt(albumCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.VIDEO_ID));
            if(videoId==tempId){
                //如果ID相等则返回
                path=albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                break;
            }
            albumCursor.moveToNext();
        }
        albumCursor.close();
        return path;
    }

    /**
     * 根据album_id获取专辑图片路径
     *albumId=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
     *
     */
    public static String getAlbumArtPath(int albumId,Context mContext) {

        String path = null;

        Uri albumURI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        ContentResolver cr = mContext.getContentResolver();
        Cursor albumCursor = cr.query(albumURI, null, null, null, null);
        if (albumCursor == null) {
            return null;
        }
        albumCursor.moveToFirst();
        while (!albumCursor.isAfterLast()) {
            //设置路径
            int tempId=albumCursor.getInt(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
            if(albumId==tempId){
                //如果ID相等则返回
                path=albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
                break;
            }
            albumCursor.moveToNext();
        }
        albumCursor.close();
        return path;
    }
    /**
     * 根据文件路径获取专辑路径
     * @param mContext
     * @param path
     * @return
     */
    public static String getAlbumByFilePath(Context mContext, String path) {

        Uri originalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(originalUri, null, null, null, null);
        if (cursor == null) {
            return null;
        }

        String tempPath;
        String albumPath=null;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            //设置路径
            tempPath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            if(tempPath.equals(path)){
                //路径相等返回专辑图片路径

                int albumId=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                albumPath=getAlbumArtPath(albumId, mContext);
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return albumPath;
    }

    /**
     * 根据文件路径获取视频路径
     * @param mContext
     * @param path
     * @return
     */
    public static String getVideoPreViewByFilePath(Context mContext, String path) {

        Uri originalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(originalUri, null, null, null, null);
        if (cursor == null) {
            return null;
        }

        String tempPath;
        String albumPath=null;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            //设置路径
            tempPath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            if(tempPath.equals(path)){
                //路径相等返回专辑图片路径

                int videoId=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                albumPath=getVideoPreviewPath(videoId, mContext);
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return albumPath;

    }

    /**
     * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过
     * appInfo.publicSourceDir = apkPath;来修正这个问题，详情参见:
     * http://code.google.com/p/android/issues/detail?id=9151
     */
    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }




}