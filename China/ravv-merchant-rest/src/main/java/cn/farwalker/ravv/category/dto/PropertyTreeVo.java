package cn.farwalker.ravv.category.dto;

import java.util.List;
/**
 * 分类树
 * @author Administrator
 */
public class PropertyTreeVo {
	private Long id;
	private String label;
	private Boolean leaf;
	private String type;
	private Integer sequence;
	private Boolean addAble;
	private Boolean delAble;
	private List<PropertyTreeVo> children;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<PropertyTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<PropertyTreeVo> children) {
		this.children = children;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getAddAble() {
		return addAble;
	}

	public void setAddAble(Boolean addAble) {
		this.addAble = addAble;
	}

	public Boolean getDelAble() {
		return delAble;
	}

	public void setDelAble(Boolean delAble) {
		this.delAble = delAble;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	
	
}
