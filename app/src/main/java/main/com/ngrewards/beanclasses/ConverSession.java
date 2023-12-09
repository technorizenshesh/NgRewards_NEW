package main.com.ngrewards.beanclasses;


import java.io.File;
import java.io.Serializable;

/**
 * Created by technorizen on 19/12/17.
 */

public class ConverSession implements Serializable {
    String message;
    String chat_id;
    String image;
    String statuss;
    String msg_type;
    String chat_video;
    String video_thumb_img;
    String file_name;
    String attach_file_name;
    String no_of_message;
    String file_path;
    String fullname;
    boolean fileIsAvb;
    File file;
    String id;
    String senderid;
    String reciverid;
    String datetime;
    String time;
    String senderimg;
    String reciverimg;
    String sendername;
    String recname;
    String sender_online_status;
    String userimg;
    String username;
    String chat_image;
    String receiver_type;

    public String getNo_of_message() {
        return no_of_message;
    }

    public void setNo_of_message(String no_of_message) {
        this.no_of_message = no_of_message;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isFileIsAvb() {
        return fileIsAvb;
    }

    public void setFileIsAvb(boolean fileIsAvb) {
        this.fileIsAvb = fileIsAvb;
    }

    public String getAttach_file_name() {
        return attach_file_name;
    }

    public void setAttach_file_name(String attach_file_name) {
        this.attach_file_name = attach_file_name;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getVideo_thumb_img() {
        return video_thumb_img;
    }

    public void setVideo_thumb_img(String video_thumb_img) {
        this.video_thumb_img = video_thumb_img;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getChat_video() {
        return chat_video;
    }

    public void setChat_video(String chat_video) {
        this.chat_video = chat_video;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(String receiver_type) {
        this.receiver_type = receiver_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getChat_image() {
        return chat_image;
    }

    public void setChat_image(String chat_image) {
        this.chat_image = chat_image;
    }

    public String getReciverid() {
        return reciverid;
    }

    public void setReciverid(String reciverid) {
        this.reciverid = reciverid;
    }

    public String getSender_online_status() {
        return sender_online_status;
    }

    public void setSender_online_status(String sender_online_status) {
        this.sender_online_status = sender_online_status;
    }

    public String getSenderimg() {
        return senderimg;
    }

    public void setSenderimg(String senderimg) {
        this.senderimg = senderimg;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getReciverimg() {
        return reciverimg;
    }

    public void setReciverimg(String reciverimg) {
        this.reciverimg = reciverimg;
    }

    public String getRecname() {
        return recname;
    }

    public void setRecname(String recname) {
        this.recname = recname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatuss() {
        return statuss;
    }

    public void setStatuss(String statuss) {
        this.statuss = statuss;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    @Override
    public String toString() {
        return "ConverSession{" +
                "message='" + message + '\'' +
                ", chat_id='" + chat_id + '\'' +
                ", image='" + image + '\'' +
                ", statuss='" + statuss + '\'' +
                ", msg_type='" + msg_type + '\'' +
                ", chat_video='" + chat_video + '\'' +
                ", video_thumb_img='" + video_thumb_img + '\'' +
                ", file_name='" + file_name + '\'' +
                ", attach_file_name='" + attach_file_name + '\'' +
                ", no_of_message='" + no_of_message + '\'' +
                ", file_path='" + file_path + '\'' +
                ", fullname='" + fullname + '\'' +
                ", fileIsAvb=" + fileIsAvb +
                ", file=" + file +
                ", id='" + id + '\'' +
                ", senderid='" + senderid + '\'' +
                ", reciverid='" + reciverid + '\'' +
                ", datetime='" + datetime + '\'' +
                ", time='" + time + '\'' +
                ", senderimg='" + senderimg + '\'' +
                ", reciverimg='" + reciverimg + '\'' +
                ", sendername='" + sendername + '\'' +
                ", recname='" + recname + '\'' +
                ", sender_online_status='" + sender_online_status + '\'' +
                ", userimg='" + userimg + '\'' +
                ", username='" + username + '\'' +
                ", chat_image='" + chat_image + '\'' +
                ", receiver_type='" + receiver_type + '\'' +
                '}';
    }
}
