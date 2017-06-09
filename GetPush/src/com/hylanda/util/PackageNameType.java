package com.hylanda.util;


public enum PackageNameType {

	/* XINHUAWANG("com.xinhuanews","�»���"){},
	XINHUAWANG2("com.xinhuanews","�»���"){},
	XINHUAWANG3("com.xinhuanews","�»���"){},; */
	JINRITOUTIAO("com.ss.android.article.news","����ͷ��"),
	WANGYIXINWEN("com.netease.newsreader","��������"),
	SOUHUXINWEN("com.sohu.newsclient","�Ѻ�����"),
	RENMINRIBAO("com.peopledailychina","�����ձ�"),
	XINHUASHE("net.xinhuamm.mainclient","�»���"),
	ZAKERNEWS("com.myzaker.ZAKER_Phone","ZAKER����"),
	FENGHUANGXINWEN("com.ifeng.news2","�������"),
	YINGSHIXINWEN("com.cntnews","��������"),
	TIANTIANKUAIBAO("com.tencent.reading","����챨"),
	XINLANGXINWEN("com.sina.news","��������"),
	XINWEN360("com.so.news","360����"),
	GUOWUYUAN("com.gov.cn","����Ժ"),
	YIDIANZIXUN("com.hipu.yidian","һ����Ѷ"),
	BAIDUXINWEN("com.baidu.news","�ٶ�����"),
	HEXINWEN("com.newspaper.client","������"),
	NANFANGPLUS("com.nfdaily.nfplus","�Ϸ�PLUS"),
	GUANGMINGRIBAO("cn.gmw.cloud","�����ձ�"),
	XINWENZIXUN("com.yidian.xiaomi","������Ѷ"),
	XUANWEN("com.cplatform.xhxw","���ţ��»�����"),
	RENMINXINWEN("com.peipleClients.views","��������"),
	PENGPAIXINWEN("com.wondertek.paper","��������"),
	SHIKEXINWEN("com.rednet.moment","ʱ������"),
	DONGFANGTOUTIAO("com.songheng.eastnews","����ͷ��"),
	BEIJINGSHIJIAN("com.btime.bjtime","����ʱ��"),
	LIZHIXINWEN("com.jsbc.lznews","��֦����"),
	JIKEXINWEN("com.jike.news","��������"),
	HUANQIUTIME("com.huanqiu.news","����TIME"),
	CANKAOXIAOXI("com.cutt.zhiyue.android.app236492","�ο���Ϣ"),
	;
	
	private String packageName;
	private String mediaName;
	
	private PackageNameType(String packageName,String mediaName)
	{
		this.packageName = packageName;
		this.mediaName = mediaName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	
	public static String getMediaName(String packageName)
	{
		for (PackageNameType pnt:PackageNameType.values()) {
			if (pnt.packageName.equalsIgnoreCase(packageName)) {
				return pnt.mediaName;
			}
		}
		return null;
	}
	
}
