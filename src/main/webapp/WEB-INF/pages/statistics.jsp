<%@include file="/WEB-INF/jspf/header.jspf" %>

<header>
    <div><h1><a href="/">Super Taxi</a></h1></div>
    <nav>
        <c:choose>
            <c:when test="${requestScope.lang == 'en'}">
                <a href="javascript:setLang('ua')"><fmt:message key="lang"/></a>
            </c:when>
            <c:otherwise>
                <a href="javascript:setLang('en')"><fmt:message key="lang"/></a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>
<script type="text/javascript">
    function setLang(lang) {
        document.cookie = "lang=" + lang + ";";
        location.reload();
    }
</script>
<head>

    <meta charset="UTF-8">
    <title>Statistics</title>
    <link rel="stylesheet" href="css/statistics.css">

</head>
<body>
<div class="row">
    <div class="container">

        <form action="/statistics" method="get">

            <fmt:message key="filterUser"/>
            <input name="userName" value="<c:out value="${requestScope.userField}"/>">
            <fmt:message key="filterDate"/>
            <input name="date" type="date" value="<c:out value="${requestScope.dateField}"/>">

            <select class="form-control" id="records" name="orderBy">
                <c:choose>

                    <c:when test="${requestScope.currOrder.equals('byDate')}">
                        <option value="noOrder"><fmt:message key="noOrdering"/></option>
                        <option value="byDate" selected><fmt:message key="dateOrdering"/></option>
                        <option value="byCost"><fmt:message key="costOrdering"/></option>
                    </c:when>

                    <c:when test="${requestScope.currOrder.equals('byCost')}">
                        <option value="noOrder"><fmt:message key="noOrdering"/></option>
                        <option value="byDate"><fmt:message key="dateOrdering"/></option>
                        <option value="byCost" selected><fmt:message key="costOrdering"/></option>
                    </c:when>

                    <c:otherwise>
                        <option value="noOrder" selected><fmt:message key="noOrdering"/></option>
                        <option value="byDate"><fmt:message key="dateOrdering"/></option>
                        <option value="byCost"><fmt:message key="costOrdering"/></option>
                    </c:otherwise>

                </c:choose>
            </select>

            <button type="submit"><fmt:message key="submit"/></button>
        </form>


        <table class="table responsive" id="sort">
            <thead>
            <tr>
                <th scope="col"><fmt:message key="userName"/></th>
                <th scope="col"><fmt:message key="carName"/></th>
                <th scope="col"><fmt:message key="orderDate"/></th>
                <th scope="col"><fmt:message key="costOrder"/>, <fmt:message key="uah"/></th>
            </tr>
            </thead>
            <tbody>

            <c:forEach items="${statsList}" var="order">
                <tr>
                    <td data-table-header=<fmt:message key="userName"/>>
                            ${order.userName}
                    </td>
                    <td data-table-header=<fmt:message key="carName"/>>
                            ${order.carName}
                    </td>
                    <td data-table-header=<fmt:message key="orderDate"/>>
                            ${order.orderDate}
                    </td>
                    <td data-table-header=<fmt:message key="costOrder"/>, <fmt:message key="uah"/>>
                            ${order.cost}
                    </td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>

    <nav aria-label="Navigation for statistics">
        <ul class="pagination">
            <c:if test="${currentPage != 1}">
                <li class="page-item"><a class="page-link"
                                         href="/statistics?recordsPerPage=${recordsPerPage}&currentPage=${currentPage-1}&currFilter=${requestScope.currFilter}&userName=${requestScope.userField}&date=${requestScope.dateField}&orderBy=${requestScope.currOrder}"><fmt:message key="previous"/></a>
                </li>
            </c:if>

            <c:forEach begin="1" end="${noOfPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <li class="page-item active"><a class="page-link">
                                ${i} <span class="sr-only"><fmt:message key="current"/></span></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link"
                                                 href="/statistics?recordsPerPage=${recordsPerPage}&currentPage=${i}&currFilter=${requestScope.currFilter}&userName=${requestScope.userField}&date=${requestScope.dateField}&orderBy=${requestScope.currOrder}">${i}</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage lt noOfPages}">
                <li class="page-item"><a class="page-link"
                                         href="/statistics?recordsPerPage=${recordsPerPage}&currentPage=${currentPage+1}&currFilter=${requestScope.currFilter}&userName=${requestScope.userField}&date=${requestScope.dateField}&orderBy=${requestScope.currOrder}"><fmt:message key="next"/></a>
                </li>
            </c:if>
        </ul>
    </nav>
</div>

</body>
</html>
