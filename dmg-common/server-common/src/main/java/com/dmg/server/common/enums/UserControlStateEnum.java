package com.dmg.server.common.enums;

/**
 * @Author liubo
 * @Description //TODO 玩家控制状态
 * @Date 14:29 2020/2/26
 */
public enum UserControlStateEnum {

    CONTROL_STATE_POINT(1, "点控状态", 21, 31),
    CONTROL_STATE_AUTO(2, "自控状态", 22, 32);

    //编号
    private Integer code;
    //描述
    private String desc;
    // 状态id
    private Integer[] stateIds;


    private UserControlStateEnum(Integer code, String desc, Integer ... stateIds) {
		this.code = code;
		this.desc = desc;
		this.stateIds = stateIds;
	}

	public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

	public Integer[] getStateIds() {
		return stateIds;
	}

	public void setStateIds(Integer[] stateIds) {
		this.stateIds = stateIds;
	}
    
	/**
	 * 通过状态id获取code
	 */
	public static Integer getCodeByStateId(Integer id){
		for (UserControlStateEnum e : values()) {
			for (Integer stateId : e.getStateIds()) {
				if (stateId == id) {
					return e.getCode();
				}
			}
		}
		return 0;
	}
}
