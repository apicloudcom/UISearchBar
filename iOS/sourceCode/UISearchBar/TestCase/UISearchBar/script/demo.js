/*用于自动生成演示页面.*/
var Demo = {
	/**
	* 构造方法
	* 
	* @param	jsonPath		用来初始化测试页面的json文件.默认为网页同名同目录json文件.
	* @param	targetElement	目标元素.demo界面将被添加为此元素的子节点.默认为body元素.
	*
	* @return	实例对象.
	*
	*/
	"createNew": function(jsonPath, targetElement, moduleName){
		/*jsonPath文件的路径.默认未网页同名同目录json文件.*/
		var jsonPath = jsonPath || (function(){
			var a = location.href;
			var b = a.split("/");
			var c = b.slice(b.length-1, b.length).toString(String).split(".");
			var pageName = c.slice(0, 1);
			var jsonPath =  pageName + '.json';
			return jsonPath;
		}());
		
		/* demo 模块添加到哪个元素下. */
		var targetElement = targetElement || $('body')[0];

		
		// ------------------------------------------------
		
		/**
		* 生成包含代码块的行
		*
		* @code	string	要高亮显示的代码.
		*
		* @return element	代码块节点.
		*/
		function createCodeBlockRow(code){
			var code = code || '';
			var codeStr = "<div class='row'><div class='col-xs-12'><pre><code class='javascript'>" + code + "</code></pre></div></div>";
			return codeStr;
		};
		
		// -----------------------------------------------
		
		/**
		* 生成包含按钮组的行
		* 
		* 可选的按钮类型: default(默认), primary, success, info, warning, danger, link.
		*
		* @buttonGroupInfo	array	包含按钮组各个按钮的配置信息:如按钮类型, 事件响应函数等.事件响应函数仅需提供函数名.按钮组信息结构类似于:
		[{"text": "按钮1", "type": "success","click" : "函数名1"}, 
		  {"text": "按钮2", "type": "info","click" : "函数名2"}, 
		  {"text": "按钮3", "type": "warning","click" : "函数名3"}]
		
		* @return	element	按钮组节点.
		*/
		function createButtonGroupRow(buttonGroupInfo){
			var buttonGroupInfo = buttonGroupInfo || [];
			var buttonGroupStr = "<div class='row'><div class='col-xs-12 btn-group btn-group-justified diy-btn-group-test'>";
			$.each(buttonGroupInfo, function(index, buttonInfo){
				var text = buttonInfo.text || "按钮" + index;
				var type = buttonInfo.type || "default";
				var click = buttonInfo.click || ""; 
				var btnId = buttonInfo.id || "";
				
				var buttonStr = "<div class='btn-group'><button  id='" + btnId + "' tapmode='' type='button' onclick='" + click + "()' class='btn btn-" + type + "'>" + text + "</button></div>";
				
				buttonGroupStr += buttonStr;
			});
			buttonGroupStr += "</div></div>";
			
			return buttonGroupStr;
		};
		
		// ------------------------------------------------------
		
		/**
		* 生成包含输入框组的行.
		*
		* 可选的输入框类型: text(默认), nunber.
		*
		* @inputGroupInfo	array	包含各个输入框组的信息.按钮组信息的结构类似于:
			[{"label":"标签1",
			  "type": "text",
			  "id" : "id1",
			  "placeholder": "占位符1",
			  "disabled": "disabled",
			  "value": "值1"},
			{"label":"标签2",
			  "type": "number",
			  "id" : "id2",
			  "placeholder": "占位符2",
			  "value": "2"}
			] 
		* @return element	输入框组节点.
		*
		*/
		function createInputGroupRow(inputGroupInfo){
			var inputGroupInfo = inputGroupInfo || [];
			var inputGroupCount = inputGroupInfo.length || 1;
			var inputGroupWidth = 12 / inputGroupCount;
			
			var inputGroupStr = "<div class='row'>";
			
			$.each(inputGroupInfo, function(index, inputInfo){
				var label = inputInfo.label || "标签" + index;
				var type = inputInfo.type || "text";
				var id = inputInfo.id || "";
				var placeholder = inputInfo.placeholder || "";
				var disabled = (inputInfo.disabled) ? "disabled" : "";
				var value = inputInfo.value || "";
				
				var inputStr = "<div class='col-xs-" + inputGroupWidth + "'><div class='input-group'><span class='input-group-addon'>" + label + "</span><input type='" + type + "' class='form-control' placeholder='" + placeholder + "' id='" + id + "' " + disabled + " value='" +value+ "' /></div></div>";
				inputGroupStr += inputStr;
			});
			
			inputGroupStr += '</div>';
			
			return inputGroupStr;
		};
		
		// ---------------------------------------------------------
		
		/**
		* 生成一个内容块.
		*
		* @blockInfo    object	存储标题和内容块内各个行的数据.目前支持的行类型包括:codeBlock,buttonGroup,inputGroup.一个blocKInfo结构类似于:
			{
			"title": "内容",
			"data":[{"type": "codeBlock",
					"data": "int a = 42; // 代码如诗"},
					{"type": "buttonGroup",
			 		"data": [{"text": "按钮1", "type": "success","click" : "函数名1"}, 
		  					{"text": "按钮2", "type": "info","click" : "函数名2"}, 
		  					{"text": "按钮3", "type": "warning","click" : "函数名3"}]},
					{"type": "inputGroup",
			 		"data": [{"label":"标签1",
			 	 			"type": "text",
			  				"id" : "id1",
			  				"placeholder": "占位符1",
			  				"disabled": "disabled",
			 	 			"value": "值1"},
							{"label":"标签2",
			  				"type": "number",
			  				"id" : "id2",
			  				"placeholder": "占位符2",
			  				"value": "2"}
			] }]}
		*
		* @return	string	包含内容块的html字符串.
		*/
		function createContentBlock(blockInfo){
			var title = blockInfo.title || "内容块";
			var blockData = blockInfo.data || [];
			
		    var contentBlockStr = "<div class='row'><div class='col-xs-12'><h3>" + title + "</h3>";
			
			$.each(blockData, function(index, rowInfo){
				var rowType = rowInfo.type || "";
				var rowData = rowInfo.data || false; 
				
				var rowStr = "";
				
				if("codeBlock" === rowType)
				{	
					rowStr = createCodeBlockRow(rowData);
				}	
				
				if("buttonGroup" === rowType)
				{
					rowStr = createButtonGroupRow(rowData);
				}
				
				if("inputGroup" === rowType)
				{
					rowStr = createInputGroupRow(rowData);
				}
				
				contentBlockStr += rowStr;
			});
			
			contentBlockStr += "</div></div>";
			
			return contentBlockStr;
		};
		
		// ----------------------------------------------------------
		
		/**
		* 生成一个完整的demo模块.
		*  
		* @demoData	object	demo模块的数据,包含标题和各内容模块的数据.示例如下:
		{"title":"日历演示",
		"data": [{
			"title": "内容",
			"subTitle": "子标题",
			"data":[{"type": "codeBlock",
					"data": "int a = 42; // 代码如诗"},
					{"type": "buttonGroup",
			 		"data": [{"text": "按钮1", "type": "success","click" : "函数名1"}, 
		  					{"text": "按钮2", "type": "info","click" : "函数名2"}, 
		  					{"text": "按钮3", "type": "warning","click" : "函数名3"}]},
					{"type": "inputGroup",
			 		"data": [{"label":"标签1",
			 	 			"type": "text",
			  				"id" : "id1",
			  				"placeholder": "占位符1",
			  				"disabled": "disabled",
			 	 			"value": "值1"},
							{"label":"标签2",
			  				"type": "number",
			  				"id" : "id2",
			  				"placeholder": "占位符2",
			  				"value": "2"}
			] }]}]}
		* 
		* @return	string	包含完整demo内容的字符串.
		*/
		function createDemoBlock(demoData){
			var title = demoData.title || "演示";
			var subTitle = demoData.subTitle || "";
			var contentBlockData = demoData.data || [];
			
			if("" !== subTitle){
				subTitle = "<small>" + subTitle + "</small>";
			}
			
			var demoBlockStr = "<div class='container-fluid'><div class='page-header'><h2>" + title + subTitle + "</h2></div>";
			
			$.each(contentBlockData, function(index, contentBlockInfo){
				var contentBlockStr = createContentBlock(contentBlockInfo);
				
				demoBlockStr += contentBlockStr;
			});
			
			demoBlockStr += "</div>";
			
			return demoBlockStr;
		};
		
		// ---------------------------------------------------------
		
		/**
		* 生成一个完整的demo模块.
		*  
		* @mutibleDemoData	array	包含多个demo模块的数据.
		* 
		* @return	string	包含完整多个demo内容的字符串.
		*/
		function createMutipleDemoBlock(mutipleDemoData){
			if(false === (mutipleDemoData instanceof Array)){
				mutipleDemoData = [mutipleDemoData];
			}
			
			var mutipleDemoStr = "";
			
			$.each(mutipleDemoData, function(index, demoData){
				mutipleDemoStr += createDemoBlock(demoData);
			});
			return mutipleDemoStr;
		}
		
		// ----------------------------------------------------------
		
		/**
		* 显示完整示例模块.
		*
		* @callback	function	显示示例模块之后执行的回调方法.
		*						两个参数:ret,{"json": "json数据", "status": "success或fault"};
		*						err,{"message": "错误信息"}.
		*
		*/
		function show(callback){
			var ret = {"json": {}, "status": "success"};
			var err = {"message": "没有错误"};
			
			/* 桌面测试时,需特殊处理json文件的路径,以保证最新. */
			if("undefined" === typeof(api)){
				jsonPath += "?" + new Date();
			}
			
			$.getJSON(jsonPath, function(data){
				ret["json"] = data;
				
				var demoNode = $(createMutipleDemoBlock(data));
				
				demoNode.appendTo(targetElement);
				
				if('undefined' !== typeof(api)){ // 优化按钮点击.
				    api.parseTapmode();
				}
				
				if('undefined' !== typeof(hljs)){ // 代码高亮.
					$('pre code').each(function(i, block) {
					    hljs.highlightBlock(block);
					  });
				}
				
				/* 将模块添加为demo的一个属性. */
				var moduleName = data["module"];
				if("undefined" !== typeof(moduleName) && "undefined" !== typeof(api)){
					try{
						demo["module"] = api.require(moduleName);
					}catch(e){
						var message = "\"" + moduleName + "\"模块不存在";
						
						ret["status"] = "error";
						err["message"] = message;
					}
				}
				
				if('function' === typeof(callback)){
					callback(ret, err);
				}
				
			});
		}
		
		// -------------------------------------------------------

		/* 返回自定义的实例对象.可以在此处控制函数或属性的访问权限.*/
		var demo = {};
	
		if("undefined" !== typeof(api)){ // 真机环境下,直接深度复制api,以用demo替代api进行各种操作.
			demo = this.clone(api);
		}
		
		// 公开的方法和属性
		var pub = {
			'createCodeBlockRow': createCodeBlockRow,
			'createButtonGroupRow': createButtonGroupRow,
			'createInputGroupRow': createInputGroupRow,
			'createContentBlock': createContentBlock,
			'createDemoBlock' : createDemoBlock,
			'show': show
		};
		
		for(var key in pub){
			demo[key] = pub[key];
		}
			
		return demo;
	},
	"clone": function(obj){
		var objClone;
		if (obj.constructor == Object){
			objClone = new obj.constructor();
		}else{
			objClone = new obj.constructor(obj.valueOf());
		}
		// objClone = new obj.constructor(obj.valueOf());
		
		for(var key in obj){
			if ( objClone[key] != obj[key] ){
				if ( typeof(obj[key]) == 'object' ){
					objClone[key] = arguments.callee(obj[key]);
				}else{
					objClone[key] = obj[key];
				}
			}
		}
		objClone.toString = obj.toString;
		objClone.valueOf = obj.valueOf;
		return objClone;
	}
};

