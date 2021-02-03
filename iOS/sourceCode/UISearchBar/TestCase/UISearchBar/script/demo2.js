var apiready= function(){
	/* 重写window.alert,增强体验 */
	window.alert = function(str){
		api.alert({"title": "提示","msg":str, "buttons":["确定"] });
	}
	
	/* 为所有按钮添加tapmode属性, 消除 300ms 延迟. */
	$("button, a, input[type='button']").each(function(){
		if("undefined" === typeof($(this).attr("tapmode"))){
			$(this).attr("tapmode", "");
		}
	});

	api.parseTapmode();
}

// 代码高亮.
$('.panel pre').each(function(i, block) {
    // 代码高亮.
    hljs.highlightBlock(block);

    var codeBlock = $(block);
    // 使用 pre 的一个属性来保存原来的代码.
    codeBlock.attr("original", codeBlock.text());

    // 添加可编辑代码块.
    var textBlock = $("<textarea>");
    textBlock.val(codeBlock.text());
    textBlock.height(codeBlock.height());
    textBlock.width(codeBlock.width());
    codeBlock.after(textBlock);
    textBlock.hide();
});

// 为按钮添加点击事件.			  
$("[role=run], [role=edit], [role=restore]").click(function(e){

	// 获取按钮
	var btn = $(e.currentTarget);
	
	// 分析按钮作用
	if("undefined" === typeof(btn.attr("role"))){ // 没有角色信息,没必要继续往下分析了.
		return;
	}
	
	var role = btn.attr("role");
	
	// 定位到当前按钮所在面板
	var panel = $(e.currentTarget).closest(".panel");
	
	// 定位到代码块.
	var codeBlock = panel.find("pre");

    // 定位到可编辑代码块.
    var textBlock = panel.find("textarea");

	
	if("run" === role){ // 运行.
		// 获取要执行的代码
		var code = codeBlock.text();
	
		// 执行代码
        eval(code);
		
		return;
	}
	
	if("edit" === role){ // 编辑.
        var action = "edit";

        if("保存" === $(this).text()){
             action = "save"
        }

        if("edit" === action){
            $(this).text("保存");

            /* 编辑代码 */
            textBlock.val(codeBlock.text());
            codeBlock.hide();
            textBlock.show();
        }

        if("save" === action){
            $(this).text("编辑");

            /* 保存代码 */
            codeBlock.text(textBlock.val());
            codeBlock.show();
            textBlock.hide();
            hljs.highlightBlock(codeBlock[0]);
        }
	}
	
	if("restore" === role){ // 复原.
		codeBlock.text(codeBlock.attr("original"));
		hljs.highlightBlock(codeBlock[0]);
	}
});