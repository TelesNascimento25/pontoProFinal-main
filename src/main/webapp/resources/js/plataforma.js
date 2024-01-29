var horariosTrabalho = [];

function calcular(tipo) {
    var entrada = document.getElementById("entradaHorario").value;
    var saida = document.getElementById("saidaHorario").value;
    var entradaMarcacao = document.getElementById("entradaMarcacao").value;
    var saidaMarcacao = document.getElementById("saidaMarcacao").value;

    $.post("/pontoPro/calculo", { entrada: entrada, saida: saida, entradaMarcacao: entradaMarcacao, 
        saidaMarcacao: saidaMarcacao }, function(data) {
        var tabela = document.getElementById("tabela" + tipo);
        var corpoTabela = tabela.getElementsByTagName('tbody')[0];
        corpoTabela.innerHTML = "";

        data[tipo].forEach(function(item) {
            var novaLinha = corpoTabela.insertRow(-1);
            var celulaEntrada = novaLinha.insertCell(0);
            var celulaSaida = novaLinha.insertCell(1);
            var celulaTipo = novaLinha.insertCell(2);

            celulaEntrada.innerHTML = item.entrada;
            celulaSaida.innerHTML = item.saida;
            celulaTipo.innerHTML = item[tipo.toLowerCase()];
        });
    });
};

function adicionarRegistro(formId, tabelaId) {
    var form = document.getElementById(formId);
    var entrada = form.querySelector('#entradaHorario').value;
    var saida = form.querySelector('#saidaHorario').value;

    var erro = document.getElementById('erro');
    erro.style.display = 'none'; 
    if (!entrada || !saida) {
        erro.innerHTML = 'Por favor, preencha os horários de entrada e saída.';
        erro.style.display = 'block'; 
        return;
    }

    if (saida <= entrada) {
        erro.innerHTML = 'O horário de saída não pode ser menor ou igual ao horário de entrada.';
        erro.style.display = 'block';
        return;
    }

    var tabela = document.getElementById(tabelaId);
    var thead = tabela.getElementsByTagName('thead')[0];

        var novaLinhaThead = thead.insertRow(0);
        var celulaEntrada = novaLinhaThead.insertCell(0);
        var celulaSaida = novaLinhaThead.insertCell(1);
        celulaEntrada.innerHTML = 'Horário de Entrada no Trabalho: ' + entrada;
        celulaSaida.innerHTML = 'Horário de Saída no Trabalho: ' + saida;

    horariosTrabalho.push({ entrada: entrada, saida: saida, marcacoes: [] });
}


function adicionarMarcacao(formId, tabelaId) {
	debugger;
    var form = document.getElementById(formId);
    var entradaMarcacao = form.querySelector('#entradaMarcacao').value;
    var saidaMarcacao = form.querySelector('#saidaMarcacao').value;

    var erro = document.getElementById('erro');
    erro.style.display = 'none'; 

    if (!entradaMarcacao || !saidaMarcacao) {
        erro.innerHTML = 'Por favor, preencha os horários de entrada e saída.';
        erro.style.display = 'block'; 
        return;
    }

    if (saidaMarcacao <= entradaMarcacao) {
        erro.innerHTML = 'O horário de saída não pode ser menor ou igual ao horário de entrada.';
        erro.style.display = 'block'; 
        return;
    }

    if (horariosTrabalho.length == 0) {
        erro.innerHTML = 'Por favor, adicione um horário de trabalho antes de adicionar uma marcação.';
        erro.style.display = 'block'; 
        return;
    }

    var tabela = document.getElementById(tabelaId);
    var tbody = tabela.getElementsByTagName('tbody')[0];

    var novaLinha = tbody.insertRow(-1);
    var celulaEntrada = novaLinha.insertCell(0);
    var celulaSaida = novaLinha.insertCell(1);
    celulaEntrada.innerHTML = 'Ponto de Entrada batido ás: ' + entradaMarcacao;
    celulaSaida.innerHTML = 'Ponto de Saída batido ás: ' + saidaMarcacao;

    horariosTrabalho[horariosTrabalho.length - 1].marcacoes.push({ entrada: entradaMarcacao, saida: saidaMarcacao });
}


function calcularDiferenca() {
    var atrasos = [];
    var horasExtras = [];

    horariosTrabalho.forEach(function (horario) {
        var entradaTrabalho = new Date("1970-01-01 " + horario.entrada);
        var saidaTrabalho = new Date("1970-01-01 " + horario.saida);

        horario.marcacoes.forEach(function (marcacao) {
            var entradaMarcacao = new Date("1970-01-01 " + marcacao.entrada);
            var saidaMarcacao = new Date("1970-01-01 " + marcacao.saida);

            if (entradaMarcacao > entradaTrabalho) {
                atrasos.push({ entrada: horario.entrada, saida: marcacao.entrada, atraso: calcularDiferencaMinutos(entradaTrabalho, entradaMarcacao) });
            }
            if (saidaMarcacao < saidaTrabalho) {
                atrasos.push({ entrada: marcacao.saida, saida: horario.saida, atraso: calcularDiferencaMinutos(saidaMarcacao, saidaTrabalho) });
            }

            if (entradaMarcacao < entradaTrabalho) {
                horasExtras.push({ entrada: entradaMarcacao, saida: entradaTrabalho, horaExtra: calcularDiferencaMinutos(entradaMarcacao, entradaTrabalho) });
            }
            if (saidaMarcacao > saidaTrabalho) {
                horasExtras.push({ entrada: saidaTrabalho, saida: saidaMarcacao, horaExtra: calcularDiferencaMinutos(saidaTrabalho, saidaMarcacao) });
            }
        });
    });

    document.getElementById('mensagemErroHoraExtra').style.display = 'none';
    document.getElementById('mensagemSucessoAtraso').style.display = 'none';

    if (atrasos.length === 0) {
        document.getElementById('mensagemSucessoAtraso').style.display = 'block';
    }

    if (horasExtras.length === 0) {
        document.getElementById('mensagemErroHoraExtra').style.display = 'block';
    }

    return {
        atrasos: atrasos,
        horasExtras: horasExtras
    };
}


function calcularDiferencaMinutos(inicio, fim) {
    var diff = (fim - inicio) / 60000; 
    return Math.round(diff);
}

function calcularAtrasos() {
    var diferenca = calcularDiferenca();
    var atrasos = diferenca.atrasos;

    limparTabela("tabelaAtraso");

    preencherTabelaAtraso("tabelaAtraso", atrasos);
}

function calcularHorasExtras() {
    var diferenca = calcularDiferenca();
    var horasExtras = diferenca.horasExtras;

    limparTabela("tabelaHoraExtra");

    preencherTabelaHoraExtra("tabelaHoraExtra", horasExtras);
}

function limparTabela(idTabela) {
    var tabela = document.getElementById(idTabela);
    var corpoTabela = tabela.getElementsByTagName('tbody')[0];
    corpoTabela.innerHTML = "";
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


function formatarHora(data) {
    var opcoes = { hour: '2-digit', minute: '2-digit' };
    return data.toLocaleTimeString('pt-BR', opcoes);
}


function millisecondsToTime(ms) {
    var seconds = Math.floor((ms / 1000) % 60);
    var minutes = Math.floor((ms / (1000 * 60)) % 60);
    var hours = Math.floor((ms / (1000 * 60 * 60)) % 24);

    hours = hours < 10 ? '0' + hours : hours;
    minutes = minutes < 10 ? '0' + minutes : minutes;

    return hours + ":" + minutes;
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
