<html>
<meta charset="utf-8">
<%@page language="java" contentType="text/html;carset=UTF-8" pageEncoding="utf-8" %>
<script type="text/javascript" src="./jquery-3.5.1.min.js"></script>
<h2>国家红黑名单接口快速查询</h2>
<input type="text" name="tym" placeholder="请输入统一代码" id="tym"/>
<button id="button">查询</button>
处理结果：<span id="result"></span>
<div id="hidediv" style="display: none">
    <table id="tds" border="1">
        <tr>
            <th>统一代码</th>
            <th>纳入理由</th>
            <th>入库时间</th>
            <th>事项类型</th>
            <th>主体类型</th>
            <th>名单类型</th>
            <th>企业名称</th>
            <th>列入单位</th>
        </tr>
    </table>
</div>
<script>
    $("#button").click(function(){
        $("#hidediv").css('display','none');
        $.ajax({url:"http://11.23.2.170:10021/service-web/api/callResource",
            data:{"pageSize":10,
                "currentPage":1,
                "TONGYIXINYONGDAIMA":$("#tym").val(),
                "appID":"5492d86b175d479695f7b31cbaf126a0",
                "uniqueIdentifies":"intelligenceData_021",
                "sm4token":"b9d959df34742c351a40c6016f939a89b0c8e31f5b252a330c3e7bcfb8d9a78ad5d69ce8f7a6ed3c717b3070173101c4b12030b4608a7aa4a33e44e134c4186d067dd66080852ff210e3d63f9bd71651"},
            type:"POST",
            contentType:"application/x-www-form-urlencoded",
            dataType:"json",
            success:function(result){
                $("#result").html(result.msg);
                empty();
                if(result.code!=200){
                    alert("系统繁忙，请稍后重试。");
                }else{
                    $("#hidediv").css('display','block');
                    var data=result.data.datas;
                    var str="";
                    if(data.length<1){
                        $("#tds").append("无查询结果");
                    }else{
                        for(i=0;i<data.length;i++){
                            str="<tr><td>"+data[i].TONGYIXINYONGDAIMA+"</td>"
                                +"<td>"+data[i].NRLY+"</td>"
                                +"<td>"+data[i].RKSJ+"</td>"
                                +"<td>"+data[i].SXLX+"</td>"
                                +"<td>"+data[i].ZHUTILEIXING+"</td>"
                                +"<td>"+data[i].MDLX+"</td>"
                                +"<td>"+data[i].QYMC+"</td>"
                                +"<td>"+data[i].LRDW+"</td></tr>";
                            $("#tds").append(str)
                        }
                    }
                }
            }});
    });
    function empty() {
        $("#tds").empty();
        $("#tds").append("<tr>" +
            "            <th>统一代码</th>" +
            "            <th>纳入理由</th>" +
            "            <th>入库时间</th>" +
            "            <th>事项类型</th>" +
            "            <th>主体类型</th>" +
            "            <th>名单类型</th>" +
            "            <th>企业名称</th>" +
            "            <th>列入单位</th>" +
            "        </tr>");
    }
</script>
</body>
</html>
