$(document).ready(search);

var sumData;

var flag = 1;

 // 页面加载，首先查询出所有的数据
 function search () {
 	flag=1;
	var json={ "username" : $("#username").val(), "keyword" : $("#keyword").val() };
	$.post("../system/FileServlet?action=search", json, function(data) {
		// 总数据
		sumData = jQuery.parseJSON(data);
		show();
	});
}
 
$("#btn_search").click(search);

$("#btn_clear").click(function() {
	$("#username").val("");
	$("#keyword").val("");	
	search();
});

function priSearch (){
	flag=2;
	$.post("../system/FileServlet?action=priSearch", null, function(data) {
		// 总数据
		sumData = jQuery.parseJSON(data);
		show();
	});
}

$("#btn_private").click(priSearch);



function show() {
	$("#tab").html("<tr><th>id</th><th>creator</th><th>name</th><th>operations</th></tr>");
	// <th>操作</th>
	for (var i = 0; i < sumData.length; i++) {
		s = "<tr>"+
				"<td class='id'>" + sumData[i].id + "</td>" +
				"<td>" + sumData[i].uName + "</td>" +
				"<td>" + sumData[i].fName + "</td>" +
				"<td><a class='" + i + "' href = 'javascript:void(0);' onclick ='down(this)'>download</a>　　" +
				"<a class='" + i + "' href = 'javascript:void(0);' onclick ='del(this)'>delete</a></td>" +
			"</tr>";
		$("#tab").append(s);
	}
}

function down(source){
	var className = source.getAttribute("class");
	var id =  document.getElementById("tab").rows[parseInt(className) + 1].cells[0].innerHTML;
	
	var json={"fid" : id};
	$.post("../system/FileServlet?action=download", json, function(data) {
		var obj = JSON.parse(data);
		
		var link = document.createElement("a");
        link.href = encodeURI(obj.fContent);
        link.style = "visibility:hidden";
        link.download = obj.fName;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
	});
}

function del(source){
	var className = source.getAttribute("class");
	var id =  document.getElementById("tab").rows[parseInt(className) + 1].cells[0].innerHTML;
	
	
	var json={"fId" : id};
	$.post("../system/FileServlet?action=delete", json, function(data) {
		var obj = JSON.parse(data);
		if(obj.ret === "0"){
			alert("file delete successfully");
			$('.newFile').hide();
			if(flag == 1){
				search();
			}else if(flag==2){
				priSearch();
			}
		} else {
			alert(obj.msg);
		}
	});
}







