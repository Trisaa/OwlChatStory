package com.owl.chatstory.data.chatsource.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.owl.chatstory.data.usersource.model.UserModel;

import java.util.List;

/**
 * Created by lebron on 2017/9/17.
 */

public class FictionDetailModel implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("cover")
    private String cover;
    @SerializedName("tags")
    private List<String> tags;
    @SerializedName("summary")
    private String summary;
    @SerializedName("views")
    private int views;
    @SerializedName("writer")
    private UserModel writer;
    @SerializedName("chapter")
    private List<ChapterModel> chapters;
    @SerializedName("createline")
    private Long createLine;
    @SerializedName("is_end")
    private int ended;//是否完结
    @SerializedName("serials")
    private int serials;//是否是连载
    @SerializedName("language")
    private String language;//语言

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public UserModel getWriter() {
        return writer;
    }

    public void setWriter(UserModel writer) {
        this.writer = writer;
    }

    public List<ChapterModel> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChapterModel> chapters) {
        this.chapters = chapters;
    }

    public Long getCreateLine() {
        return createLine;
    }

    public void setCreateLine(Long createLine) {
        this.createLine = createLine;
    }

    public int getEnded() {
        return ended;
    }

    public void setEnded(int ended) {
        this.ended = ended;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getSerials() {
        return serials;
    }

    public void setSerials(int serials) {
        this.serials = serials;
    }

    public FictionDetailModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.cover);
        dest.writeStringList(this.tags);
        dest.writeString(this.summary);
        dest.writeInt(this.views);
        dest.writeParcelable(this.writer, flags);
        dest.writeTypedList(this.chapters);
        dest.writeString(this.token);
        dest.writeString(this.language);
    }

    protected FictionDetailModel(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.cover = in.readString();
        this.tags = in.createStringArrayList();
        this.summary = in.readString();
        this.views = in.readInt();
        this.writer = in.readParcelable(UserModel.class.getClassLoader());
        this.chapters = in.createTypedArrayList(ChapterModel.CREATOR);
        this.token = in.readString();
        this.language = in.readString();
    }

    public static final Creator<FictionDetailModel> CREATOR = new Creator<FictionDetailModel>() {
        @Override
        public FictionDetailModel createFromParcel(Parcel source) {
            return new FictionDetailModel(source);
        }

        @Override
        public FictionDetailModel[] newArray(int size) {
            return new FictionDetailModel[size];
        }
    };
}
