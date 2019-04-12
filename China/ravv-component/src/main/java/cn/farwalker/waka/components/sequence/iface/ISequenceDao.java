/**
 * 
 */
package cn.farwalker.waka.components.sequence.iface;

import cn.farwalker.waka.components.sequence.SequenceRange;

/**
 * @author mingxing.fmx
 *
 */
public interface ISequenceDao {

	public int MIN_STEP = 1;
    public int MAX_STEP = 10000;
    /**步长*/
    public static final int DEFAULT_STEP = 200;
    /**重试次数*/
    public int DEFAULT_RETRY_TIMES = 150;

    /**序列所在的表名*/
    public String D_TABLE_NAME = "CW_SEQUENCE";
    /**存储序列名称的列名*/
    public String D_COLUMN_NAME = "NAME";
    /**存储序列值的列名*/
    public String D_COLUMN_VALUE = "VAL"; 
	/**
	 * 取得下一个可用的序列区间
	 *
	 * @param name 序列名称
	 * @return 返回下一个可用的序列区间
	 * @throws SequenceException
	 */
	public SequenceRange nextRange(String name,int step) ;
	 
}
