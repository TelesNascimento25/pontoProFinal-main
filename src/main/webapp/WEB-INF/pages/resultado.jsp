<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="tab-pane fade" id="resultado" role="tabpanel" aria-labelledby="resultado-tab">
    <div class="mt-4">
        <h2>Resultado do Ponto Eletrônico</h2>
        <div class="table-responsive">
            <table class="table table-bordered table-hover" id="tabelaResultado">
                <thead>
                    <tr>
                        <th>Entrada</th>
                        <th>Saída</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="atraso" items="${atrasos}">
                        <tr>
                            <td>${atraso.entrada}</td>
                            <td>${atraso.saida}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
