<div class="centerwell">
    <h1 class="report-title">商品分析</h1>
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
        $(document).ready(function(){
            vaseline.fetchProducts(".centerwell .product-matrix tbody", 0);
        });
    </script>
</div>