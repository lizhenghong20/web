package cn.farwalker.ravv.service.flash.sale.model;

import cn.farwalker.ravv.common.constants.FlashSaleCategoryStatusEnum;
import lombok.Data;

/**
 * Created by asus on 2018/12/26.
 */
@Data
public class FlashSaleCategoryVo extends FlashSaleBo {
    FlashSaleCategoryStatusEnum categoryStatus;
}
