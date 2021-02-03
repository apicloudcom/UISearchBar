# **概述**

可记录搜索历史的搜索模块（内含iOS和Android）

APICloud 的 UISearchBar 模块是一个搜索模块，封装了一个可自定义的 window 页面，开发者只需要简单配置相应的参数即可打开一个原生开发的搜索页。但是由于本模块 UI 布局界面为固定模式，不能满足日益增长的广大开发者对搜索模块样式的需求。因此，广大原生模块开发者，可以参考此模块的开发方式、接口定义等开发规范，或者基于此模块开发出更多符合产品设计的新 UI 布局的模块，希望此模块能起到抛砖引玉的作用。

# **模块接口文档**

<p style="color: #ccc; margin-bottom: 30px;">来自于：APICloud 官方</p>

<div class="outline">

[open](#open)
[close](#close)
[setText](#setText)
[clearHistory](#clearHistory)

</div>

# **概述**

UISearchBar 是一个独立的搜索页面，相当于打开一个 window 。模块预览图如下：
![图片说明](http://docs.apicloud.com/img/docImage/searchBar.jpg)

本模块可实现的主要功能有：

1，设置输入框占位提示文字
    
  可通过 open 接口的 placeholder 参数配置

2，设置搜索历史记录条数
    
  可通过 open 接口的 historyCount 参数配置

3，设置历史记录存储位置
    
  可通过 open 接口的 dataBase 参数配置

4，设置搜索页面样式
    
  可通过 open 接口的 styles 参数配置

5，清空历史记录
    
  可通过 clearHistory 接口实现

***本模块源码已开源，地址为：https://github.com/apicloudcom/UISearchBar***

## 模块接口

<div id="open"></div>

# **open**

打开搜索页面

open({params}, callback(ret))

## params

placeholder：

- 类型：字符串
- 描述：（可选项）搜索框的占位提示文本
- 默认值：'请输入搜索关键字'

historyCount：

- 类型：数字
- 描述：（可选项）历史记录条数
- 默认值：10

dataBase：

- 类型：字符串
- 描述：（可选项）历史记录存储库名，以区分同一个 app 多个不同页面的数据
- 默认值：UISearchBarData

showRecordBtn：

- 类型：布尔
- 描述：（可选项）是否显示录音按钮
- 默认值：true

texts：

- 类型：JSON 对象
- 描述：（可选项）模块各部分的文本
- 内部字段：

```js
{
    cancelText: '取消',           //（可选项）字符串类型；取消按钮的文本；默认：'取消'  
    clearText: '清除搜索记录'     //（可选项）字符串类型；清除按钮的文本；默认：'清除搜索记录'
}
```

styles：

- 类型：JSON 对象
- 描述：（可选项）模块各部分的样式
- 内部字段：

```js
{
    navBar: {                               //（可选项）JSON对象；搜索框所在的导航条样式
        bgColor: '#FFFFFF',                 //（可选项）字符串类型；搜索框所在的导航条背景色，支持 rgb，rgba，#；默认：'#FFFFFF'
        borderColor: '#ccc'                 //（可选项）字符串类型；搜索框所在的导航条边框颜色，支持 rgb，rgba，#；默认：'#CCCCCC'
    },
    searchBox: {                            //（可选项）JSON对象；搜索框样式
        marginL:,                           //（可选项）数字类型；输入框距离屏幕左边的间距；默认：5.0
        width: ,                            //（可选项）数字类型；输入框的宽；默认：当前屏幕宽度的75%-marginL
        bgImg: '',                          //（可选项）字符串类型；搜索框的背景图片，要求本地路径（fs://、widget://）；默认：自带背景图片
        color: '#000',                      //（可选项）字符串类型；搜索框输入文本的颜色，支持 rgb，rgba，#；默认：'#000000'
        size: 16,                           //（可选项）数字类型；搜索框输入文本的字体大小；默认：16
        height: 44                          //（可选项）数字类型；搜索框的高度；默认：44
    },
    cancel: {                               //（可选项）JSON对象；取消按钮的样式
        width: ,                            //（可选项）数字类型；取消按钮的宽；默认：当前屏幕宽度的20%
        marginR:,                           //（可选项）数字类型；输入框距离屏幕左边的间距；默认：当前屏幕宽度的2.5%
        bg: 'rgba(0,0,0,0)',                //（可选项）字符串类型；取消按钮的背景，支持 rgb，rgba，#，图片路径（本地路径，支持fs://、widget://）；默认：'rgba(0,0,0,0)'
        color: '#D2691E',                   //（可选项）字符串类型；取消按钮的字体颜色，支持 rgb，rgba，#；默认：'#D2691E'
        size: 16                            //（可选项）数字类型；取消按钮的字体大小；默认：16
    },
    list: {                                 //（可选项）JSON对象；历史记录列表的样式
        color: '#696969',                   //（可选项）字符串类型；历史记录列表的文本颜色，支持 rgb，rgba，#；默认：'#696969'
        bgColor: '#FFFFFF',                 //（可选项）字符串类型；历史记录列表的背景色，支持 rgb，rgba，#；默认：'#FFFFFF'
        activeBgColor: '#eee',              //（可选项）字符串类型；历史记录列表按下时的背景色，支持 rgb，rgba，#；默认：'#EEEEEE'
        borderColor: '#eee',                //（可选项）字符串类型；历史记录列表的边框颜色，支持 rgb，rgba，#；默认：'#EEEEEE'
        size: 16                            //（可选项）数字类型；历史记录列表的字体大小；默认：16
    },
    clear: {                                //（可选项）JSON对象；清除历史记录按钮的样式
        color: '#000000',                   //（可选项）字符串类型；清除按钮的字体颜色，支持 rgb，rgba，#；默认：'#000000'
        bgColor: '#fff',                    //（可选项）字符串类型；清除按钮的背景色，支持 rgb，rgba，#；默认：'#FFFFFF'
        activeBgColor: '#eee',              //（可选项）字符串类型；清除按钮按下时的背景色，支持 rgb，rgba，#；默认：'#EEEEEE'
        borderColor: '#ccc',                //（可选项）字符串类型；清除按钮的下边框颜色，支持 rgb，rgba，#；默认：'#CCCCCC'
        size: 16                            //（可选项）数字类型；清除按钮的字体大小；默认：16
    }
}
```

## callback(ret)

ret：

- 类型：JSON 对象
- 内部字段：

```js
{
	eventType: 'record',        //字符串类型；交互事件类型
                                //取值范围：
                                //record（用户点击录音按钮）
                                //search（用户点击搜索按钮）
                                //history（用户点击历史记录）
                                //cancel（用户点击取消按钮）
	text: ''                    //字符串类型；返回用户要搜索的文本
}
```

## 示例代码

```js
var UISearchBar = api.require('UISearchBar');
UISearchBar.open({
	placeholder: '请输入搜索关键字',
	historyCount: 10,
	showRecordBtn: true,
	texts: {
		cancelText: '取消',
		clearText: '清除搜索记录'
	},
	styles: {
		navBar: {
			bgColor: '#FFFFFF',
			borderColor: '#ccc'
		},
		searchBox: {
			bgImg: '',
			color: '#000',
			height: 44
		},
		cancel: {
			bg: 'rgba(0,0,0,0)',
			color: '#D2691E',
			size: 16
		},
		list: {
			color: '#696969',
			bgColor: '#FFFFFF',
			borderColor: '#eee',
			size: 16
		},
		clear: {
			color: '#000000',
			borderColor: '#ccc',
			size: 16
		}
	}
}, function(ret, err) {
	if (ret) {
		alert(JSON.stringify(ret));
	} else {
		alert(JSON.stringify(err));
	}
});
```

## 可用性

iOS系统，Android系统

可提供的1.0.0及更高版本

<div id="close"></div>

# **close**

关闭搜索页面

close()

## 示例代码

```js
var UISearchBar = api.require('UISearchBar');
UISearchBar.close();
```

## 可用性

iOS系统，Android系统

可提供的1.0.0及更高版本

<div id="setText"></div>

# **setText**

设置搜索框的文字

setText({params})

## params

text：

- 类型：字符串
- 描述：（可选项）搜索框内的文字，若不传或传空则清空搜索框内容

## 示例代码

```js
var UISearchBar = api.require('UISearchBar');
UISearchBar.setText({
	text: '设置语音识别的文本'
});
```

## 可用性

iOS系统，Android系统

可提供的1.0.0及更高版本

<div id="clearHistory"></div>

# **clearHistory**

清空当前搜索历史记录

clearHistory()

## 示例代码

```js
var UISearchBar = api.require('UISearchBar');
UISearchBar.clearHistory();
```

## 可用性

iOS系统，Android系统

可提供的1.0.0及更高版本
