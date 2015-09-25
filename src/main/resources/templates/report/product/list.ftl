<#import "../../modules/pagination-bar.ftl" as paginationBar>
<div class="product-list">
    <div class="report-title">商品列表</div>
    <@paginationBar.pagination divId="product-list-pagination-bar" api="/rest/report/products/" page=0 callback="rebuildProductMatrixTable" />
    <table class="product-matrix" cellpadding="0" cellspacing="0">
        <thead>
        <tr class="product">
            <th class="picture"></th>
            <th class="title">商品名称</th>
            <th class="price">最近成交价格(元)</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>

    <script type="text/javascript">
        function rebuildProductMatrixTable(products) {
            var tableBody = $(".product-list .product-matrix tbody");
            tableBody.empty();
            $.each(products, function(k, v) {
                var row = $('<tr class="product"></tr>');
                row.append($('<td class="picture"><img src="' + v.picturePath + '"/></td>'));
                row.append($('<td class="title">' + v.title + '</td>'));
                row.append($('<td class="price">' + v.price + '</td>'));

                tableBody.append(row);
            });
        }
    </script>
</div>