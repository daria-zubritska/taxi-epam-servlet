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
    <h1><fmt:message key="orderForm"/></h1>
    <form action="/">
        <div class="info">
            <select>
                <option value="from" disabled selected><fmt:message key="urLoc"/></option>
                <%--                <option value="2">2</option>--%>
                <%--                <option value="4">4</option>--%>
                <%--                <option value="6">6</option>--%>
            </select>
            <select>
                <option value="to" disabled selected><fmt:message key="destination"/></option>
                <%--                <option value="2">2</option>--%>
                <%--                <option value="4">4</option>--%>
                <%--                <option value="6">6</option>--%>
            </select>

            <select>
                <option value=
                                "passengers" disabled selected>
                    <fmt:message key="numOfPassengers"/></option>
                <option value="2">2</option>
                <option value="4">4</option>
                <option value="6">6</option>
            </select>

        </div>
        <h3><fmt:message key="carClass"/></h3>
        <div class="metod">
            <div>
                <input type="radio" value="none" id="radioOne" name="metod" value="cheap" checked/>
                <label for="radioOne" class="radio"><fmt:message key="cheap"/></label>
            </div>
            <div>
                <input type="radio" value="none" id="radioTwo" name="metod" value="comfort"/>
                <label for="radioTwo" class="radio"><fmt:message key="comfort"/></label>
            </div>
            <div>
                <input type="radio" value="none" id="radioThree" name="metod" value="business"/>
                <label for="radioThree" class="radio"><fmt:message key="business"/></label>
            </div>
        </div>
        <button href="/orderSubmit" class="button"><fmt:message key="submit"/></button>
    </form>
</div>
</body>
</html>
