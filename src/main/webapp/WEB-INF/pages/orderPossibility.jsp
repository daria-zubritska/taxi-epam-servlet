<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="css/order.css" type="text/css"/>
    <header>
        <div><h1><a href="/">Super Taxi</a></h1></div>
        <nav>
            <%--        <a href="/myPage">My space</a>--%>
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
<body>

<div class="main-block">
    <h1><fmt:message key="orderSubmitForm"/></h1>
    <form action="/orderSubmit" method="post">

        <c:choose>
            <c:when test="${requestScope.absentUserChoice != null}">
            <h1><fmt:message key="chooseOther"/></h1>
            </c:when>
        </c:choose>

        <h3><fmt:message key="carClass"/></h3>
        <div class="metod">

            <c:forEach items="${orderChoice}" var="order">
                <div>
                    <input type="radio" name="class" id=${order.id} value=${order.id}/>
                    <label class="radio" for=${order.id}>${order.carName} ${order.passengers}</label>
                </div>
            </c:forEach>

        </div>

        <c:choose>
            <c:when test="${requestScope.error != null}">
                <div style="color:red; text-align: center;"><fmt:message key="${requestScope.error}"/></div>
            </c:when>
        </c:choose>

        <button type="submit" class="button"><fmt:message key="submit"/></button>
        <button href="/" class="button"><fmt:message key="cancel"/></button>
    </form>
</div>
</body>
</html>
