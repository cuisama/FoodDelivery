$(function(){
    var u = new util();
    var orders = {};
    var curPage = "m1";
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
        //


    });

    function showM2(){
        u.request({
            url:"/CuisineResource/list",
            success:function(res){
                var data = res;
                var html = "";
                for(var i = 0; i < data.length; i++){
                	html += "<tr>"
                    html += u.getTrHtml(data[i], ["id", "name", "price", "preferential", "discount"])
                    html += "<td>" + (data[i].state != 1 ? "<a href='#' class='cuisine-state-ope0'>激活</a>" : "<a href='#' class='cuisine-state-ope1'>关闭</a>") + " <a href='#' class='cuisine-state-ope2'>编辑</a></td>"
                    html += "</tr>";
                }
                $("#cuisineTable tbody").html(html)

                //去激活
                $(".cuisine-state-ope0").click(function(){
                	var id = u.getKey(this)
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
                	var id = u.getKey(this)
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
                	var o = {
                		id:u.getColumn(this, 1),
                		name: u.getColumn(this, 2),
                		price: u.getColumn(this, 3),
                		preferential: u.getColumn(this, 4),
                		discount: u.getColumn(this, 5),
                	}
                	$("#editCuisineModal .hidden-column input:nth-child(1)").val(o.id)
                	$("#editCuisineModal .input-group:nth-child(1) input").val(o.name);
                	$("#editCuisineModal .input-group:nth-child(2) input").val(o.price);
                	$("#editCuisineModal .input-group:nth-child(3) input").val(o.preferential);
                	$("#editCuisineModal .input-group:nth-child(4) input").val(o.discount);
                	$("#editCuisineModal").modal()
                })
            }
        })
        
        $("#editCuisineModal .btn-primary").click(function(){
            var cuisine = {
                id:$("#editCuisineModal .hidden-column input:nth-child(1)").val(),
                name: $("#editCuisineModal .input-group:nth-child(1) input").val(),
                price: $("#editCuisineModal .input-group:nth-child(2) input").val(),
                preferential: $("#editCuisineModal .input-group:nth-child(3) input").val(),
                discount: $("#editCuisineModal .input-group:nth-child(4) input").val(),
            }
        	u.request({
        		url:"/CuisineResource/update",
        		method:"POST",
        		data:cuisine,
        		success:function(data){
        			if(data != 1){
                		u.showModal("更新失败")
                	} else {
                		showM2();
                	}
        		}
        	})
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
}
util.prototype={
    request:function (data) {
    	var _this = this;
        $.ajax({
            url: "http://localhost:8080/Satiety/service" + data.url,
            data: data.data,//data.method == "POST" ? JSON.stringify(data.data) : data.data,
            method: data.method || "GET",
            contentType:"application/json",
            headers:{
                "token": this.token
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

}