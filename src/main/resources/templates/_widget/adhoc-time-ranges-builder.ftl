<#macro adhocTimeRangesBuilder wrapper_id>
<div id="${wrapper_id}" class="adhoc-time-ranges-builder">
    <form class="datetime-form" action="#">
        <div class="time-ranges">
            <div class="time-range">
                <label for="datetime-start">起始日期</label><input id="datetime-start-total-fee" class="datetimepicker datetime-start" type="text" >
                <label for="datetime-end">结束日期</label><input id="datetime-end-total-fee" class="datetimepicker datetime-end" type="text" >
            </div>
        </div>
        <div class="actions">
            <input type="button" value="+" class="add" />
        </div>
        <input type="submit" value="查询" class="submit" />
    </form>
</div>
<script type="text/javascript">
    $(document).ready(function() {
       $("#${wrapper_id} .add").click(function(e) {
           var timeRanges = $("#${wrapper_id} .time-ranges");
           var cloned = timeRanges.find("div:first-child").clone();
           timeRanges.append(cloned);
           vaseline.initDateTimePickers();
       });
    });
</script>
</#macro>