<html>
    <#include "header.ftl">
<body>
    <#include "leading.ftl">
    <div class="center-container">
        <#include "leftnav.ftl">
        <div class="centerwell">
            <#include "report/${Request.category}/${Request.report}.ftl">
        </div>
    </div>
    <#include "footer.ftl">
</body>
</html>