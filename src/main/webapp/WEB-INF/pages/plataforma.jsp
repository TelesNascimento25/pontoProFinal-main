<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="br.com.pontoPro.model.IntervaloDeTempo" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Sistema de Ponto Eletrônico</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-timepicker@2.3.0/css/bootstrap-timepicker.min.css" rel="stylesheet">
    <link rel="stylesheet" href="styles.css">
    <link href="${pageContext.request.contextPath}/resources/style2.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    
</head>
<body>
    <div class="container">
   <div class="logo-container mb-4 <% if (request.getAttribute("mensagemErro") != null || request.getAttribute("mensagemSucesso") != null) { %>alert-space<% } %>">
                      <img src="${pageContext.request.contextPath}/resources/images/logo.jpg" alt="Logo pontoPro" class="mb-4" style="width: 100px; height: 100px; display: block; margin: auto;">
    </div>
        <h1>Sistema de Ponto Eletrônico</h1>

     <ul class="nav nav-tabs" id="navTabs">
            <li class="nav-item">
                <a class="nav-link active" id="ponto-tab" data-toggle="tab" href="#tabPonto">Marcação de Ponto</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="atraso-tab" data-toggle="tab" href="#tabAtraso">Atraso</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="horaExtra-tab" data-toggle="tab" href="#tabHoraExtra">Hora Extra</a>
            </li>
        </ul>

        <div class="tab-content mt-4">
            <div class="tab-pane fade show active" id="tabPonto">
                <div class="card">
                    <div class="card-body">
                        <h2 class="card-title font-weight-bold text-left">Horário de Trabalho</h2>
                        <div class="table-responsive">
                            <div id="erro" class="alert alert-danger" style="display: none;"></div>
                            <table class="table table-bordered table-hover" id="tabelaHorarioTrabalho">
                                <tbody>
                                    <!-- Linhas da tabela preenchidas dinamicamente -->
                                </tbody>
                            </table>
                        </div>
                        <form id="formHorarioTrabalho">
                            <div class="row mt-3 justify-content-end">
                                <div class="col-md-4">
                                    <label for="entradaHorario" class="form-label">Entrada:</label>
                                    <input type="time" class="form-control" id="entradaHorario" placeholder="HH:MM">
                                </div>
                                <div class="col-md-4">
                                    <label for="saidaHorario" class="form-label">Saída:</label>
                                    <input type="time" class="form-control" id="saidaHorario" placeholder="HH:MM">
                                </div>
                                <div class="col-md-4 d-flex align-items-center justify-content-center">
                                    <button type="button" class="btn btn-primary" onclick="adicionarRegistro()">Adicionar Horário de Trabalho</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

               <div class="card mt-4">
                    <div class="card-body">
                        <h2 class="card-title font-weight-bold text-left">Marcações</h2>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover" id="tabelaMarcacoes">
                                <tbody id="corpo-tabelaMarcacoes">
                                    <!-- Linhas da tabela preenchidas dinamicamente -->
                                </tbody>
                            </table>
                        </div>
                        <form id="formMarcacoes">
                            <div class="row mt-3">
                                <div class="col-md-4">
                                    <label for="entradaMarcacao" class="form-label">Entrada Marcação:</label>
                                    <input type="time" class="form-control" id="entradaMarcacao" placeholder="HH:MM">
                                </div>
                                <div class="col-md-4">
                                    <label for="saidaMarcacao" class="form-label">Saída Marcação:</label>
                                    <input type="time" class="form-control" id="saidaMarcacao" placeholder="HH:MM">
                                </div>
                                <div class="col-md-4 d-flex align-items-center justify-content-center mt-3">
                                    <button type="button" class="btn btn-primary" onclick="adicionarMarcacao()">Adicionar Marcação de Ponto</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="tab-pane fade" id="tabAtraso">
                <div class="card">
                    <div class="card-body">
                        <h2 class="mt-4">Atraso</h2>
                        <div class="table-responsive">
                        <div id="mensagemSucessoAtraso" class="alert alert-success" style="display: none;">
						    Você não possui atrasos, Parabéns!
						</div>
                            <table class="table table-bordered table-hover" id="tabelaAtraso">
                                <thead>
                                    <tr>
                                        <th>Horário Previsto de Trabalho </th>
                                        <th>Horário de Batimento de Ponto </th>
                                        <th>Tempo de Atraso (min)</th>
                                    </tr>
                                </thead>
                                <tbody id="corpo-tabelaAtraso">
                                </tbody>
                            </table>
                        </div>
                       <div class="row mt-3">
                            <div class="col-md-4">
                                <button type="button" class="btn btn-primary" onclick="calcularAtrasos()">Calcular Atrasos</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

			           <div class="tab-pane fade" id="tabHoraExtra">
			    <div class="card">
			        <div class="card-body">
			            <h2 class="mt-4">Hora Extra</h2>
			            <div class="table-responsive">
			                <div id="mensagemErroHoraExtra" class="alert alert-danger" style="display: none;">
			                    Você não possui horas extras!
			                </div>
			                <table class="table table-bordered table-hover" id="tabelaHoraExtra">
			                    <thead>
			                        <tr>
			                            <th>Horário Previsto de Trabalho</th>
			                            <th>Horário de Batimento de Ponto</th>
			                            <th>Tempo de Horas Extras (min)</th>
			                        </tr>
			                    </thead>
			                    <tbody id="corpo-tabelaHoraExtra">
			                    </tbody>
			                </table>
			            </div>
			            <div class="row mt-3">
			                <div class="col-md-4">
			                    <button type="button" class="btn btn-primary" onclick="calcularHorasExtras()">Calcular Horas Extras</button>
			                </div>
			            </div>
			        </div>
			    </div>
			</div>
        </div>
    </div>
      <script src="${pageContext.request.contextPath}/resources/js/plataforma.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-timepicker/0.5.2/js/bootstrap-timepicker.min.js"></script>
    </div>
</body>
</html>
