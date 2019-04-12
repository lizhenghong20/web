package com.cangwu.frame.web;
/**
 * 在线用户<br/>
 * <b>注意:</b>由于IOnlineUser要写session,所以有get就要有set;
 * @author juno
 */
public interface IOnlineUser {
    public String KEY_ONLINE = "online_user";

    /*private String id;
    private String code;
    private String name;*/
    public Long getId();

    public void setId(Long id);

    public String getAccount();

    public void setAccount(String code);

    public String getName();

    public void setName(String name);

    public String getIp();

    public void setIp(String ip);
}
