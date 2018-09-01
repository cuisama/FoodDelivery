/**
 * Created by cuizizhe on 2018/9/1.
 */
$(function(){
    var u = new util();
    $().ready(function () {

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
                    var html = "<tr>";
                    for(var i = 0; i < data.length; i++){
                        html += u.getTrHtml(data[i], ["id", "cuisine", "amount", "buyTime", "state"])
                        switch (state){
                            case "0":
                                html += "<td><a href='#' id='orderStateOpe0'>接单</a></td>"
                                break;
                            case "1":
                                html += "<td><a href='#'id='orderStateOpe1'>完成</a></td>"
                                break;
                        }
                        html += "</tr>";
                    }

                    $("#orderStateTable tbody").html(html)


                    //orderStateOpe 订单状态操作按钮
                    switch (state){
                        case "0":
                            $("#orderStateOpe0").click(function () {
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
                            $("#orderStateOpe1").click(function () {
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

                }
            })
        })



    });

    
    
    
})

var util = function () {
    
}
util.prototype={
    request:function (data) {

        $.ajax({
            url: "http://localhost:8080/FoodDelivery/service" + data.url,
            data: data.data,
            method: data.method || "GET",
            header:{
                "content-type":"application/json",
                "token": "oGZUI0egBJY1zhBYw2KhdUfwVJJE"
            },
            success:function (res) {
                data.success(res);
            },
            error:function (res) {
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
    }

}