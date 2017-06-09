package com.hylanda.util;


public enum PackageNameType {

	/* XINHUAWANG("com.xinhuanews","新华网"){},
	XINHUAWANG2("com.xinhuanews","新华网"){},
	XINHUAWANG3("com.xinhuanews","新华网"){},; */
	JINRITOUTIAO("com.ss.android.article.news","今日头条"),
	WANGYIXINWEN("com.netease.newsreader","网易新闻"),
	SOUHUXINWEN("com.sohu.newsclient","搜狐新闻"),
	RENMINRIBAO("com.peopledailychina","人民日报"),
	XINHUASHE("net.xinhuamm.mainclient","新华社"),
	ZAKERNEWS("com.myzaker.ZAKER_Phone","ZAKER新闻"),
	FENGHUANGXINWEN("com.ifeng.news2","凤凰新闻"),
	YINGSHIXINWEN("com.cntnews","央视新闻"),
	TIANTIANKUAIBAO("com.tencent.reading","天天快报"),
	XINLANGXINWEN("com.sina.news","新浪新闻"),
	XINWEN360("com.so.news","360新闻"),
	GUOWUYUAN("com.gov.cn","国务院"),
	YIDIANZIXUN("com.hipu.yidian","一点资讯"),
	BAIDUXINWEN("com.baidu.news","百度新闻"),
	HEXINWEN("com.newspaper.client","和新闻"),
	NANFANGPLUS("com.nfdaily.nfplus","南方PLUS"),
	GUANGMINGRIBAO("cn.gmw.cloud","光明日报"),
	XINWENZIXUN("com.yidian.xiaomi","新闻资讯"),
	XUANWEN("com.cplatform.xhxw","炫闻（新华网）"),
	RENMINXINWEN("com.peipleClients.views","人民新闻"),
	PENGPAIXINWEN("com.wondertek.paper","澎湃新闻"),
	SHIKEXINWEN("com.rednet.moment","时刻新闻"),
	DONGFANGTOUTIAO("com.songheng.eastnews","东方头条"),
	BEIJINGSHIJIAN("com.btime.bjtime","北京时间"),
	LIZHIXINWEN("com.jsbc.lznews","荔枝新闻"),
	JIKEXINWEN("com.jike.news","即刻新闻"),
	HUANQIUTIME("com.huanqiu.news","环球TIME"),
	CANKAOXIAOXI("com.cutt.zhiyue.android.app236492","参考消息"),
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
