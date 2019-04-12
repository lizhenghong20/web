package cn.farwalker.ravv.service.sys.dict.biz;
import com.baomidou.mybatisplus.service.IService;
import cn.farwalker.ravv.service.sys.dict.model.SysDictBo;

/**
 * 字典表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISysDictBiz extends IService<SysDictBo>{
    /**
     * 添加字典
     *
     * @author Jason Chen
     * @Date 2017/4/27 17:01
     */
    void addDict(String dictName, String dictValues);

    /**
     * 编辑字典
     *
     * @author Jason Chen
     * @Date 2017/4/28 11:01
     */
    void updateDict(Long dictId, String dictName, String dicts);

    /**
     * 删除字典
     *
     * @author Jason Chen
     * @Date 2017/4/28 11:39
     */
    void deleteDict(Long dictId);
}