package cn.farwalker.ravv.admin.profitallot;


import cn.farwalker.ravv.admin.profitallot.dto.MemberPromoteInfoVo;
import cn.farwalker.ravv.admin.profitallot.dto.MemberRelativeTreeVo;
import cn.farwalker.ravv.admin.profitallot.dto.RebatedMonthVo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.constants.MemberSubordinate;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberSimpleInfoVo;
import cn.farwalker.ravv.service.sale.profitallot.biz.ISaleProfitAllotBiz;
import cn.farwalker.ravv.service.sale.profitallot.constants.DistStatusEnum;
import cn.farwalker.ravv.service.sale.profitallot.model.ProfitAllotInfoVo;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.plugins.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminProfitallotServiceImpl implements AdminProfitallotService{

    @Resource
    private ISaleProfitAllotBiz saleProfitAllotBiz;
    protected ISaleProfitAllotBiz getBiz(){
        return saleProfitAllotBiz;
    }

    @Resource
    private IMemberBiz memberBiz;

    @Override
    public MemberPromoteInfoVo getPromoteInfo(Long memberId) {
        //获取会员信息
        MemberBo member = memberBiz.selectById(memberId);
        if(null == member) {
            throw new WakaException("找不到该会员信息");
        }
        MemberPromoteInfoVo promoteInfo = new MemberPromoteInfoVo();
        Tools.bean.copyProperties(member, promoteInfo);

        //补全头像路径
        promoteInfo.setAvator(QiniuUtil.getFullPath(promoteInfo.getAvator()));

        return promoteInfo;
    }

    @Override
    public MemberRelativeTreeVo getRelativeTree(Long memberId, Integer start, Integer size) {
        //获取会员信息
        MemberBo member = memberBiz.selectById(memberId);
        if(null == member) {
            throw new WakaException("找不到该会员信息");
        }
        if(null == member.getReferralCode()) {
            throw new WakaException("该会员没有推荐码，请联系管理员");
        }

        MemberRelativeTreeVo MemberRelativeTree = new MemberRelativeTreeVo();
        //获取会员上级推荐人
        MemberSimpleInfoVo supurior = memberBiz.getMemberSupurior(memberId);
        MemberRelativeTree.setSupurior(supurior);

        //获取第一级下级推荐人
        Page<MemberSimpleInfoVo> fristSubordinate = memberBiz.subordinateList(member.getReferralCode(), MemberSubordinate.Frist_Subordinate, start, size);
        MemberRelativeTree.setFristSubordinate(fristSubordinate);

        //获取第二级下级推荐人
        Page<MemberSimpleInfoVo> secondSubordinate = memberBiz.subordinateList(member.getReferralCode(), MemberSubordinate.Second_Subordinate, start, size);
        MemberRelativeTree.setSecondSubordinate(secondSubordinate);

        //获取第三级下级推荐人
        Page<MemberSimpleInfoVo> thirdSubordinate = memberBiz.subordinateList(member.getReferralCode(), MemberSubordinate.Third_Subordinate, start, size);
        MemberRelativeTree.setThirdSubordinate(thirdSubordinate);

        return MemberRelativeTree;
    }

    @Override
    public Page<MemberSimpleInfoVo> getRelativeByLevel(Long memberId, MemberSubordinate subordinate, Integer start, Integer size) {
        //获取会员信息
        MemberBo member = memberBiz.selectById(memberId);
        if(null == member) {
            throw new WakaException("找不到该会员信息");
        }

        Page<MemberSimpleInfoVo> relativePage = memberBiz.subordinateList(member.getReferralCode(), subordinate, start, size);

        return relativePage;
    }

    @Override
    public Page<ProfitAllotInfoVo> getRebatedListByStatus(Long memberId, DistStatusEnum status, Integer start, Integer size) {
        //获取会员信息
        MemberBo member = memberBiz.selectById(memberId);
        if(null == member) {
            throw new WakaException("找不到该会员信息");
        }

        Page<ProfitAllotInfoVo> profitAllotInfoPage = saleProfitAllotBiz.profitAllotInfoPage(memberId, status, start, size);

        return profitAllotInfoPage;
    }

    @Override
    public Page<RebatedMonthVo> getRebatedList(Long memberId, Integer start, Integer size) {
        //获取会员信息
        MemberBo member = memberBiz.selectById(memberId);
        if(null == member) {
            throw new WakaException("找不到该会员信息");
        }

        //获取已返现分销记录
        Page<ProfitAllotInfoVo> profitAllotInfoList = saleProfitAllotBiz.profitAllotInfoPage(memberId, DistStatusEnum.Returned, start, size);
        if(Tools.collection.isEmpty(profitAllotInfoList.getRecords())){
            Page<RebatedMonthVo> rebatedMonthVoPage = new Page<>();
            return rebatedMonthVoPage;
        }

        //按月份排序
        List<RebatedMonthVo> rebatedMonthVoList = this.rebatedSort(profitAllotInfoList.getRecords());

        //转换为分页格式，方便前端做分页，分页仍依据分销记录
        Page<RebatedMonthVo> rebatedMonthVoPage = new Page<>();
        rebatedMonthVoPage.setCurrent(profitAllotInfoList.getCurrent());
        rebatedMonthVoPage.setSize(profitAllotInfoList.getSize());
        rebatedMonthVoPage.setTotal(profitAllotInfoList.getTotal());
        rebatedMonthVoPage.setRecords(rebatedMonthVoList);

        return rebatedMonthVoPage;
    }

    @Override
    public Page<RebatedMonthVo> getMonthRebatedlist(Long memberId, Date month, Integer start, Integer size) {
        //获取会员信息
        MemberBo member = memberBiz.selectById(memberId);
        if(null == member) {
            throw new WakaException("找不到该会员信息");
        }

        //获取所选月份的分销记录
        Page<ProfitAllotInfoVo> allotInfoVoList = saleProfitAllotBiz.getMonthRebatedPage(memberId, month, DistStatusEnum.Returned, start, size);

        //返回所选月份的分销信息列表
        RebatedMonthVo rebatedMonthVo = new RebatedMonthVo();
        if(Tools.collection.isNotEmpty(allotInfoVoList.getRecords())) {
            BigDecimal amtMonthTotal = new BigDecimal(0.00);
            String date = null;
            for(ProfitAllotInfoVo allotInfoVo : allotInfoVoList.getRecords()) {
                date = allotInfoVo.getReturnedTime();
                allotInfoVo.setAvator(QiniuUtil.getFullPath(allotInfoVo.getAvator()));
                amtMonthTotal = amtMonthTotal.add(allotInfoVo.getAmt());
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            try {
                rebatedMonthVo.setDate(dateFormat.parse(date));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            rebatedMonthVo.setAmtMonthTotal(amtMonthTotal);
            rebatedMonthVo.setProfitAllotInfoList(allotInfoVoList.getRecords());
        }

        //转换为分页格式，方便前端做分页，分页仍依据分销记录
        Page<RebatedMonthVo> rebatedMonthVoPage = new Page<>(allotInfoVoList.getCurrent(),allotInfoVoList.getSize());
        rebatedMonthVoPage.setTotal(allotInfoVoList.getTotal());
        List<RebatedMonthVo> rebatedMonthVoList = new ArrayList<>();
        rebatedMonthVoList.add(rebatedMonthVo);
        rebatedMonthVoPage.setRecords(rebatedMonthVoList);

        return rebatedMonthVoPage;
    }


    /**
     * 按月份分组排序
     * @param profitAllotInfoList 分销按月分类列表信息
     */
    private List<RebatedMonthVo> rebatedSort(List<ProfitAllotInfoVo> profitAllotInfoList) {
        //按月份分组
        Map<String,List<ProfitAllotInfoVo>> profitAllotInfoVo = profitAllotInfoList.stream().collect(Collectors.groupingBy(ProfitAllotInfoVo::getReturnedTime));

        List<RebatedMonthVo> rebatedMonthVoList = new ArrayList<>();
        for(List<ProfitAllotInfoVo> allotInfoVoList : profitAllotInfoVo.values()){
            BigDecimal amtMonthTotal = new BigDecimal(0.00);
            String date = null;
            for(ProfitAllotInfoVo allotInfoVo : allotInfoVoList) {
                date = allotInfoVo.getReturnedTime();
                allotInfoVo.setAvator(QiniuUtil.getFullPath(allotInfoVo.getAvator()));
                amtMonthTotal = amtMonthTotal.add(allotInfoVo.getAmt());
            }

            RebatedMonthVo rebatedMonthVo = new RebatedMonthVo();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            try {
                rebatedMonthVo.setDate(dateFormat.parse(date));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            rebatedMonthVo.setAmtMonthTotal(amtMonthTotal);
            rebatedMonthVo.setProfitAllotInfoList(allotInfoVoList);

            rebatedMonthVoList.add(rebatedMonthVo);
        }

        Collections.sort(rebatedMonthVoList, new Comparator<RebatedMonthVo>() {
            public int compare(RebatedMonthVo o1, RebatedMonthVo o2) {
                Date first = o1.getDate();
                Date second = o2.getDate();
                // 降序
                return second.compareTo(first);
            }
        });

        return rebatedMonthVoList;
    }
}
