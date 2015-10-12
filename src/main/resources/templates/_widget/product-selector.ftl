<#import "../modules/pagination-bar.ftl" as paginationBar>
<div class="product-selector">
    <@paginationBar.pagination
        divId="product-selector-pagination-bar"
        api="/rest/report/products/"
        page=0
        pageSize=20
        callback="rebuildProductSelectorTable" />
    <table cellpadding="0" cellspacing="0">
        <thead>
            <tr>
                <th class="picture"></th>
                <th class="title">商品名称</th>
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
            row.append($('<td class="picture"><img src="' + product.picturePath + '" /></td>'));
            row.append($('<td class="title">' + product.title + '</td>'));
            row.bind('click', function (event) {
                vaseline.onProductSelected(product.numberIid, product.title);
            });
            productSelectorTable.append(row);
        });
    }
</script>