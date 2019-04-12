package cn.farwalker.ravv.service.sys.dict.biz.impl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.farwalker.waka.core.BizExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.sys.dict.biz.ISysDictBiz;
import cn.farwalker.ravv.service.sys.dict.dao.ISysDictDao;
import cn.farwalker.ravv.service.sys.dict.dao.ISysDictMgrDao;
import cn.farwalker.ravv.service.sys.dict.model.SysDictBo;
import cn.farwalker.waka.factory.MutiStrFactory;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class SysDictBizImpl extends ServiceImpl<ISysDictDao,SysDictBo> implements ISysDictBiz{
	@Resource
    private ISysDictMgrDao dictDao;

    @Resource
    private ISysDictDao dictMapper;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void addDict(String dictName, String dictValues) {
        //判断有没有该字典
    	Wrapper<SysDictBo> wp = (new EntityWrapper<SysDictBo>()).eq(SysDictBo.Key.name.toString(), dictName).and().eq(SysDictBo.Key.pid.toString(), 0);
        List<SysDictBo> dicts = dictMapper.selectList(wp);
        if(dicts != null && dicts.size() > 0){
            throw new WakaException(BizExceptionEnum.DICT_EXISTED);
        }

        //解析dictValues
        List<Map<String, String>> items = MutiStrFactory.parseKeyValue(dictValues);

        //添加字典
        SysDictBo dict = new SysDictBo();
        dict.setName(dictName);
        dict.setNum(0);
        dict.setPid(0L);
        this.dictMapper.insert(dict);

        //添加字典条目
        for (Map<String, String> item : items) {
            String num = item.get(MutiStrFactory.MUTI_STR_KEY);
            String name = item.get(MutiStrFactory.MUTI_STR_VALUE);
            SysDictBo itemDict = new SysDictBo();
            itemDict.setPid(dict.getId());
            itemDict.setName(name);
            try {
                itemDict.setNum(Integer.valueOf(num));
            }catch (NumberFormatException e){
                throw new WakaException(BizExceptionEnum.DICT_MUST_BE_NUMBER);
            }
            this.dictMapper.insert(itemDict);
        }
    }

    @Override
    public void updateDict(Long dictId, String dictName, String dicts) {
        //删除之前的字典
        this.deleteDict(dictId);

        //重新添加新的字典
        this.addDict(dictName,dicts);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteDict(Long dictId) {
        //删除这个字典的子词典
        Wrapper<SysDictBo> dictEntityWrapper = new EntityWrapper<>();
        dictEntityWrapper = dictEntityWrapper.eq("pid", dictId);
        dictMapper.delete(dictEntityWrapper);

        //删除这个词典
        dictMapper.deleteById(dictId);
    }	
}