<div class="product-combo">
    <h1 class="report-title">商品购买组合网络图谱</h1>
    <div id="product-combo-network-topo"></div>
    <script type="text/javascript">
        var network = null;
        $(document).ready(function() {
            $.getJSON("/rest/report/product-purchase-combo/", function(response) {
                if (network != null) {
                    network.destroy();
                }
                var container = document.getElementById('product-combo-network-topo');
                var options = {
                    interaction: {
                        hover: true,
                    },
                    nodes: {
                        shape: 'dot',
                        color: {
                            hover: {
                                background: '#fcf141'
                            }
                        }
                    },
                    edges: {
                        color: {
                            hover: '#fcf141'
                        }
                    }
                };
                network = new vis.Network(container, response, options);
            });
        });
    </script>
</div>