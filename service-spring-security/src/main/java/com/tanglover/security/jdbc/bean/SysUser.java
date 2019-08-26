package com.tanglover.security.jdbc.bean;

import java.io.Serializable;

/**
 * @author TangXu
 * @create 2019-05-27 11:27:18
 * @description: SysUser
 */
@SuppressWarnings({"serial"})
public class SysUser implements Cloneable, Serializable {

    public static String[] columns = {"sys_user_id", "account", "password", "no", "gender", "nickname", "enabled", "truename", "remark", "creator", "create_time", "modifier", "modify_time"};
    public long sysUserId;//bigint(20)    
    public String account = "";//varchar(64)    帐号
    public String password = "";//varchar(64)    密码
    public String no = "";//varchar(64)    编号
    public int gender;//int(11)    性别（0：未知、1：man、2：lady）
    public String nickname = "";//varchar(64)    昵称
    public int enabled;//int(11)    是否禁用（0：否、1：是）
    public String truename = "";//varchar(64)    真实姓名
    public String remark = "";//varchar(500)    备注
    public long creator;//bigint(20)    创建人
    public long createTime;//bigint(20)    创建时间
    public long modifier;//bigint(20)    修改人
    public long modifyTime;//bigint(20)    修改时间

    public long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
    	if (account == null) {
            account = "";
        }
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
    	if (password == null) {
            password = "";
        }
        this.password = password;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
    	if (no == null) {
            no = "";
        }
        this.no = no;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
    	if (nickname == null) {
            nickname = "";
        }
        this.nickname = nickname;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
    	if (truename == null) {
            truename = "";
        }
        this.truename = truename;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
    	if (remark == null) {
            remark = "";
        }
        this.remark = remark;
    }

    public long getCreator() {
        return creator;
    }

    public void setCreator(long creator) {
        this.creator = creator;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getModifier() {
        return modifier;
    }

    public void setModifier(long modifier) {
        this.modifier = modifier;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public static SysUser newSysUser(long sysUserId, String account, String password, String no, int gender, String nickname, int enabled, String truename, String remark, long creator, long createTime, long modifier, long modifyTime) {
        SysUser ret = new SysUser();
        ret.setSysUserId(sysUserId);
        ret.setAccount(account);
        ret.setPassword(password);
        ret.setNo(no);
        ret.setGender(gender);
        ret.setNickname(nickname);
        ret.setEnabled(enabled);
        ret.setTruename(truename);
        ret.setRemark(remark);
        ret.setCreator(creator);
        ret.setCreateTime(createTime);
        ret.setModifier(modifier);
        ret.setModifyTime(modifyTime);
        return ret;    
    }
}