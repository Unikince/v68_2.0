package com.zyhy.lhj_server.bgmanagement.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 18:02 2019/11/6
 */
public class SysUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建人
     */
    private Integer createUser;
    /**
     * 修改时间
     */
    private Date modifyDate;
    /**
     * 修改人
     */
    private Integer modifyUser;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * userName
     */
    private String userName;
    /**
     * password
     */
    private String password;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Integer getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(Integer modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "SysUserDTO [id=" + id + ", createDate=" + createDate + ", createUser=" + createUser + ", modifyDate="
				+ modifyDate + ", modifyUser=" + modifyUser + ", sort=" + sort + ", userName=" + userName
				+ ", password=" + password + "]";
	}
    
}
