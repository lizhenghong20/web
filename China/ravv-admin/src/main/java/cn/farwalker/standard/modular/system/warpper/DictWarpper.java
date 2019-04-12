package cn.farwalker.standard.modular.system.warpper;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import cn.farwalker.ravv.service.sys.dict.model.SysDictBo;
import cn.farwalker.standard.common.constant.factory.ConstantFactory;
import cn.farwalker.waka.util.Tools;

/**
 * 字典列表的包装
 *
 * @author Jason Chen
 * @date 2017年4月25日 18:10:31
 */
public class DictWarpper extends BaseControllerWarpper {

    public DictWarpper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        StringBuffer detail = new StringBuffer();
        BigInteger i1 = (BigInteger) map.get("id");
        Long id = i1.longValue();
        List<SysDictBo> dicts = ConstantFactory.me().findInDict(id);
        if(dicts != null){
            for (SysDictBo dict : dicts) {
                detail.append(dict.getNum() + ":" +dict.getName() + ",");
            }
            map.put("detail", Tools.string.removeSuffix(detail.toString(),","));
        }
    }

}
