package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 18/8/18.
 */

public class CommentDatum {
    @SerializedName("comment_ID")
    @Expose
    private String commentID;
    @SerializedName("comment_post_ID")
    @Expose
    private String commentPostID;
    @SerializedName("comment_author")
    @Expose
    private String commentAuthor;
    @SerializedName("comment_author_email")
    @Expose
    private String commentAuthorEmail;
    @SerializedName("comment_author_url")
    @Expose
    private String commentAuthorUrl;
    @SerializedName("comment_author_IP")
    @Expose
    private String commentAuthorIP;
    @SerializedName("comment_date")
    @Expose
    private String commentDate;
    @SerializedName("comment_date_gmt")
    @Expose
    private String commentDateGmt;
    @SerializedName("comment_content")
    @Expose
    private String commentContent;
    @SerializedName("comment_karma")
    @Expose
    private String commentKarma;
    @SerializedName("comment_approved")
    @Expose
    private String commentApproved;
    @SerializedName("comment_agent")
    @Expose
    private String commentAgent;
    @SerializedName("comment_type")
    @Expose
    private String commentType;
    @SerializedName("comment_parent")
    @Expose
    private String commentParent;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getCommentPostID() {
        return commentPostID;
    }

    public void setCommentPostID(String commentPostID) {
        this.commentPostID = commentPostID;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public String getCommentAuthorEmail() {
        return commentAuthorEmail;
    }

    public void setCommentAuthorEmail(String commentAuthorEmail) {
        this.commentAuthorEmail = commentAuthorEmail;
    }

    public String getCommentAuthorUrl() {
        return commentAuthorUrl;
    }

    public void setCommentAuthorUrl(String commentAuthorUrl) {
        this.commentAuthorUrl = commentAuthorUrl;
    }

    public String getCommentAuthorIP() {
        return commentAuthorIP;
    }

    public void setCommentAuthorIP(String commentAuthorIP) {
        this.commentAuthorIP = commentAuthorIP;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentDateGmt() {
        return commentDateGmt;
    }

    public void setCommentDateGmt(String commentDateGmt) {
        this.commentDateGmt = commentDateGmt;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentKarma() {
        return commentKarma;
    }

    public void setCommentKarma(String commentKarma) {
        this.commentKarma = commentKarma;
    }

    public String getCommentApproved() {
        return commentApproved;
    }

    public void setCommentApproved(String commentApproved) {
        this.commentApproved = commentApproved;
    }

    public String getCommentAgent() {
        return commentAgent;
    }

    public void setCommentAgent(String commentAgent) {
        this.commentAgent = commentAgent;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getCommentParent() {
        return commentParent;
    }

    public void setCommentParent(String commentParent) {
        this.commentParent = commentParent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
