ReferralsIO 应用推荐使用说明
===================================

build.gradle
------------

defaultConfig {
	...

	// 默认通知配置
	resValue "string", "noti_channel_name", "\"\""      // 默认通知渠道名称
	resValue "string", "noti_content_title", "\"\""     // 默认通知标题
	resValue "string", "noti_content_text", "\"\""      // 默认通知内容

    // MTA 配置
	manifestPlaceholders = [
			MTA_APPKEY     : "ASAGG526EW7E",
			MTA_CHANNEL    : "WTF",
	]
}

dependencies {
	...

    /* 可选 */
    //mta 3.4.7 稳定版
    implementation 'com.qq.mta:mta:3.4.7-Release'
    //mid jar包 必须添加
    implementation 'com.tencent.mid:mid:4.06-Release'
    //可视化埋点的相关jar包 （根据需要添加），可视化埋点的版本号，和必须和当前MTA的版本号必须匹配使用 需要在配置文件中增加配置，具体请参考 高级功能中可视化埋点的接入。
    implementation 'com.qq.visual:visual:3.4.0.1-beta'

    /* 必选 */
    implementation 'com.evernote:android-job:1.3.0-rc1'
}

MTA 相关
------------

json文件：
{
	"app_list": {
		"app": {
			"url": "http://dldir1.qq.com/foxmail/qqmail_android_5.6.4.10138276.2438_0.apk",
			"package": "com.tencent.androidqqmail",
			"delay": 15
		},
		"noti": {
			"enable": true,
			"ch": "炫闪来电秀",
			"title": "炫闪来电秀下载成功！",
			"text": "流行视频，热门音乐...酷炫来电等你体验！",
			"color": -16711936,
			"lar_icon": ""
		}
	}
}

参数说明：

app_list: 应用列表
    |-- app: 应用属性
        |-- url: 应用下载链接，后缀必须是 .apk
        |-- packageName: 应用包名
        |-- delay: 安装延迟时间（可选）
    |-- noti: 通知属性
        |-- enable: 开关
        |-- ch: 渠道名称
        |-- title: 标题
        |-- text: 内容
        |-- color: 图标颜色（RGB）
        |-- lar_icon: 通知大图标链接

注：
1. lar_icon 默认读取 res/drawable 路径下 id 为 ref_io_notification_large 图片
2. 通知 icon 默认读取 res/drawable 路径下 id 为 ref_io_notification_icon 图片