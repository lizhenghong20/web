package cn.farwalker.ravv.youtube.livevideo;
import java.util.List;

import cn.farwalker.ravv.admin.youtube.AdminYoutubeService;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoBo;
import cn.farwalker.waka.core.JsonResult;

/**
 * 直播视频列表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/youtube/livevideo")
public class LiveVideoController{
    private final static  Logger log =LoggerFactory.getLogger(LiveVideoController.class);

    @Autowired
    private AdminYoutubeService adminYoutubeService;

    /**创建记录
     * @param vo null<br/>
     */
    @RequestMapping("/create")
    public JsonResult<?> doCreate(@RequestBody YoutubeLiveVideoBo vo){
        try {
            if (vo == null) {
                return JsonResult.newFail("vo不能为空");
            }
            return JsonResult.newSuccess(adminYoutubeService.createLiveVideo(vo));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    /**删除记录
     * @param id null<br/>
     */
    @RequestMapping("/delete")
    public JsonResult<Boolean> doDelete(@RequestParam Long id){
        try{
            if (id == null) {
                throw new WakaException("id不能为空");
            }
            return JsonResult.newSuccess(adminYoutubeService.deleteLiveVideo(id));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    /**取得单条记录
     * @param id null<br/>
     */
    @RequestMapping("/get")
    public JsonResult<YoutubeLiveVideoBo> doGet(@RequestParam Long id){
        try{
            if (id == null) {
                throw new WakaException("vo不能为空");
            }
            return JsonResult.newSuccess(adminYoutubeService.getOneLiveVideo(id));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    /**列表记录
     * @param query 查询条件<br/>
     * @param start 开始行号<br/>
     * @param size 记录数<br/>
     * @param sortfield 排序(+字段1,-字段名2)<br/>
     * @param anchorMemberId 主播会员id
     */
    @RequestMapping("/list")
    public JsonResult<Page<YoutubeLiveVideoBo>> doList(@RequestBody List<QueryFilter> query,Integer start,Integer size,
                                                       String sortfield, Long anchorMemberId){
        try{
//            if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//            }
            return JsonResult.newSuccess(adminYoutubeService.getListLiveVideo(query, start, size, sortfield, anchorMemberId));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    /**修改记录
     * @param vo null<br/>
     */
    @RequestMapping("/update")
    public JsonResult<Boolean> doUpdate(@RequestBody YoutubeLiveVideoBo vo){
        try {
            if (vo == null) {
                return JsonResult.newFail("vo不能为空");
            }
            return JsonResult.newSuccess(adminYoutubeService.updateLiveVideo(vo));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }

    }
}