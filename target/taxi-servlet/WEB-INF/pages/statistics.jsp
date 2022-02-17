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
    <link rel='stylesheet'
          href='https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/css/dataTables.bootstrap.min.css'>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,300i,500" rel="stylesheet"/>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="css/statistics.css">

</head>
<body>
<div class="row">
    <div class="container">

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

            <%--            <tr>--%>
            <%--                <td data-table-header="Title">Parent Adolescent Relationship Factors and Adolescent Outcomes Among High-Risk Families.</td>--%>
            <%--                <td data-table-header="Authors">Matthew Withers, Lenore McWey, Mallory Lucier-Greer</td>--%>
            <%--                <td data-table-header="Journal">Family Relations</td>--%>
            <%--                <td data-table-header="Date">Jan. 2017</td>--%>
            <%--            </tr>--%>

            </tbody>
        </table>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/jquery.dataTables.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/dataTables.bootstrap.min.js"></script>
<script src='https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.0/moment.min.js'></script>
<script src="js/statistics.js"></script>

</body>
</html>
