<%@include file="/WEB-INF/jspf/header.jspf"%>

<head>
    <title>Title</title>
</head>
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
<body>

</body>
</html>
