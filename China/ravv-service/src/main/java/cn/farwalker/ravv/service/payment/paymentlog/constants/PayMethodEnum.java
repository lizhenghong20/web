package cn.farwalker.ravv.service.payment.paymentlog.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * PC/或者APP
 * @author Administrator
 *
 */
public enum PayMethodEnum implements IEnumJsons{
	APP("APP","APP"),PC("PC","PC");
	private final String key;
	private final String label;

	private PayMethodEnum(String key, String label) {
		this.key = key;
		this.label = label;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return getKey();
	}
}
