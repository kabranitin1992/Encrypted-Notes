package com.aroner.encrypted_notes.Bean;

/**
 * Created by arone on 24-01-2018.
 */

public class TagBean {
    public TagBean(int tagId, String tagName) {
        TagId = tagId;
        TagName = tagName;
    }

    public TagBean() {
    }

    public int getTagId() {
        return TagId;
    }

    public void setTagId(int tagId) {
        TagId = tagId;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String tagName) {
        TagName = tagName;
    }

    int TagId;
    String TagName;

    public int getiUserId() {
        return iUserId;
    }

    public void setiUserId(int iUserId) {
        this.iUserId = iUserId;
    }

    public String getsEmailId() {
        return sEmailId;
    }

    public void setsEmailId(String sEmailId) {
        this.sEmailId = sEmailId;
    }

    public int getiDeleted() {
        return iDeleted;
    }

    public void setiDeleted(int iDeleted) {
        this.iDeleted = iDeleted;
    }

    public String getsCreatedTimeStamp() {
        return sCreatedTimeStamp;
    }

    public void setsCreatedTimeStamp(String sCreatedTimeStamp) {
        this.sCreatedTimeStamp = sCreatedTimeStamp;
    }

    public int getiNoteId() {
        return iNoteId;
    }

    public void setiNoteId(int iNoteId) {
        this.iNoteId = iNoteId;
    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public String getsNote() {
        return sNote;
    }

    public void setsNote(String sNote) {
        this.sNote = sNote;
    }

    public String getCreatedTimestamp() {
        return CreatedTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        CreatedTimestamp = createdTimestamp;
    }

    public String getDeletedTimestamp() {
        return DeletedTimestamp;
    }

    public void setDeletedTimestamp(String deletedTimestamp) {
        DeletedTimestamp = deletedTimestamp;
    }

    public TagBean(int tagId, int iUserId, String tagName,String sCreatedTimeStamp, String deletedTimestamp, int iDeleted) {
        TagId = tagId;
        TagName = tagName;
        this.iUserId = iUserId;
        this.iDeleted = iDeleted;
        this.sCreatedTimeStamp = sCreatedTimeStamp;
        DeletedTimestamp = deletedTimestamp;
    }

    int iUserId;
    String sEmailId;
    int iDeleted;
    String sCreatedTimeStamp;

    int iNoteId;
    String sType;
    String sNote;
    String CreatedTimestamp;
    String DeletedTimestamp;

    public String getsText() {
        return sText;
    }

    public void setsText(String sText) {
        this.sText = sText;
    }

    String sText;

    public String getsTitle() {
        return sTitle;
    }

    public void setsTitle(String sTitle) {
        this.sTitle = sTitle;
    }

    String sTitle;


    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    String str;

    int iIconImg;

    public int getiIconImg() {
        return iIconImg;
    }

    public void setiIconImg(int iIconImg) {
        this.iIconImg = iIconImg;
    }

    public String getsTagTitle() {
        return sTagTitle;
    }

    public void setsTagTitle(String sTagTitle) {
        this.sTagTitle = sTagTitle;
    }

    String sTagTitle;

    public String getsKey() {
        return sKey;
    }

    public void setsKey(String sKey) {
        this.sKey = sKey;
    }

    String sKey;

    public String getsDeletedTimeStamp() {
        return sDeletedTimeStamp;
    }

    public void setsDeletedTimeStamp(String sDeletedTimeStamp) {
        this.sDeletedTimeStamp = sDeletedTimeStamp;
    }

    String sDeletedTimeStamp;

    public String getsSyncTimeStamp() {
        return sSyncTimeStamp;
    }

    public void setsSyncTimeStamp(String sSyncTimeStamp) {
        this.sSyncTimeStamp = sSyncTimeStamp;
    }

    public String getsUpdatedDataTime() {
        return sUpdatedDataTime;
    }

    public void setsUpdatedDataTime(String sUpdatedDataTime) {
        this.sUpdatedDataTime = sUpdatedDataTime;
    }

    String sSyncTimeStamp,sUpdatedDataTime;
}
