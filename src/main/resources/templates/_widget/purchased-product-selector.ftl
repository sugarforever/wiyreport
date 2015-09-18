<#import "../modules/pagination-bar.ftl" as paginationBar>
<div class="product-selector">
    <@paginationBar.pagination
        divId="product-selector-pagination-bar"
        api="/rest/report/product-purchase/product/" + RequestParameters.consumerId
        page=0
        callback="rebuildProductSelectorTable" />
    <table cellpadding="0" cellspacing="0">
        <thead>
            <tr>
                <th class="title">商品名称</th>
                <th class="purchased">累计购买次数</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>
<script type="text/javascript">
    function rebuildProductSelectorTable(products) {
        var productSelectorTable = $(".product-selector table tbody");
        productSelectorTable.empty();
        $.each(products, function (k, product) {
            var row = $("<tr></tr>");
            row.append($('<td class="title">' + product.title + '</td>'));
            row.append($('<td class="purchased">' + product.purchased + '</td>'));

            row.bind('click', function (event) {
                vaseline.onProductSelected(product.numIid, product.title);
            });
            productSelectorTable.append(row);
        });
    }
</script>