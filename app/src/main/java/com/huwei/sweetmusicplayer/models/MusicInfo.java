package com.huwei.sweetmusicplayer.models;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.huwei.sweetmusicplayer.R;
import com.huwei.sweetmusicplayer.SweetApplication;
import com.huwei.sweetmusicplayer.abstracts.AbstractMusic;
import com.huwei.sweetmusicplayer.util.BitmapUtil;
import com.huwei.sweetmusicplayer.util.CharacterParser;
import com.huwei.sweetmusicplayer.util.MusicUtils;

import java.io.File;

/**
 * Entity mapped to table MUSIC_INFO.
 */
public class MusicInfo extends AbstractMusic {

    private Long songId;
    private Long albumId;
    private String title;
    private String artist;
    private Integer duration;
    private String path;
    private Boolean favorite;

    public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>() {

        @Override
        public MusicInfo createFromParcel(Parcel source) {
            return new MusicInfo(source);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    //------------------- new property
    private String album ;
    private Long size ;

    public MusicInfo() {

    }

    public MusicInfo(Long songId, Long albumId, String title, String artist, Integer duration, String path, Boolean favorite) {
        this.songId = songId;
        this.albumId = albumId;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
        this.favorite = favorite;
    }


    /** constructor with special property*/
    public MusicInfo(Long songId, Long albumId, String title, String artist, Integer duration, String path , String album , Long size) {
        this.songId = songId;
        this.albumId = albumId;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
        this.album = album ;
        this.size = size ;
    }
    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String getArtPic() {
        Uri uri= ContentUris.withAppendedId(MusicUtils.sArtworkUri, albumId);

        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor actualimagecursor = SweetApplication.context.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        if(actualimagecursor.moveToFirst()) {
            String img_path = actualimagecursor.getString(actual_image_column_index);
            File file = new File(img_path);
            Uri fileUri = Uri.fromFile(file);
            return fileUri.toString();
        }
        return "";
    }

    @Override
    public void loadArtPic(OnLoadListener loadListener) {
        new LoadArtPicTask(loadListener).execute(albumId);
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    /**
     *
     * define by myself
     */
    public static final Byte ISFAVORITE=1;
    public static final Byte NOTFAVORITE=0;

    public MusicInfo(Parcel parcel){
        super();

        this.songId=parcel.readLong();
        this.albumId=parcel.readLong();
        this.title=parcel.readString();
        this.artist = parcel.readString();
        this.duration = parcel.readInt();
        this.path = parcel.readString();
        this.favorite = parcel.readByte()==ISFAVORITE;
    }


    /**
     * 获取title的首字母
     * @return
     */
    public String getKeyofTitle(){
        return CharacterParser.getFirstUpperLetter(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(songId);
        dest.writeLong(albumId);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeInt(duration);
        dest.writeString(path);
        dest.writeByte(favorite ? ISFAVORITE : NOTFAVORITE);
    }



    @Override
    public Uri getDataSoure() {
        return Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + getSongId());
    }

    @Override
    public MusicType getType() {
        return MusicType.Local;
    }

    @Override
    public MusicInfo createFromParcel(Parcel source) {
        return new MusicInfo(source);
    }

    @Override
    public MusicInfo[] newArray(int size) {
        return new MusicInfo[size];
    }

    private class LoadArtPicTask extends AsyncTask<Long,Void,Bitmap>{

        OnLoadListener onLoadListener;

        private LoadArtPicTask(OnLoadListener onLoadListener) {
            this.onLoadListener = onLoadListener;
        }

        @Override
        protected Bitmap doInBackground(Long... params) {
            long albumId = params[0];
            return MusicUtils.getArtworkQuick(SweetApplication.context,albumId);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if(onLoadListener!=null){
                if(bitmap==null){
                    bitmap = BitmapUtil.drawableToBitamp(SweetApplication.context.getResources().getDrawable(R.drawable.img_album_background));
                }

                onLoadListener.onSuccessLoad(bitmap);
            }

        }
    }

}
