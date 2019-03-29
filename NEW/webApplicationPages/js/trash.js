$(document).ready(search);

var sumData;

var flag = 1;
var PRI = new Array();

 function search () {
 	flag=1;
	var json={ "username" : $("#username").val(), "keyword" : $("#keyword").val() };
	$.post("../system/FileServlet?action=trashSearch", json, function(data) {
		
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





function show() {
	$("#tab").html("<tr><th>id</th><th>name</th><th>creator</th><th>operations</th></tr>");

	
	for (var i = 0; i < sumData.length; i++) {
		console.log("sumData: "+sumData[i].id+sumData[i].fName);
		PRI[sumData[i].id]=sumData[i].pri;
		s = "<tr>"+
				"<td class='id'>" + sumData[i].id + "</td>" +
				"<td>" + sumData[i].fName + "</td>" +
				"<td>" + sumData[i].uName + "</td>" +
				"<td><a class='" + i + "' href = 'javascript:void(0);' onclick ='totalDel(this)'>delete</a>  " +
				"<a class='"+ i +"'href = 'javascript:void(0);' onclick='recover(this)'>recover</a></td>"
			"</tr>";
		$("#tab").append(s);
	}
}


function totalDel(source){
	var className = source.getAttribute("class");
	var id =  document.getElementById("tab").rows[parseInt(className) + 1].cells[0].innerHTML;
	
	
	var json={"id" : id};
	$.post("../system/FileServlet?action=totallyDel", json, function(data) {
		var obj = JSON.parse(data);
		if(obj.ret === "0"){
			alert("File delete totally.");
			$('.newFile').hide();
			if(flag == 1){
				search();
			}
		} else {
			alert(obj.msg);
		}
	});
}

function recover(source){
	var className = source.getAttribute("class");
	var id =  document.getElementById("tab").rows[parseInt(className) + 1].cells[0].innerHTML;
	var fname =  document.getElementById("tab").rows[parseInt(className) + 1].cells[1].innerHTML;
	console.log("fname: "+fname);
	var pri = PRI[id];
	var json={"fname" : fname,"pri" : pri,"id" : id};
	$.post("../system/FileServlet?action=recover",json,function(data){
		var obj = JSON.parse(data);
		if(obj.ret =="0"){
			alert(obj.msg);
			$('.newFile').hide();
			if(flag == 1){
				search();
			}
		}else{
			alert(obj.msg);
		}
	});
}







