package cn.farwalker.ravv.admin.login.dto;

import cn.farwalker.waka.auth.validator.dto.Credence;

/**
 * 认证的请求dto
 *
 * @author Jason Chen
 * @Date 2018/02/13 14:00
 */
public class AuthRequest implements Credence {

    private String account;
    private String password;
    private int type;

    public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
    public String getCredenceName() {
        return this.account;
    }

    @Override
    public String getCredenceCode() {
        return this.password;
    }

	@Override
	public int getCredenceType() {
		return this.type;
	}
}
