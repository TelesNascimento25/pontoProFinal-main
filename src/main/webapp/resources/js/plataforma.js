
// Adicione um identificador único para cada linha
var idContador = 0;
var contadorHorarios = 0;

function preencherTabela(tabelaId, dados) {
    debugger;
    var tabela = document.getElementById(tabelaId);
    var tbody = tabela.querySelector('tbody');

    // Preencher a tabela com os dados
    for (var i = 0; i < dados.length; i++) {
        var novaLinha = tbody.insertRow(-1);
        novaLinha.id = 'linha' + idContador++;

        var celulaEntrada = novaLinha.insertCell(-1);
        var celulaSaida = novaLinha.insertCell(-1);

        // Adicione botões de edição e exclusão
        var celulaAcao = novaLinha.insertCell(-1);
        celulaAcao.innerHTML = '<button onclick="editarLinha(\'' + novaLinha.id + '\')">Editar</button>';

        var registro = dados[i].registro || dados[i].marcacao;
        celulaEntrada.innerHTML = registro.entrada;
        celulaSaida.innerHTML = registro.saida;
    }
}


function adicionarRegistro() {
    debugger;
    if (contadorHorarios >= 3) {
        alert('Você já preencheu 3 horários de trabalho.');
        return;
    }

    var form = document.getElementById('formHorarioTrabalho');
    var entrada = form.querySelector('#entradaHorario').value;
    var saida = form.querySelector('#saidaHorario').value;

    // Validar e enviar dados
    if (entrada && saida) {
        var dados = { acao: 'adicionarRegistro', entrada: entrada, saida: saida };
        enviarDadosParaServidor('AdicionarRegistroServlet', dados, function (response) {
            if (response.success) {
                contadorHorarios++;
            }
            callbackAdicionar(response, 'tabelaHorarioTrabalho');
        });
    } else {
        alert('Preencha os horários de entrada e saída.');
    }
}

function adicionarMarcacao() {
    debugger;
    var form = document.getElementById('formMarcacoes');
    var entradaMarcacao = form.querySelector('#entradaMarcacao').value;
    var saidaMarcacao = form.querySelector('#saidaMarcacao').value;

    // Validar e enviar dados
    if (entradaMarcacao && saidaMarcacao) {
        var dados = { acao: 'adicionarMarcacao', entradaMarcacao: entradaMarcacao, saidaMarcacao: saidaMarcacao };
        enviarDadosParaServidor('AdicionarMarcacaoServlet', dados, function (response) {
            callbackAdicionar(response, 'tabelaMarcacoes');
        });
    } else {
        alert('Preencha os horários de entrada e saída para a marcação.');
    }
}

function editarLinha(idLinha) {
    debugger;
    var linha = document.getElementById(idLinha);
    var celulas = linha.cells;

    // Preencher campos de entrada com valores da linha selecionada
    document.getElementById('entradaHorario').value = celulas[0].textContent;
    document.getElementById('saidaHorario').value = celulas[1].textContent;

    // Adicionar um botão de salvar
    document.getElementById('formHorarioTrabalho').innerHTML += '<button onclick="salvarEdicao(\'' + idLinha + '\')">Salvar</button>';
}

// Função para salvar a edição
function salvarEdicao(idLinha) {
    debugger;
    var linha = document.getElementById(idLinha);
    var celulas = linha.cells;

    // Atualizar os valores na linha com os novos valores dos campos de entrada
    celulas[0].textContent = document.getElementById('entradaHorario').value;
    celulas[1].textContent = document.getElementById('saidaHorario').value;

    // Remover o botão de salvar
    document.getElementById('formHorarioTrabalho').innerHTML = '';

    // Limpar os campos de entrada
    document.getElementById('entradaHorario').value = '';
    document.getElementById('saidaHorario').value = '';
}

function enviarDadosParaServidor(urlServlet, dados, successCallback, errorCallback) {
    debugger;
    $.ajax({
        type: 'POST',
        url: urlServlet,
        data: dados,
        success: function (response) {
            debugger;
            if (successCallback) {
                debugger;
                successCallback(response);
            }
        },
        error: function () {
            debugger;
            if (errorCallback) {
                errorCallback();
            } else {
                alert('Erro ao enviar dados para o servidor.');
            }
        }
    });
}

function callbackAdicionar(response, tabelaId) {
    debugger;
    if (response.success) {
        preencherTabela(tabelaId, [response]);
    } else {
        alert('Erro ao adicionar.');
    }
}

function calcularAtrasos() {
    // Obter os dados dos horários de trabalho e das marcações de trabalho
    var horariosTrabalho = obterHorariosTrabalho();
    var marcacoesTrabalho = obterMarcacoesTrabalho();

    // Enviar os dados para o servlet
    $.ajax({
        type: 'POST',
        url: 'CalcularAtrasoHoraExtraServlet',
        data: {
            horariosTrabalho: horariosTrabalho,
            marcacoesTrabalho: marcacoesTrabalho
        },
        success: function (response) {
            // Lógica de callback, se necessário
            console.log(response);
        },
        error: function () {
            alert('Erro ao enviar dados para o servidor.');
        }
    });
}

function calcularHorasExtras() {
	debugger;
    // Obter os dados dos horários de trabalho e das marcações de trabalho
    var horariosTrabalho = obterHorariosTrabalho();
    var marcacoesTrabalho = obterMarcacoesTrabalho();

    // Enviar os dados para o servlet
  $.ajax({
    type: 'POST',
    url: 'CalcularAtrasoHoraExtraServlet',
    data: {
        horariosTrabalho: JSON.stringify(horariosTrabalho),
        marcacoesTrabalho: JSON.stringify(marcacoesTrabalho)
    },
        success: function (response) {
            // Lógica de callback, se necessário
            console.log(response);
        },
        error: function () {
            alert('Erro ao enviar dados para o servidor.');
        }
    });
}





function preencherTabelaHoraExtra(idTabela, dados) {
    var tabela = document.getElementById(idTabela);
    var corpoTabela = tabela.getElementsByTagName('tbody')[0];

    dados.forEach(function (item) {
        var novaLinha = corpoTabela.insertRow(-1);
        var celulaEntradaPrevista = novaLinha.insertCell(0);
        var celulaBatimentoPonto = novaLinha.insertCell(1);
        var celulaHoraExtra = novaLinha.insertCell(2);

        celulaEntradaPrevista.innerHTML = formatarHora(item.entrada); 
        celulaBatimentoPonto.innerHTML = formatarHora(item.saida); 
        celulaHoraExtra.innerHTML = item.horaExtra;

        celulaHoraExtra.classList.add('text-success');
    });
}

function obterHorariosTrabalho() {
	debugger;
    // Obter todas as linhas da tabela de horários de trabalho
    var linhas = document.getElementById('tabelaHorarioTrabalho').rows;

    // Inicializar um array para armazenar os horários de trabalho
    var horariosTrabalho = [];

    // Iterar sobre cada linha da tabela
    for (var i = 0; i < linhas.length; i++) {
        // Obter as células da linha atual
        var celulas = linhas[i].cells;

        // Obter o horário de entrada e saída da linha atual
        var entrada = celulas[0].textContent;
        var saida = celulas[1].textContent;

        // Adicionar o horário de trabalho ao array
        horariosTrabalho.push({ entrada: entrada, saida: saida });
    }

    return horariosTrabalho;
}

function obterMarcacoesTrabalho() {
	debugger;
    // Obter todas as linhas da tabela de marcações de trabalho
    var linhas = document.getElementById('tabelaMarcacoes').rows;

    // Inicializar um array para armazenar as marcações de trabalho
    var marcacoesTrabalho = [];

    // Iterar sobre cada linha da tabela
    for (var i = 0; i < linhas.length; i++) {
        // Obter as células da linha atual
        var celulas = linhas[i].cells;

        // Obter o horário de entrada e saída da linha atual
        var entrada = celulas[0].textContent;
        var saida = celulas[1].textContent;

        // Adicionar a marcação de trabalho ao array
        marcacoesTrabalho.push({ entrada: entrada, saida: saida });
    }

    return marcacoesTrabalho;
}


function preencherTabelaAtraso(idTabela, dados) {
    var tabela = document.getElementById(idTabela);
    var corpoTabela = tabela.getElementsByTagName('tbody')[0];

    dados.forEach(function (item) {
        var novaLinha = corpoTabela.insertRow(-1);
        var celulaEntrada = novaLinha.insertCell(0);
        var celulaSaida = novaLinha.insertCell(1);
        var celulaAtraso = novaLinha.insertCell(2);

        celulaEntrada.innerHTML = item.entrada;
        celulaSaida.innerHTML = item.saida;
        celulaAtraso.innerHTML = item.atraso;

        celulaAtraso.classList.add('text-danger');
    });
}

$(document).ready(function () {
    $('#navTabs a').on('click', function (e) {
        e.preventDefault();
        $(this).tab('show');
    });
});
