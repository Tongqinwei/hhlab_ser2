package com.beans;

/**
 * Created by hasee on 2017/9/2.
 */
public class home {
    private user owner;//主页所有者信息
    private user visitor;//主页访问者信息
    private comment[] comments;//所有者动态信息
    private boolean isHisOwn;//访问者所有者是否为同一个人
    private boolean isFollower;//访问者是否已关注所有者

    public home(){

    }

    public user getOwner() {
        return owner;
    }

    public void setOwner(user owner) {
        this.owner = owner;
    }

    public user getVisitor() {
        return visitor;
    }

    public void setVisitor(user visitor) {
        this.visitor = visitor;
    }

    public comment[] getComments() {
        return comments;
    }

    public void setComments(comment[] comments) {
        this.comments = comments;
    }

    public boolean isHisOwn() {
        return isHisOwn;
    }

    public void setHisOwn(boolean hisOwn) {
        isHisOwn = hisOwn;
    }

    public boolean isFollower() {
        return isFollower;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
    }
}
