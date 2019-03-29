$(document).ready(search);

var sumData;

var flag = 1;
var pri = 0;
 // search for files and list on the page
 function search () {
 	flag=1;
	var json={ "username" : $("#username").val(), "keyword" : $("#keyword").val() };
	$.post("../system/FileServlet?action=search", json, function(data) {
		// file list from server
		sumData = jQuery.parseJSON(data);
		pri=0;
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
		// private file list from server
		sumData = jQuery.parseJSON(data);
		pri=1;
		show();
	});
}

$("#btn_private").click(priSearch);



function show() {
	$("#tab").html("<tr><th>name</th><th>creator</th><th>operations</th></tr>");
	
	for (var i = 0; i < sumData.length; i++) {
		s = "<tr>"+
				//"<td class='id'>" + sumData[i].id + "</td>" +
				"<td>" + sumData[i].fName + "</td>" +
				"<td>" + sumData[i].uName + "</td>" +
				"<td><a class='" + i + "' href = 'javascript:void(0);' onclick ='down(this)'>download</a>　　" +
				"<a class='" + i + "' href = 'javascript:void(0);' onclick ='del(this)'>delete</a></td>" +
			"</tr>";
		$("#tab").append(s);
	}
}

function down(source){
	var className = source.getAttribute("class");
	var name =  document.getElementById("tab").rows[parseInt(className) + 1].cells[0].innerHTML;
	
	var json={"fname" : name , "pri" : pri};
	console.log("pri: "+json);
	$.post("../system/FileServlet?action=download", json, function(data) {
		var obj = JSON.parse(data);
		var link = document.createElement("a");
	    
		if(obj.ret=="0"){
			link.href = encodeURI(obj.fContent);
			link.style = "visibility:hidden";
        	link.download = obj.fName;
        	document.body.appendChild(link);
        	link.click();
        	document.body.removeChild(link);

		}
		else{
			alert(obj.msg);
		}
       
	});
}

function del(source){
	var className = source.getAttribute("class");
	var name =  document.getElementById("tab").rows[parseInt(className) + 1].cells[0].innerHTML;
	var json={"fname" : name , "pri" : pri};
	$.post("../system/FileServlet?action=delete", json, function(data) {
		var obj = JSON.parse(data);
		if(obj.ret === "0"){
			alert("file delete to bin");
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







