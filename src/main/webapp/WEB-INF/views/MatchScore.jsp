<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<html>
<head>
    <title>Match Score</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/match-score.css">
</head>
<body>

<%-- ===== Заголовок с именами игроков ===== --%>
<div class="match-header">
    <div class="player-name">${matchScore.playerOneName}</div>
    <div class="vs-badge">VS</div>
    <div class="player-name">${matchScore.playerSecondName}</div>
</div>

<%-- ===== Завершённый матч ===== --%>
<c:choose>
    <c:when test="${matchScore.isFinished}">
        <table class="players-table">
            <thead>
            <tr>
                <th>Имя игрока</th>
                <c:forEach items="${matchScore.completedSets}" var="set" varStatus="status">
                    <th>Set ${status.index + 1}</th>
                </c:forEach>
                <th>Set ${fn:length(matchScore.completedSets) + 1}</th>
            </tr>
            </thead>
            <tbody>
                <%-- Игрок 1 --%>
            <tr>
                <td>${matchScore.playerOneName}</td>
                <c:forEach items="${matchScore.completedSets}" var="set">
                    <td>
                            ${set.playerOneGames}
                        <c:if test="${set.isTieBreak}">
                            <span class="tb-badge">(${set.tieBreakPlayerOnePoints})</span>
                        </c:if>
                    </td>
                </c:forEach>
                <td>
                        ${matchScore.currentSet.playerOneGames}
                    <c:if test="${matchScore.currentSet.isTieBreak}">
                        <span class="tb-badge">(${matchScore.currentSet.tieBreakPlayerOnePoints})</span>
                    </c:if>
                </td>
            </tr>
                <%-- Игрок 2 --%>
            <tr>
                <td>${matchScore.playerSecondName}</td>
                <c:forEach items="${matchScore.completedSets}" var="set">
                    <td>
                            ${set.playerSecondGames}
                        <c:if test="${set.isTieBreak}">
                            <span class="tb-badge">(${set.tieBreakPlayerSecondPoints})</span>
                        </c:if>
                    </td>
                </c:forEach>
                <td>
                        ${matchScore.currentSet.playerSecondGames}
                    <c:if test="${matchScore.currentSet.isTieBreak}">
                        <span class="tb-badge">(${matchScore.currentSet.tieBreakPlayerSecondPoints})</span>
                    </c:if>
                </td>
            </tr>
            </tbody>
        </table>

        <%-- ЕДИНСТВЕННАЯ плашка "Матч завершён" --%>
        <div class="match-finished-banner">
            <h2>🏆 Матч завершён! Победитель: ${matchScore.winnerName}</h2>
            <div class="action-buttons">
                <form action="${pageContext.request.contextPath}/new-match" method="get" style="display:inline;">
                    <button type="submit" class="btn-primary">Новый матч</button>
                </form>
                <form action="${pageContext.request.contextPath}/matches" method="get" style="display:inline;">
                    <button type="submit" class="btn-secondary">История матчей</button>
                </form>
            </div>
        </div>
    </c:when>

    <%-- ===== Активный матч ===== --%>
    <c:otherwise>
        <table class="players-table">
            <thead>
            <tr>
                <th>Имя игрока</th>
                <c:forEach items="${matchScore.completedSets}" var="set" varStatus="status">
                    <th>Set ${status.index + 1}</th>
                </c:forEach>
                <th>Set ${fn:length(matchScore.completedSets) + 1}</th>
                <th>Game</th>
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
                <%-- Игрок 1 --%>
            <tr>
                <td>${matchScore.playerOneName}</td>

                <c:forEach items="${matchScore.completedSets}" var="set">
                    <td>
                            ${set.playerOneGames}
                        <c:if test="${set.isTieBreak}">
                            <span class="tb-badge">(${set.tieBreakPlayerOnePoints})</span>
                        </c:if>
                    </td>
                </c:forEach>

                <td>${matchScore.currentSet.playerOneGames}</td>

                <td class="game-cell">
                    <c:choose>
                        <c:when test="${matchScore.currentSet.isTieBreak}">
                            <span class="tb-badge">TB</span>
                            <span class="tb-score">${matchScore.currentSet.tieBreakPlayerOnePoints}</span>
                        </c:when>
                        <c:otherwise>
                            ${matchScore.playerOneCurrentGameScore}
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <form action="${pageContext.request.contextPath}/match-score" method="post">
                        <input type="hidden" name="uuid" value="${matchScore.matchUuid}">
                        <input type="hidden" name="playerId" value="${matchScore.playerOneId}">
                        <button type="submit" class="btn-add-point">+ Point</button>
                    </form>
                </td>
            </tr>

                <%-- Игрок 2 --%>
            <tr>
                <td>${matchScore.playerSecondName}</td>

                <c:forEach items="${matchScore.completedSets}" var="set">
                    <td>
                            ${set.playerSecondGames}
                        <c:if test="${set.isTieBreak}">
                            <span class="tb-badge">(${set.tieBreakPlayerSecondPoints})</span>
                        </c:if>
                    </td>
                </c:forEach>

                <td>${matchScore.currentSet.playerSecondGames}</td>

                <td class="game-cell">
                    <c:choose>
                        <c:when test="${matchScore.currentSet.isTieBreak}">
                            <span class="tb-badge">TB</span>
                            <span class="tb-score">${matchScore.currentSet.tieBreakPlayerTwoPoints}</span>
                        </c:when>
                        <c:otherwise>
                            ${matchScore.playerSecondCurrentGameScore}
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <form action="${pageContext.request.contextPath}/match-score" method="post">
                        <input type="hidden" name="uuid" value="${matchScore.matchUuid}">
                        <input type="hidden" name="playerId" value="${matchScore.playerSecondId}">
                        <button type="submit" class="btn-add-point">+ Point</button>
                    </form>
                </td>
            </tr>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

</body>
</html>