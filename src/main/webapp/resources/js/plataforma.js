var horariosTrabalho = [];

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
    var form = document.getElementById(formId);
    var entradaMarcacao = form.querySelector('#entradaMarcacao').value;
    var saidaMarcacao = form.querySelector('#saidaMarcacao').value;

    var tabela = document.getElementById(tabelaId);
    var tbody = tabela.getElementsByTagName('tbody')[0];

    var novaLinha = tbody.insertRow(-1);
    var celulaEntrada = novaLinha.insertCell(0);
    var celulaSaida = novaLinha.insertCell(1);
    celulaEntrada.innerHTML = 'Ponto de Entrada batido ás: ' + entradaMarcacao;
    celulaSaida.innerHTML = 'Ponto de Saída batido ás: ' + saidaMarcacao;

    horariosTrabalho[horariosTrabalho.length - 1].marcacoes.push({ entrada: entradaMarcacao, saida: saidaMarcacao });
}

function calcularDiferencaMinutos(inicio, fim) {
    var diff = (fim - inicio) / 60000; 
    return Math.round(diff);
}

function calcularHorasExtras() {
    var diferenca = calcularDiferenca();
    var horasExtras = diferenca.horasExtras;

    limparTabela("tabelaHoraExtra");

    preencherTabelaHoraExtra("tabelaHoraExtra", horasExtras);
}

function calcularDiferenca() {
    var atrasos = [];
    var horasExtras = [];

    horariosTrabalho.forEach(function (bloco) {
        var entradaTrabalho = new Date("1970-01-01 " + bloco.entrada);
        var saidaTrabalho = new Date("1970-01-01 " + bloco.saida);

        bloco.marcacoes.forEach(function (marcacao) {
            var entradaMarcacao = new Date("1970-01-01 " + marcacao.entrada);
            var saidaMarcacao = new Date("1970-01-01 " + marcacao.saida);

            if (entradaMarcacao < entradaTrabalho) {
                horasExtras.push({ entrada: marcacao.entrada, saida: bloco.entrada, horaExtra: calcularDiferencaMinutos(entradaMarcacao, entradaTrabalho) });
            }

            if (saidaMarcacao > saidaTrabalho) {
                horasExtras.push({ entrada: bloco.saida, saida: marcacao.saida, horaExtra: calcularDiferencaMinutos(saidaTrabalho, saidaMarcacao) });
            }

            if (entradaMarcacao > entradaTrabalho) {
                atrasos.push({ entrada: bloco.entrada, saida: marcacao.entrada, atraso: calcularDiferencaMinutos(entradaTrabalho, entradaMarcacao) });
            }

            if (saidaMarcacao < saidaTrabalho) {
                atrasos.push({ entrada: marcacao.saida, saida: bloco.saida, atraso: calcularDiferencaMinutos(saidaMarcacao, saidaTrabalho) });
            }
        });
    });

    document.getElementById('mensagemErroHoraExtra').style.display = horasExtras.length === 0 ? 'block' : 'none';
    document.getElementById('mensagemSucessoAtraso').style.display = atrasos.length === 0 ? 'block' : 'none';

    return {
        atrasos: atrasos,
        horasExtras: horasExtras
    };
}

function limparTabela(tabelaId) {
    var tabela = document.getElementById(tabelaId);
    var tbody = tabela.getElementsByTagName('tbody')[0];
    tbody.innerHTML = '';
}

function preencherTabelaHoraExtra(tabelaId, horasExtras) {
    var tabela = document.getElementById(tabelaId);
    var tbody = tabela.getElementsByTagName('tbody')[0];

    horasExtras.forEach(function (horaExtra) {
        var novaLinha = tbody.insertRow(-1);
        var celulaPrevisto = novaLinha.insertCell(0);
        var celulaBatido = novaLinha.insertCell(1);
        var celulaTempo = novaLinha.insertCell(2);

        celulaPrevisto.innerHTML = horaExtra.entrada;
        celulaBatido.innerHTML = horaExtra.saida;
        celulaTempo.innerHTML = horaExtra.horaExtra;
    });
}
