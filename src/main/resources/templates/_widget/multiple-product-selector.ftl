<#macro multipleProductSelector wrapper_id css_classes>
<#import "../modules/pagination-bar.ftl" as paginationBar>
<div class="product-selector ${css_classes}" id="${wrapper_id}">
    <div class="product-select">
        <div>
            <input type="button" class="product-select-button" /><input type="text" disabled class="product-title" placeholder="点击图标选择商品进行特定商品购买组合分析"/>
        </div>
        <div class="selected-products">
            <ul></ul>
        </div>
        <div class="clearfix"></div>
        <div class="dialog hide">
            <@paginationBar.pagination
            divId="pagination-${wrapper_id}"
            api="/rest/report/products/"
            page=0
            pageSize=10
            callback="rebuildProductSelectorTable" />
            <table cellpadding="0" cellspacing="0">
                <thead>
                <tr>
                    <th class="checkbox"></th>
                    <th class="picture"></th>
                    <th class="title"></th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>

</div>
<script type="text/javascript">
    var builder = new MultipleProductsSelectBuilder("#${wrapper_id}", function(productNumIid, productTitle, productPicturePath) {
    });

    function rebuildProductSelectorTable(products) {
        builder.rebuildProductSelectorTable(products);
    }
</script>
</#macro>