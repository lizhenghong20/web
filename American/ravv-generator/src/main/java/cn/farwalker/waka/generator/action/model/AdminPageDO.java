package cn.farwalker.waka.generator.action.model;

/**
 * <p>
 * 生成运营后台的页面对象
 * </p>
 * @author Jason Chen
 * @Since 2018-02-10
 *
 */
public class AdminPageDO {
	
	/**
	 * 生成路径
	 */
	private String path = "";
	
	/**
     * 主页
     */
    private Boolean indexPageSwitch = false;

    /**
     * 添加页面
     */
    private Boolean addPageSwitch = false;

    /**
     * 编辑页面
     */
    private Boolean editPageSwitch = false;

    /**
     * 主页的js
     */
    private Boolean jsSwitch = false;

    /**
     * 详情页面js
     */
    private Boolean infoJsSwitch = false;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Boolean getIndexPageSwitch() {
		return indexPageSwitch;
	}

	public void setIndexPageSwitch(Boolean indexPageSwitch) {
		this.indexPageSwitch = indexPageSwitch;
	}

	public Boolean getAddPageSwitch() {
		return addPageSwitch;
	}

	public void setAddPageSwitch(Boolean addPageSwitch) {
		this.addPageSwitch = addPageSwitch;
	}

	public Boolean getEditPageSwitch() {
		return editPageSwitch;
	}

	public void setEditPageSwitch(Boolean editPageSwitch) {
		this.editPageSwitch = editPageSwitch;
	}

	public Boolean getJsSwitch() {
		return jsSwitch;
	}

	public void setJsSwitch(Boolean jsSwitch) {
		this.jsSwitch = jsSwitch;
	}

	public Boolean getInfoJsSwitch() {
		return infoJsSwitch;
	}

	public void setInfoJsSwitch(Boolean infoJsSwitch) {
		this.infoJsSwitch = infoJsSwitch;
	}
    
}
