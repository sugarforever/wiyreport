<div class="trade-total-fee">
    <div class="report-title">订单金额报表</div>
    <form class="datetime-form" action="#">
        <div class="datetime-selector">
            <label for="datetime-start">起始日期</label><input id="datetime-start-total-fee" class="datetimepicker datetime-start" type="text" >
            <label for="datetime-end">结束日期</label><input id="datetime-end-total-fee" class="datetimepicker datetime-end" type="text" >
            <input type="submit" value="查询" class="submit" />
        </div>
    </form>
    <div class="report-canvas-wrapper">
        <div>
            <canvas class="chart report-canvas"></canvas>
        </div>
    </div>
</div>
<script type="text/javascript">
    jQuery('.centerwell .datetimepicker').datetimepicker({
        lang:'zh',
        i18n:{
            zh:{
                months:[
                    '一月','二月','三月','四月',
                    '五月','六月','七月','八月',
                    '九月','十月','十一月','十二月',
                ],
                dayOfWeek:[
                    "周日", "周一", "周二", "周三",
                    "周四", "周五", "周六",
                ]
            }
        },
        timepicker:false,
        format:'Y-m-d'
    });
    $(document).ready(function() {
        var context = $(".report-canvas").get(0).getContext("2d");;
        $(".datetime-form").submit(function(event) {
            vaseline.totalFeeReport(context, "#datetime-start-total-fee", "#datetime-end-total-fee");
            event.preventDefault();
        });
        vaseline.totalFeeReport(context, "#datetime-start-total-fee", "#datetime-end-total-fee");
    });
</script>