<html>
    <#include "header.ftl">
<body>
    <#include "leading.ftl">
    <div class="center-container">
        <#include "leftnav.ftl">
        <#include "report/${Request.category}/${Request.report}.ftl">
    </div>
    <#include "footer.ftl">
</body>
</html>