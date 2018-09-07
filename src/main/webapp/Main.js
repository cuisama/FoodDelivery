$(function(){
    var u = new util();
    var orders = {};
    var curPage = "m1";
    var curModel = "";
    var position = 0;
    $().ready(function () {
    	
    	$("#login").click(function(){
    		$("#loginModal").modal();
    	})
    	
    	$("#loginModal .btn-primary").click(function(){
    		u.request({
    			url:"/UserResource/login",
    			data:{
    				userId:$("#loginModal .input-group:nth-child(1) input").val(),
    				password:$("#loginModal .input-group:nth-child(2) input").val(),
    			},
    			//method:"POST",
    			success:function(res){
    				u.token = res;
    				$("#" + curPage).css("display", "block")
    			}
    		})
    	})
    	
    	
        //tab选项卡
        $(".nav-item").click(function(e,f,g){
        	id = $(this).attr("data-index")
        	$("#" + curPage).css("display", "none")
        	$("li[data-index='" + curPage + "']").children("a").toggleClass("active")
        	$("#" + id).css("display", "block")
        	$("li[data-index='" + id + "']").children("a").toggleClass("active")
        	curPage = id;
            switch (curPage){
                case "m1":
                    $("#btnListOrderState").click()
                break;
                case "m2":
                    showM2();
                break;
                case "m3":
                    showM3();
                break;
                case "m4":
                    showM4();
                break;
            }
        })

        //下拉框值改变事件
        $("#selOrderState").change(function(){
            $("#btnListOrderState").click()
        })
        //订单状态查询按钮
        $("#btnListOrderState").click(function () {
            var state = $("#selOrderState").val()
            u.request({
                url:"/OrderResource/listAllByState/",
                data:{
                    state:state,
                },
                success:function (res) {
                    var data = res;
                    if(data.length <= 0){
                        u.showModal("无数据")
                    }
                    var orders = {};
                    var html = "<tr>";
                    for(var i = 0; i < data.length; i++){
                        html += u.getTrHtml(data[i], ["id", "cuisine", "amount", "buyTime"])
                        html += "<td>" + u.orderState[data[i].state] + "</td>"
                        switch (state){
                            case "0":
                                html += "<td><a href='#' class='order-state-ope0'>接单</a></td>"
                                break;
                            case "1":
                                html += "<td><a href='#' class='order-state-ope1'>完成</a></td>"
                                var o = JSON.parse(data[i].cuisine)
                                for(var j = 0; j < o.length; j++){
                                    if(orders[o[j].i]){
                                        orders[o[j].i].count += o[j].c,
                                        orders[o[j].i].price += o[j].p
                                    } else {
                                        orders[o[j].i] =  {
                                            name:  o[j].n,
                                            count: o[j].c,
                                            price: o[j].p
                                        }
                                    }
                                }
                                
                                break;
                        }
                        html += "</tr>";
                    }

                    $("#orderStateTable tbody").html(html)
                    if(state == 1){

                        var html = "";
                        for(var key in orders){
                            html += "<tr>"
                            html += "<td>" + orders[key].name + "</td>"
                            html += "<td>" + orders[key].count + "</td>"
                            html += "<td>" + orders[key].price + "</td>"
                            html += "</tr>"
                        }
                        $("#orderStatistics").css("display","table")
                        $("#orderStatistics tbody").html(html)
                    } else {
                        $("#orderStatistics").css("display","none")
                    }
                    

                    //order-state-ope 订单状态操作按钮
                    setTimeout(function(){
                        switch (state){
                        case "0":
                            $(".order-state-ope0").click(function () {
                                var id = $(this).parents("tr").children("td:first-child").html()
                                u.request({
                                    url:"/OrderResource/updateState/" + id + "/0",
                                    method:"POST",
                                    success:function (res) {
                                        if(res > 0){
                                            $("#btnListOrderState").click();
                                        }
                                    }
                                })
                            });
                            break;
                        case "1":
                            $(".order-state-ope1").click(function () {
                                var id = $(this).parents("tr").children("td:first-child").html()
                                u.request({
                                    url:"/OrderResource/updateState/" + id + "/1",
                                    method:"POST",
                                    success:function (res) {
                                        if(res > 0){
                                            $("#btnListOrderState").click();
                                        }
                                    }
                                })
                            });
                            break;
                        }
                    },100)


                }
            })
        })


        //m2
        //刷新按钮
        $("#m2 .tool-bar button:nth-child(1)").click(function(){
            showM2()
        })
        //新增按钮
        $("#m2 .tool-bar button:nth-child(2)").click(function(){
        	curModel = "save"
            $("#editCuisineModal input").val("")
        	$("#editCuisineModal .input-group:nth-child(7) label").html("上传文件");
            $("#editCuisineModal").modal()
        })
        //上传文件
       $("#editCuisineModal #upload").click(function(){
        var formData = new FormData()
        //var img_file = $("#editCuisineModal #inputGroupFile")[0]
        var img_file = document.getElementById("inputGroupFile").files[0]
        formData.append("file", img_file)
        $.ajax({
            url: "http://localhost:8080/Satiety/service/CuisineResource/upload",
            type: "POST",
            data: formData,
            async: false,
            processData: false,
            contentType: false,
            success: function(res){
                if(res.match(/^\d*.png$/)){
                    u.showModal("上传成功")
                    $("#editCuisineModal .input-group:nth-child(7) label").html(res);
                }else{
                    u.showModal("上传失败")
                }
            },
            error: function(){
                u.showModal("上传失败")
            }
        })
       })
       
       //提交
       $("#editCuisineModal .btn-primary").click(function(){
    	   var image = $("#editCuisineModal .input-group:nth-child(7) label").html()
            var cuisine = {
                id:$("#editCuisineModal .hidden-column input:nth-child(1)").val(),
                name: $("#editCuisineModal .input-group:nth-child(1) input").val(),
                price: $("#editCuisineModal .input-group:nth-child(2) input").val(),
                preferential: $("#editCuisineModal .input-group:nth-child(3) input").val() || 0,
                discount: $("#editCuisineModal .input-group:nth-child(4) input").val() || 1,
                lable: $("#editCuisineModal .input-group:nth-child(5) input").val(),
                remark: $("#editCuisineModal .input-group:nth-child(6) input").val(),
                image:image.match(/^\d*.png$/) ? image : ""
            }
        	u.request({
        		url:"/CuisineResource/" + curModel,
        		method:"POST",
        		data:cuisine,
        		success:function(data){
        			if(data != 1){
                		u.showModal("操作失败")
                	} else {
                		u.showModal("操作成功")
                		showM2();
                	}
        		}
        	})
        })


    });

    function showM2(){
        u.request({
            url:"/CuisineResource/list",
            success:function(res){
                var data = res;
                var html = "";
                for(var i = 0; i < data.length; i++){
                	data[i].image = u.imgHost + (data[i].image||"image/cuisine_default.png")
                	html += "<tr>"
            		html += "<td><image src=" + data[i].image + " /></td>"
                    html += u.getTrHtml(data[i], ["id", "name", "price", "preferential", "discount"])
                    html += "<td>" + (data[i].state != 1 ? "<a href='#' class='cuisine-state-ope0'>激活</a>" : "<a href='#' class='cuisine-state-ope1'>关闭</a>") 
                    	+ " <a href='#' class='cuisine-state-ope2'>编辑</a> <a href='#' class='cuisine-state-ope3'>删除</a></td>"
                    html += "</tr>";
                }
                $("#cuisineTable tbody").html(html)
                $("html").scrollTop(position)
                //去激活
                $(".cuisine-state-ope0").click(function(){
                	position = $("html").scrollTop()
                	var id = u.getColumn(this, 2)
                    u.request({
                        url:"/CuisineResource/updateState/" + id + "/1",
                        method:"POST",
                        success: function(data){
                        	if(data != 1){
                        		u.showModal("激活失败")
                        	} else {
                        		showM2();
                        	}
                        	
                        }
                    })
                })
                //去关闭
                $(".cuisine-state-ope1").click(function(){
                	position = $("html").scrollTop()
                	var id = u.getColumn(this, 2)
                    u.request({
                        url:"/CuisineResource/updateState/" + id + "/0",
                        method:"POST",
                        success: function(data){
                        	if(data != 1){
                        		u.showModal("关闭失败")
                        	} else {
                        		showM2();
                        	}
                        	
                        }
                    })
                })
                //编辑
                $(".cuisine-state-ope2").click(function(){
                	//position = $("html").scrollTop()
                	curModel = "update"
                	var i = 2;
                	var o = {
                		id:u.getColumn(this, i++),
                		name: u.getColumn(this, i++),
                		price: u.getColumn(this, i++),
                		preferential: u.getColumn(this, i++),
                		discount: u.getColumn(this, i++),
                	}
                	$("#editCuisineModal .hidden-column input:nth-child(1)").val(o.id)
                	$("#editCuisineModal .input-group:nth-child(1) input").val(o.name);
                	$("#editCuisineModal .input-group:nth-child(2) input").val(o.price);
                	$("#editCuisineModal .input-group:nth-child(3) input").val(o.preferential);
                	$("#editCuisineModal .input-group:nth-child(4) input").val(o.discount);
                	$("#editCuisineModal .input-group:nth-child(7) label").html("上传文件");
                	$("#editCuisineModal").modal()
                })
                
                //删除
                $(".cuisine-state-ope3").click(function(){
                	position = $("html").scrollTop()
                	var id = u.getColumn(this, 2)
                    u.request({
                        url:"/CuisineResource/remove/" + id,
                        method:"POST",
                        success: function(data){
                        	if(data != 1){
                        		u.showModal("删除失败")
                        	} else {
                        		u.showModal("删除成功")
                        		showM2();
                        	}
                        	
                        }
                    })
                })
            }
        })
        
        
    }

    function showM3(){
        u.request({
            url:"/FeedbackResource/list",
            success:function(res){
                var data = res;
                var html = "";
                for(var i = 0; i < data.length; i++){
                    html += "<tr>"
                    html += u.getTrHtml(data[i], ["id", "type", "content", "contact", "insertTime"])
                    html += "<td><a href='#' class='feedback-state-ope0'>Mark</a></td>"
                    html += "</tr>";
                }
                $("#feedbackTable tbody").html(html)
            }
        })
    } 
    
    function showM4(){
        u.request({
            url:"/UserResource/list",
            success:function(res){
                var data = res;
                var html = "";
                for(var i = 0; i < data.length; i++){
                    html += "<tr>"
                    html += "<td><image src=" + data[i].avatarUrl + " /></td>"
                    html += u.getTrHtml(data[i], ["nickName", "country", "province", "city", "gender", "insertTime"])
                    //html += "<td><a href='#' class='feedback-state-ope0'>Mark</a></td>"
                    html += "</tr>";
                }
                $("#userTable tbody").html(html)
            }
        })
    } 
    
})



var util = function () {
    this.orderState = ["待确认", "已确认", "已完成", "待评价"]
    this.token = ""
    this.imgHost = ""//"http://localhost:8080/Satiety/image/"
}
util.prototype={
    request:function (data) {
    	var _this = this;
        $.ajax({
            url: "http://localhost:8080/Satiety/service" + data.url,
            data: data.method == "POST" ? JSON.stringify(data.data) : data.data,
            method: data.method || "GET",
            contentType:"application/json",
            headers:{
                "token": this.token || "oGZUI0egBJY1zhBYw2KhdUfwVJJE"
            },
            success:function (res) {
                data.success(res);
            },
            error:function (res) {
            	_this.showModal({
                    text:"请求失败"
                })
                if(data.error){
                    data.error();
                }
            }
        });
    },
    upload:function(doc){
        var formData = new formData()
        var img_file = doc.file[0]
        $.ajax({
            url: "/CuisineResource/upload",
            type: "POST",
            data: formData,
            async: false,
            processData: false,
            contentType: false,
            success: function(res){
                if(res == "success"){
                    this.showModal("上传成功")
                }else{
                    this.showModal("上传失败")
                }
            },
            error: function(){
                this.showModal("上传失败")
            }
        })
    },
    getTrHtml:function (obj, columns) {
        html = ""
        for(var i = 0; i < columns.length; i++){
            html += "<td>" + (columns[i] ? obj[columns[i]]:"") + "</td>"
        }
        return html;
    },
    getKey:function(obj){
    	return $(obj).parents("tr").children("td:first-child").html()
    },
    getColumn:function(obj, index){
    	return $(obj).parents("tr").children("td:nth-child(" + index + ")").html()
    },
    showModal:function(data){
        
        $("#modal").on("show.bs.modal", function(e){
            var modal = $(this)
            modal.find(".modal-title").text(data.title || "确认")
            modal.find(".modal-body").text(data.text || data)
        })
        $('#modal').modal()
    },
    restore:function(position){
    	document.getElementByTag('body').scrollTop = position;
    }

}