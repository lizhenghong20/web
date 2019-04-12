package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/26.
 */
public class ZTreeNode {
    private Long id;
    private Long pId;
    private String name;
    private Boolean open;
    private Boolean checked;

    public ZTreeNode() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return this.pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOpen() {
        return this.open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getIsOpen() {
        return this.open;
    }

    public void setIsOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getChecked() {
        return this.checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public static ZTreeNode createParent() {
        ZTreeNode zTreeNode = new ZTreeNode();
        zTreeNode.setChecked(Boolean.valueOf(true));
        zTreeNode.setId(Long.valueOf(0L));
        zTreeNode.setName("顶级");
        zTreeNode.setOpen(Boolean.valueOf(true));
        zTreeNode.setpId(Long.valueOf(0L));
        return zTreeNode;
    }
}

