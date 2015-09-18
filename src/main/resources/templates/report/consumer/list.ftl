<#import "../../modules/pagination-bar.ftl" as paginationBar>
<div class="consumer-list">
    <h1 class="report-title">买家列表</h1>
    <@paginationBar.pagination
    divId="consumer-list-pagination-bar"
    api="/rest/report/consumers/"
    page=0
    callback="rebuildConsumerMatrixTable" />
    <table class="consumer-matrix" cellpadding="0" cellspacing="0">
        <thead>
            <tr>
                <th class="nick">买家昵称</th>
                <th class="count-of-bills">订单总数</th>
                <th class="first-paid">初次支付</th>
                <th class="latest-paid">最近支付</th>
                <th class="more"></th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>
<script type="text/javascript">
    function rebuildConsumerMatrixTable(consumers) {
        var consumersMatrixTableBody = $(".consumer-list .consumer-matrix tbody");;
        consumersMatrixTableBody.empty();
        $.each(consumers, function (k, consumer) {
            var row = $('<tr class="consumer"></tr>');
            row.append($('<td class="nick">' + consumer.consumerNick + '</td>'));
            row.append($('<td class="count-of-bills">' + consumer.countOfBills + '</td>'));
            row.append($('<td class="first-paid">' + consumer.strFirstPaid + '</td>'));
            row.append($('<td class="latest-paid">' + consumer.strLatestPaid + '</td>'));
            row.append($('<td class="more"><a href="?category=consumer&report=more&consumerId=' + consumer.id + '">更多</a></td>'));

            consumersMatrixTableBody.append(row);
        });
    }
</script>