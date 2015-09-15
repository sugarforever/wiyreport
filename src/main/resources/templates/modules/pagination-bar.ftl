<#macro pagination divId api page callback>
<div id="${divId}">
    <div class="pagination">
        <a class="prev" href="#">上一页</a>
        <span class="current">1</span>
        <a class="next" href="#">下一页</a>
    </div>
</div>
<script type="text/javascript">
    var prevAnchor = $("#${divId} .pagination .prev");
    var nextAnchor = $("#${divId} .pagination .next");
    var currentPageSpan = $("#${divId} .pagination .current");
    function fetchPagination(page) {
        vaseline.callPaginationApi("${api}", page, function (response) {
            if (page == 0) {
                prevAnchor.data("status", "disabled");
                prevAnchor.addClass("disabled");
            } else {
                prevAnchor.data("status", "enabled");
                prevAnchor.removeClass("disabled");
            }
            if (response.length == 0) {
                nextAnchor.data("status", "disabled");
                nextAnchor.addClass("disabled");
            } else {
                nextAnchor.data("status", "enabled");
                nextAnchor.removeClass("disabled");
            }
            currentPageSpan.text(page + 1);

            ${callback}(response);
        });
    }

    function getCurrentPageInt() {
        return parseInt($("#${divId} .pagination .current").text());
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
</script>
</#macro>