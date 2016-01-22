package com.ace.model;

import cn.bmob.im.bean.BmobChatUser;

public class MyUser extends BmobChatUser {
	private Boolean sex;
	private Integer age;
	
	public Boolean getSex() {
		return sex;
	}
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
}
