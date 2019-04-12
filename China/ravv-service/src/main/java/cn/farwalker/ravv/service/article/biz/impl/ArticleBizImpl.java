package cn.farwalker.ravv.service.article.biz.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.farwalker.ravv.service.article.biz.IArticleBiz;
import cn.farwalker.ravv.service.article.dao.IArticleDao;
import cn.farwalker.ravv.service.article.model.ArticleBo;

/**
 * 文章<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class ArticleBizImpl extends ServiceImpl<IArticleDao,ArticleBo> implements IArticleBiz{
}