<div class="product-selector">
    <div class="pagination"><a class="prev" href="#">上一页</a><span class="current">1</span><a class="next" href="#">下一页</a></div>
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
    $(document).ready(function() {
        var productSelectorTable = $(".product-selector table tbody");
        var prevAnchor = $(".product-selector .pagination .prev");
        var nextAnchor = $(".product-selector .pagination .next");
        var currentPageSpan = $(".product-selector .pagination .current");
        function fetchPagination(page) {
            vaseline.fetchProductPurchasedByConsumer(${RequestParameters.consumerId}, page, function (products) {
                productSelectorTable.empty();
                if (page == 0) {
                    prevAnchor.data("status", "disabled");
                    prevAnchor.addClass("disabled");
                } else {
                    prevAnchor.data("status", "enabled");
                    prevAnchor.removeClass("disabled");
                }
                if (products.length == 0) {
                    nextAnchor.data("status", "disabled");
                    nextAnchor.addClass("disabled");
                } else {
                    nextAnchor.data("status", "enabled");
                    nextAnchor.removeClass("disabled");
                }
                currentPageSpan.text(page + 1);
                $.each(products, function (k, product) {
                    var row = $("<tr></tr>");
                    row.append($('<td class="title">' + product.title + '</td>'));
                    row.append($('<td class="purchased">' + product.purchased + '</td>'));

                    row.bind('click', function (event) {
                        vaseline.onProductSelected(product.numIid, product.title);
                    });
                    productSelectorTable.append(row);
                });
            });
        }

        function getCurrentPageInt() {
            return parseInt($(".product-selector .pagination .current").text());
        }

        fetchPagination(getCurrentPageInt() - 1);

        prevAnchor.bind('click', function(event) {
            if (prevAnchor.data("status") != "disabled")
                fetchPagination(getCurrentPageInt() - 2);
            event.preventDefault();
        });
        nextAnchor.bind('click', function() {
            if (nextAnchor.data("status") != "disabled")
                fetchPagination(getCurrentPageInt());
            event.preventDefault();
        });
    });
</script>
