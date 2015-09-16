<div class="consumer-list">
    <h1 class="report-title">买家统计</h1>
    <table class="consumer-matrix" cellpadding="0" cellspacing="0">
        <thead>
            <tr>
                <th>买家昵称</th>
                <th>订单总数</th>
                <th>初次支付</th>
                <th>最近支付</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>

    <script type="text/javascript">
        var consumersMatrixTableBody = $(".consumer-list .consumer-matrix tbody");;
        consumersMatrixTableBody.empty();
        $(document).ready(function(){
            vaseline.fetchConsumers(0, function(response) {
                $.each(response, function(k, v) {
                    var row = $('<tr class="consumer"></tr>');
                    row.append($('<td class="nick">' + v.consumerNick + '</td>'));
                    row.append($('<td class="count-of-bills">' + v.countOfBills + '</td>'));
                    row.append($('<td class="first-paid">' + v.strFirstPaid + '</td>'));
                    row.append($('<td class="latest-paid">' + v.strLatestPaid + '</td>'));
                    row.append($('<td class="more"><a href="?category=consumer&report=more&consumerId=' + v.id + '">更多</a></td>'));

                    consumersMatrixTableBody.append(row);
                });
            });
        });
    </script>
</div>