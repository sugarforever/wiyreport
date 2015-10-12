<#macro adhocTimeRangesBuilder wrapper_id multiple=false>
<div id="${wrapper_id}" class="shadow-border adhoc-time-ranges-builder">
    <div class="datetime-form">
        <div class="time-ranges">
            <div class="time-range">
                <label for="datetime-start">时间区段</label><input id="datetime-start-total-fee" class="datetimepicker datetime-start" type="text" >
                <label for="datetime-end">~</label><input id="datetime-end-total-fee" class="datetimepicker datetime-end" type="text" >
                <#if multiple>
                    <img class="delete" src="/images/delete_button.png" />
                </#if>
            </div>
        </div>
        <#if multiple>
            <div class="actions">
                <input type="button" class="add" />
            </div>
        </#if>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        var self = this;
        self.timeRanges = $("#${wrapper_id} .time-ranges");
        self.cloned = self.timeRanges.find("div:first-child").clone();
        $("#${wrapper_id} .add").click(function(e) {
            var c = self.cloned.clone();
            c.find(".delete").click(function(e) {
               $(this).parent().remove();
            });
            self.timeRanges.append(c);
            vaseline.initDateTimePickers();
        });

        $("#${wrapper_id} .delete").click(function(e) {
            $(this).parent().remove();
        });
    });
</script>
</#macro>