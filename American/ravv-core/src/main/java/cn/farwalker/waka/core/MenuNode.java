package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/26.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class MenuNode implements Comparable {
    private Long id;
    private Long parentId;
    private String name;
    private Integer levels;
    private Integer ismenu;
    private Integer num;
    private String url;
    private String icon;
    private List<MenuNode> children;
    private List<MenuNode> linkedList = new ArrayList();

    public static final Integer isMenu = 1;
    public static final Integer notMenu = 0;



    public MenuNode() {
    }

    public MenuNode(Long id, Long parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public Integer getLevels() {
        return this.levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static MenuNode createRoot() {
        return new MenuNode(Long.valueOf(0L), Long.valueOf(-1L));
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MenuNode> getChildren() {
        return this.children;
    }

    public void setChildren(List<MenuNode> children) {
        this.children = children;
    }

    public Integer getNum() {
        return this.num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getIsmenu() {
        return this.ismenu;
    }

    public void setIsmenu(Integer ismenu) {
        this.ismenu = ismenu;
    }

    public String toString() {
        return "MenuNode{id=" + this.id + ", parentId=" + this.parentId + ", name=\'" + this.name + '\'' + ", levels=" + this.levels + ", num=" + this.num + ", url=\'" + this.url + '\'' + ", icon=\'" + this.icon + '\'' + ", children=" + this.children + ", linkedList=" + this.linkedList + '}';
    }

    public int compareTo(Object o) {
        MenuNode menuNode = (MenuNode)o;
        Integer num = menuNode.getNum();
        if(num == null) {
            num = Integer.valueOf(0);
        }

        return this.num.compareTo(num);
    }

    public void buildNodeTree(List<MenuNode> nodeList) {
        Iterator var3 = nodeList.iterator();

        while(var3.hasNext()) {
            MenuNode treeNode = (MenuNode)var3.next();
            List linkedList = treeNode.findChildNodes(nodeList, treeNode.getId());
            if(linkedList.size() > 0) {
                treeNode.setChildren(linkedList);
            }
        }

    }

    public List<MenuNode> findChildNodes(List<MenuNode> nodeList, Long parentId) {
        if(nodeList == null && parentId == null) {
            return null;
        } else {
            Iterator iterator = nodeList.iterator();

            while(iterator.hasNext()) {
                MenuNode node = (MenuNode)iterator.next();
                if(node.getParentId().longValue() != 0L && parentId.equals(node.getParentId())) {
                    this.recursionFn(nodeList, node, parentId);
                }
            }

            return this.linkedList;
        }
    }

    public void recursionFn(List<MenuNode> nodeList, MenuNode node, Long pId) {
        List childList = this.getChildList(nodeList, node);
        if(childList.size() > 0) {
            if(node.getParentId().equals(pId)) {
                this.linkedList.add(node);
            }

            Iterator it = childList.iterator();

            while(it.hasNext()) {
                MenuNode n = (MenuNode)it.next();
                this.recursionFn(nodeList, n, pId);
            }
        } else if(node.getParentId().equals(pId)) {
            this.linkedList.add(node);
        }

    }

    private List<MenuNode> getChildList(List<MenuNode> list, MenuNode node) {
        ArrayList nodeList = new ArrayList();
        Iterator it = list.iterator();

        while(it.hasNext()) {
            MenuNode n = (MenuNode)it.next();
            if(n.getParentId().equals(node.getId())) {
                nodeList.add(n);
            }
        }

        return nodeList;
    }

    public static List<MenuNode> clearBtn(List<MenuNode> nodes) {
        ArrayList noBtns = new ArrayList();
        Iterator var3 = nodes.iterator();

        while(var3.hasNext()) {
            MenuNode node = (MenuNode)var3.next();
            if(node.getIsmenu().intValue() == isMenu) {
                noBtns.add(node);
            }
        }

        return noBtns;
    }

    public static List<MenuNode> clearLevelTwo(List<MenuNode> nodes) {
        ArrayList results = new ArrayList();
        Iterator var3 = nodes.iterator();

        while(var3.hasNext()) {
            MenuNode node = (MenuNode)var3.next();
            Integer levels = node.getLevels();
            if(levels.equals(Integer.valueOf(1))) {
                results.add(node);
            }
        }

        return results;
    }

    public static List<MenuNode> buildTitle(List<MenuNode> nodes) {
        List clearBtn = clearBtn(nodes);
        (new MenuNode()).buildNodeTree(clearBtn);
        List menuNodes = clearLevelTwo(clearBtn);
        Collections.sort(menuNodes);
        Iterator var4 = menuNodes.iterator();

        while(var4.hasNext()) {
            MenuNode menuNode = (MenuNode)var4.next();
            if(menuNode.getChildren() != null && menuNode.getChildren().size() > 0) {
                Collections.sort(menuNode.getChildren());
            }
        }

        return menuNodes;
    }
}
