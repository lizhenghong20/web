package cn.farwalker.ravv.category.dto;

/**
 * 分类设置
 * @author Administrator
 */
public class PropertySettingVo {
	private Long propertyId;
	private String propertyType;
	private Boolean required;
	private Boolean manuallyInput;
	private Boolean multiSelect;
	private Boolean isimage;
	
	public Long getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public Boolean getManuallyInput() {
		return manuallyInput;
	}
	public void setManuallyInput(Boolean manuallyInput) {
		this.manuallyInput = manuallyInput;
	}
	public Boolean getMultiSelect() {
		return multiSelect;
	}
	public void setMultiSelect(Boolean multiSelect) {
		this.multiSelect = multiSelect;
	}
	public Boolean getIsimage() {
		return isimage;
	}
	public void setIsimage(Boolean isimage) {
		this.isimage = isimage;
	}
	
}
